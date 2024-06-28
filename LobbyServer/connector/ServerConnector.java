package connector;

import com.google.gson.Gson;
import connector.dto.*;
import connector.enumerate.ProcessNumber;
import connector.manager.ClientAccessManager;
import connector.manager.DatabaseAccessManager;
import connector.manager.WebSocketManager;
import connector.storage.SessionStorage;
import controller.ManagementController;

import javax.websocket.Session;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

/**
 * 全ての通信を取り扱うクラス
 * ロビーサーバから行う通信はこのクラスを通して行われる
 * @author 鈴木直人
 */
public class ServerConnector {
    /**
     * アプリケーションサーバにデータを送信するときに利用するフィールド
     */
    private WebSocketManager applicationServerAccessManager;
    /**
     * クライアントにデータを送信する時に利用するフィールド
     */
    private ClientAccessManager clientAccessManager;
    /**
     * データベースでクエリを実行する時に利用するフィールド
     */
    private DatabaseAccessManager databaseAccessManager;
    private static final ServerConnector instance = new ServerConnector();
    /**
     * ServerConnectorのインスタンスを取得するメソッド
     * @return ServerConnectorのインスタンス
     */
    public static ServerConnector getInstance(){
        return instance;
    }
    private final Gson gson = new Gson();
    public void setApplicationServerAccessManager(WebSocketManager applicationServerAccessManager) {
        System.out.println("[ServerConnector] setWebSocketManagerApplication() applicationServerAccessManager=" + applicationServerAccessManager + "}");
        instance.applicationServerAccessManager = applicationServerAccessManager;
    }
    public void setClientAccessManager(ClientAccessManager clientAccessManager) {
        System.out.println("[ServerConnector] setClientAccessManager() {clientAccessManager=" + clientAccessManager + "}");
        this.clientAccessManager = clientAccessManager;
    }

    public void setDatabaseAccessManager(DatabaseAccessManager databaseAccessManager){
        System.out.println("[ServerConnector] setDatabaseAccessManager() {databaseAccessManager=" + databaseAccessManager + "}");
        instance.databaseAccessManager = databaseAccessManager;
    }
    public void connectToApplicationServer(){
        System.out.println("[ServerConnector] connectToApplicationServer()");
        instance.applicationServerAccessManager.connect();
    }

    /**
     * アカウントを作成する通信を実現するメソッド
     * データベースにユーザＩＤとパスワードを登録する
     * @param userID ユーザＩＤ
     * @param password　パスワード
     * @throws SQLException 同一ユーザＩＤが存在する場合などに投げられる
     */
    public void createAccount(String userID, String password) throws SQLException {
        System.out.println("[ServerConnector] createAccount() {userID=" + userID + ", password=" + password + "}");
        databaseAccessManager.createAccount(new User(userID, password, 1000));
    }

    /**
     * 与えられたユーザＩＤとパスワードでログインできるかどうか問い合わせる通信を実現するメソッド
     * @param userID　ユーザＩＤ
     * @param password　パスワード
     * @return ログインできるかどうか
     * @throws SQLException SQLサーバとの通信で発生する
     */
    public boolean userCheck(String userID, String password) throws SQLException {
        System.out.println("[ServerConnector] userCheck() {userID=" + userID + ", password=" + password + "}");
        return (databaseAccessManager.userCheck(new User(userID, password, 0)) == 1);
    }

    /**
     * ログインに関する通信を実現するメソッド
     * @param userID　ユーザID
     * @param password　パスワード
     * @return レートを含むユーザクラス
     * @throws SQLException　SQLサーバとの通信で発生する
     */
    public User login(String userID, String password) throws SQLException {
        System.out.println("[ServerConnector] login() {userID=" + userID + ", password=" + password + "}");
        return databaseAccessManager.login(new User(userID, password, 1000));
    }

    /**
     * ゲーム開始に関する通信を実現するメソッド
     * アプリケーションサーバに与えられたユーザのリストを送る
     * @param users マッチングしたユーザ４人のリスト
     * @throws IllegalArgumentException usersが不正だった場合（nullなど）に投げられる
     */
    public void startGame(String[] users) throws IllegalArgumentException {
        System.out.println("[ServerConnector] startGame() {users=" + Arrays.toString(users) + "}");
        applicationServerAccessManager.sendData(new DtoStartGame(ProcessNumber.START_GAME, users));
    }

    /**
     * ランキングを取得する通信を実現するメソッド
     * @return ランキング上位10名のユーザリスト
     * @throws SQLException SQLサーバとの通信で発生する
     */
    public List<User> getRank() throws SQLException {
        System.out.println("[ServerConnector] getRank()");
        return databaseAccessManager.getRank();
    }

    /**
     * 別サーバから受け取ったデータを処理するメソッド
     * 受け取ったメッセージ（JSON形式）を含まれるprocessNumberに従いコントローラを呼び出す
     * @param data 受け取ったメッセージ（JSON形式）
     * @param session データを受け取ったセッション
     */
    public String receiveData(String data, Session session) {
        System.out.println("[ServerConnector] receiveData() {data=" + data + ", session=" + session.getId() + "}");
        Dto dto = gson.fromJson(data, Dto.class);

        switch (dto.getProcessNumber()){
            case CREATE_ACCOUNT -> {
                dto = gson.fromJson(data, DtoUser.class);
                System.out.println("[processNumber] CREATE_ACCOUNT {dto="+dto+"}");
                try {
                    ManagementController.getInstance().createAccountUserID(((DtoUser)dto).getUserID(), ((DtoUser)dto).getPassword());
                    dto.setSuccess(true);
                    dto.setMessage("Account successfully created");
                } catch (Exception e) {
                    dto.setSuccess(false);
                    dto.setMessage(e.getMessage());
                }
            }
            case LOGIN -> {
                dto = gson.fromJson(data, DtoUser.class);
                System.out.println("[processNumber] LOGIN {dto="+dto+"}");
                try {
                    User user = ManagementController.getInstance().login(((DtoUser)dto).getUserID(), ((DtoUser)dto).getPassword());
                    SessionStorage.getInstance().put(user.getUserID(), session);
                    ((DtoUser) dto).setRate(user.getRate());
                    dto.setSuccess(true);
                    dto.setMessage(gson.toJson(user));
                    ((DtoUser) dto).setRate(user.getRate());
                } catch (SQLException e) {
                    dto.setSuccess(false);
                    dto.setMessage(e.getMessage());
                }
            }
            case START_MATCHING -> {
                dto = gson.fromJson(data, DtoMatching.class);
                System.out.println("[processNumber] START_MATCHING {dto="+dto+"}");
                ManagementController.getInstance().matching(((DtoMatching)dto).getUserID());
                dto.setSuccess(true);
                dto.setMessage("Matching started");
                System.out.println("Matching user list");
                ManagementController.getInstance().getUserList().forEach(System.out::println);
            }
            case STOP_MATCHING -> {
                dto = gson.fromJson(data, DtoMatching.class);
                System.out.println("[processNumber] STOP_MATCHING {dto="+dto+"}");
                ManagementController.getInstance().stopMatching(((DtoMatching) dto).getUserID());
                dto.setSuccess(true);
                dto.setMessage("Stopped matching");
            }
            case GET_RANK -> {
                dto = gson.fromJson(data, DtoRank.class);
                System.out.println("[processNumber] GET_RANK {dto="+dto+"}");
                try {
                    ((DtoRank) dto).setUsers(ManagementController.getInstance().getRank());
                    dto.setSuccess(true);
                    dto.setMessage("Got Ranking");
                } catch (SQLException e) {
                    dto.setSuccess(false);
                    dto.setMessage(e.getMessage());
                }
            }
            case LOGOUT -> {
                System.out.println("[processNumber] LOGOUT {dto="+dto+"}");
                SessionStorage.getInstance().remove(session);
            }
        }
        return gson.toJson(dto); //処理結果を通信元に送る
    }
}

package connector;

import com.google.gson.Gson;
import connector.dto.*;
import connector.manager.ClientAccessManager;
import connector.manager.DatabaseAccessManager;
import connector.manager.LobbyServerAccessManager;
import connector.storage.SessionStorage;
import controller.GameController;

import javax.websocket.Session;
import java.sql.SQLException;

/**
 * 全ての通信を取り扱うクラス
 * アプリケーションサーバから行う通信はこのクラスを通して行われる
 * @author 鈴木直人
 */
public class ServerConnector {
    /**
     * クライアントにデータを送信するときに利用するフィールド
     */
    private ClientAccessManager clientAccessManager;
    /**
     * ロビーサーバにデータを送信するときに利用するフィールド
     */
    private LobbyServerAccessManager lobbyServerAccessManager;
    /**
     * データベースでクエリを実行する時に利用するフィールド
     */
    private DatabaseAccessManager databaseAccessManager;
    private static final ServerConnector instance = new ServerConnector();
    private final Gson gson = new Gson();

    /**
     * ServerConnectorのインスタンスを生成するメソッド
     * @return ServerConnectorのインスタンス
     */
    public static ServerConnector getInstance(){
        return instance;
    }
    public void setDatabaseAccessManager(DatabaseAccessManager databaseAccessManager){
        System.out.println("[ServerConnector] setDatabaseAccessManager() {databaseAccessManager=" + databaseAccessManager + "}");
        instance.databaseAccessManager = databaseAccessManager;
    }
    public void setLobbyServerAccessManager(LobbyServerAccessManager lobbyServerAccessManager){
        System.out.println("[ServerConnector] setLobbyServerAccessManager() {lobbyServerAccessManager=" + lobbyServerAccessManager + "}");
        instance.lobbyServerAccessManager = lobbyServerAccessManager;
    }
    public void setClientAccessManager(ClientAccessManager clientAccessManager){
        System.out.println("[ServerConnector] setClientAccessManager() {clientAccessManager=" + clientAccessManager +"}");
        instance.clientAccessManager = clientAccessManager;
    }

    /**
     * レート更新の通信を実現するメソッド
     * @param userID ユーザＩＤ
     * @param rate　更新後のレート
     * @throws SQLException SQLサーバとの通信で発生する
     */
    public void updateRate(String userID, int rate) throws SQLException {
        System.out.println("[ServerConnector] updateRate() {userID=" + userID + ", rate : " + rate + "}");
        instance.databaseAccessManager.setRate(userID, rate);
    }

    /**
     * レート取得を実現するメソッド
     * @param userID ユーザＩＤ
     * @return レート
     * @throws SQLException SQLサーバとの通信で発生する
     */
    public int getRate(String userID) throws SQLException {
        System.out.println("[ServerConnector] getRate() {userID=" + userID + "}");
        return instance.databaseAccessManager.getRate(userID);
    }

    /**
     * クライアントへのデータ送信を実現するメソッド
     * @param userID ユーザＩＤ
     * @param dto　データ
     * @throws IllegalArgumentException ユーザＩＤに対応するセッションが存在しない際に投げられる
     */
    public void sendDataToClient(String userID,Dto dto) throws IllegalArgumentException {
        System.out.println("[ServerConnector] sendToClient() {userID=" + userID + ", Dto=" + dto + "}");
        clientAccessManager.sendData(SessionStorage.getInstance().get(userID),dto);
    }
    /**
     * 別サーバから受け取ったデータを処理するメソッド
     * 受け取ったメッセージ（JSON形式）を含まれるprocessNumberに従いコントローラを呼び出す
     * @param data 受け取ったメッセージ（JSON形式）
     */
    public String receiveData(String data, Session session) {
        System.out.println("[ServerConnector] receiveData() {data=" + data + ", session=" + session.getId() + "}");
        Dto dto = gson.fromJson(data, Dto.class);

        switch (dto.getProcessNumber()){
            case LOGIN -> { //ログイン時にアプリケーションサーバのSessionStorageも同時に変更する
                dto = gson.fromJson(data, DtoUser.class);
                System.out.println("[processNumber] LOGIN : " + dto);
                SessionStorage.getInstance().put(((DtoUser)dto).getUserID(), session);
                dto.setSuccess(true);
                dto.setMessage("session id was successfully registered");
            }
            case START_GAME -> { //ロビーサーバから
                dto = gson.fromJson(data, DtoStartGame.class);
                System.out.println("[processNumber] START_GAME : " + dto);
                GameController.createRoom(((DtoStartGame)dto).getUsers());
                dto.setSuccess(true);
                dto.setMessage("Game start");
            }
            case INPUT_NUMBER -> { //クライアントから
                dto = gson.fromJson(data, DtoInputNumber.class);
                System.out.println("[processNumber] INPUT_NUMBER : " + dto);
                GameController.setAnswer(((DtoInputNumber)dto).getAnswer(),((DtoInputNumber)dto).getRoomID());
                dto.setSuccess(true);
                dto.setMessage("play successfully finished");
            }
            case LOGOUT -> {
                System.out.println("[processNumber] LOGOUT : " + dto);
                SessionStorage.getInstance().remove(session);
            }
            case EXIT_GAME -> {
                dto = gson.fromJson(data, DtoExitGame.class);
                System.out.println("[processNumber] EXIT_GAME : " + dto);
                // クライアントがゲームから抜けた際の処理
                GameController.setOnCloseUser(((DtoExitGame) dto).getUserID());
            }
        }
        return gson.toJson(dto); //処理結果を通信元に送る
    }
}

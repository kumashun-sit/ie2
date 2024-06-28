package connector;

import com.google.gson.Gson;
import connector.dto.*;
import connector.enumerate.ProcessNumber;
import connector.manager.WebSocketManager;
import controller.BoundaryController;

import java.io.IOException;
import java.util.Objects;

/**
 * 全ての通信を取り扱うクラス
 * クライアントから行う通信はこのクラスを通して行われる
 * @author 鈴木直人
 */
public class ServerConnector {
    /**
     * ロビーサーバにデータを送信するときに利用するフィールド
     */
    private WebSocketManager lobbyServerAccessManager;
    /**
     * アプリケーションサーバにデータを送信するときに利用するフィールド
     */
    private WebSocketManager applicationServerAccessManager;
    private static final ServerConnector instance = new ServerConnector();
    private final Gson gson = new Gson();
    /**
     * ServerConnectorのインスタンスを生成するメソッド
     * @return ServerConnectorのインスタンス
     */
    public static ServerConnector getInstance() {
        return instance;
    }
    public void setWebSocketManagerLobby(WebSocketManager lobbyServerAccessManager) {
        System.out.println("[ServerConnector] setLobbyServerAccessManager() {lobbyServerAccessManager=" + lobbyServerAccessManager+"}");
        instance.lobbyServerAccessManager = lobbyServerAccessManager;
    }
    public void setWebSocketManagerApplication(WebSocketManager applicationServerAccessManager) {
        System.out.println("[ServerConnector] setApplicationServerAccessManager() {applicationServerAccessManager=" + applicationServerAccessManager + "}");
        instance.applicationServerAccessManager = applicationServerAccessManager;
    }
    public void connectToLobbyServer(){
        System.out.println("[ServerConnector] connectToLobbyServer()");
        instance.lobbyServerAccessManager.connect();
    }
    public void connectToApplicationServer(){
        System.out.println("[ServerConnector] connectToApplicationServer()");
        instance.applicationServerAccessManager.connect();
    }

    /**
     * アカウントを作成する通信を実現するメソッド
     * @param userID ユーザＩＤ
     * @param password パスワード
     * @throws IOException 通信上でエラーが発生した際に投げられる
     */
    public void createAccount(String userID, String password) throws IOException {
        instance.lobbyServerAccessManager.sendData(new DtoUser(ProcessNumber.CREATE_ACCOUNT, userID, password));
    }

    /**
     * ログイン処理に関する通信を実現するメソッド
     * @param userID ユーザＩＤ
     * @param password パスワード
     * @throws IOException 通信上でエラーが発生した際に投げられる
     */
    public void login(String userID, String password) throws IOException {
        Dto dto = new DtoUser(ProcessNumber.LOGIN, userID, password);
        instance.lobbyServerAccessManager.sendData(dto);
        instance.applicationServerAccessManager.sendData(dto);
    }

    /**
     * マッチング開始を実現するメソッド
     * @param userID ユーザＩＤ
     * @throws IOException　通信上でエラーが発生した際に投げられる
     */
    public void startMatching(String userID) throws IOException {
        instance.lobbyServerAccessManager.sendData(new DtoMatching(ProcessNumber.START_MATCHING, userID));
    }

    /**
     * マッチング中断を実現するメソッド
     * @param userID ユーザＩＤ
     * @throws IOException　通信上でエラーが発生した際に投げられる
     */
    public void stopMatching(String userID) throws IOException {
        instance.lobbyServerAccessManager.sendData(new DtoMatching(ProcessNumber.STOP_MATCHING, userID));
    }

    /**
     * ランキング取得を実現するメソッド
     * @throws IOException 通信上でエラーが発生した際に投げられる
     */
    public void getRank() throws IOException {
        instance.lobbyServerAccessManager.sendData(new DtoRank(ProcessNumber.GET_RANK));
    }

    /**
     * ゲーム中の数字入力処理を実現するメソッド
     * @param roomID ルームＩＤ
     * @param answer　入力した答え
     * @throws IOException 通信上でエラーが発生した際に投げられる
     */
    public void inputNumber(int roomID, int[] answer) throws IOException {
        instance.applicationServerAccessManager.sendData(new DtoInputNumber(ProcessNumber.INPUT_NUMBER, roomID, answer));
    }

    /**
     * ゲーム中にゲームから切断した際に行うべき処理を実現するメソッド
     * @param userID ユーザＩＤ
     * @param roomID　ルームＩＤ
     * @throws IOException　通信上でエラーが発生した際に投げられる
     */
    public void exitGame(String userID, int roomID) throws IOException {
        instance.applicationServerAccessManager.sendData(new DtoExitGame(ProcessNumber.EXIT_GAME, userID, roomID));
    }

    /**
     * ログアウトを実現するメソッド
     * @throws IOException 通信上でエラーが発生した際に投げられる
     */
    public void logout() throws IOException {
        instance.lobbyServerAccessManager.sendData(new Dto(ProcessNumber.LOGOUT));
        instance.applicationServerAccessManager.sendData(new Dto(ProcessNumber.LOGOUT));
    }

    /**
     * 別サーバから受け取ったデータを処理するメソッド
     * 受け取ったメッセージ（JSON形式）を含まれるprocessNumberに従いコントローラを呼び出す
     * @param data 受け取ったメッセージ（JSON形式）
     */
    public void receiveData(String data) {
        System.out.println("[ServerConnector] receiveData() {data=" + data +"}");
        Dto dto = gson.fromJson(data, Dto.class);

        switch (dto.getProcessNumber()){
            case CREATE_ACCOUNT -> {
                dto = gson.fromJson(data, DtoUser.class);
                if((dto).isSuccess()){
                    System.out.println("Succeeded to account created : " + dto.getMessage());
                    BoundaryController.getInstance().createAccountSuccess();
                }
                else{
                    System.out.println("Failed to create account : " + dto.getMessage());
                    BoundaryController.getInstance().createAccountFailure(dto.getMessage());
                }
            }
            case LOGIN -> {
                dto = gson.fromJson(data, DtoUser.class);
                if(dto.isSuccess()){
                    //アプリケーションサーバでセッションIDが登録された場合を除く
                    if (!Objects.equals(dto.getMessage(), "session id was successfully registered")) {
                        System.out.println("Succeeded to login : " + dto.getMessage());
                        BoundaryController.getInstance().loginSuccess(((DtoUser) dto).getUserID(), ((DtoUser) dto).getRate());
                    }
                }
                else{
                    System.out.println("Failed to login : " + dto.getMessage());
                    BoundaryController.getInstance().loginFailure(dto.getMessage());
                }
            }
            case GET_RANK -> {
                dto = gson.fromJson(data, DtoRank.class);
                if(dto.isSuccess()){
                    System.out.println("Succeeded to get rank : " + dto.getMessage());
                    BoundaryController.getInstance().getRankSuccess(((DtoRank) dto).getUsers());
                }
                else{
                    System.out.println("Failed to get rank : " + dto.getMessage());
                    BoundaryController.getInstance().getRankFailure();
                }
            }
            case START_MATCHING -> {
                dto = gson.fromJson(data, DtoMatching.class);
                if(dto.isSuccess()){
                    System.out.println("Matching successfully started");
                    BoundaryController.getInstance().startMatchingSuccess();
                }
                else{
                    System.out.println("Failed to start matching");
                    BoundaryController.getInstance().startMatchingFailure();
                }
            }
            case STOP_MATCHING -> {
                if(dto.isSuccess()){
                    System.out.println("Matching stopped");
                    BoundaryController.getInstance().stopMatchingSuccess();
                }
                else {
                    System.out.println("Failed to stop matching");
                    BoundaryController.getInstance().stopMatchingFailure();
                }
            }
            case START_GAME -> {
                dto = gson.fromJson(data, DtoGameInfo.class);
                BoundaryController.getInstance().startGame(
                        ((DtoGameInfo) dto).getRoomID(),
                        ((DtoGameInfo) dto).getTurn(),
                        ((DtoGameInfo) dto).getUsers(),
                        ((DtoGameInfo) dto).getRates());
            }
            case INPUT_NUMBER -> {
                dto = gson.fromJson(data, DtoInputNumber.class);
                if(dto.isSuccess()){
                    System.out.println("play successfully finished");
                }
                else {
                    System.out.println("Failed to play");
                    BoundaryController.getInstance().inputNumberFailure();
                }
            }
            case CHANGE_TURN -> {
                dto = gson.fromJson(data, DtoChangeTurn.class);
                BoundaryController.getInstance().changeTurn(
                        ((DtoChangeTurn) dto).getHit(),
                        ((DtoChangeTurn) dto).getBlow(),
                        ((DtoChangeTurn) dto).getAnswer(),
                        ((DtoChangeTurn) dto).isTimeUp());
            }
            case END_GAME -> {
                dto = gson.fromJson(data, DtoEndGame.class);
                BoundaryController.getInstance().endGame(
                        ((DtoEndGame) dto).getWinnerID(),
                        ((DtoEndGame) dto).getAnswer(),
                        ((DtoEndGame) dto).getAfterRates(),
                        ((DtoEndGame) dto).isIs8TurnSkipped()
                );
            }
        }
    }
}

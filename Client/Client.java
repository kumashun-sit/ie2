import boundary.MainFrame;
import connector.ServerConnector;
import connector.manager.WebSocketManager;
import controller.BoundaryController;

import javax.swing.*;
import com.formdev.flatlaf.FlatIntelliJLaf;

import java.io.IOException;

public class Client implements Runnable {
    //------------------------------------------------------------------------//
    //ローカル用
    private static final String lobbyServerEndpoint = "ws://localhost:8080/app/lobby";
    private static final String applicationServerEndpoint = "ws://localhost:8081/app/application";

    //SZKサーバ用
    //private static final String lobbyServerEndpoint = "ws://172.31.69.25:5000/app/lobby";
    //private static final String applicationServerEndpoint = "ws://172.31.69.25:5001/app/application";

    //TNKサーバ用
//    private static final String lobbyServerEndpoint = "ws://172.30.162.37:5000/app/lobby";
//    private static final String applicationServerEndpoint = "ws://172.30.162.37:5001/app/application";
    //------------------------------------------------------------------------//


    public static void main(String[] args) {
        //ロビーサーバへの接続
        ServerConnector.getInstance().setWebSocketManagerLobby(new WebSocketManager(lobbyServerEndpoint));
        ServerConnector.getInstance().connectToLobbyServer();

        //アプリケーションサーバへの接続
        ServerConnector.getInstance().setWebSocketManagerApplication(new WebSocketManager(applicationServerEndpoint));
        ServerConnector.getInstance().connectToApplicationServer();

        //GUIの起動
        FlatIntelliJLaf.setup(); //FlatLafライブラリの適用
        final JFrame mainFrame = new MainFrame();
        mainFrame.setVisible(true);

        Runtime.getRuntime().addShutdownHook(new Thread(new Client(), "Shutdown"));
    }

    //マッチング待機中の場合、プログラム終了時にマッチングを中断する
    public void run() {
        if (BoundaryController.getInstance().isWaitMatching) BoundaryController.getInstance().stopMatchingRequest();
        if (BoundaryController.getInstance().isPlayingGame) BoundaryController.getInstance().exitGame();
        try {
            ServerConnector.getInstance().logout();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}

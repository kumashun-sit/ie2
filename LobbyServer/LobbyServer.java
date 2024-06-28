//import connector.*;
import connector.ServerConnector;
import connector.endpoint.LobbyServerEndpoint;
import connector.manager.ClientAccessManager;
import connector.manager.DatabaseAccessManager;
import connector.manager.WebSocketManager;
import org.glassfish.tyrus.server.Server;

public class LobbyServer{
    //------------------------------------------------------------------------//
    //ローカル用
    private static final String applicationServerEndpoint = "ws://localhost:8081/app/application";

    //SZKサーバ用
    //private static final String applicationServerEndpoint = "ws://172.31.69.25:5001/app/application";

    //TNKサーバ用
//    private static final String applicationServerEndpoint = "ws://172.30.162.37:5001/app/application";
    //------------------------------------------------------------------------//

    public static void main(String[] args) throws Exception {
        //サーバの起動 --------------------------------------------------------------//
        //ローカル用
        Server server = new Server("localhost", 8080, "/app", null, LobbyServerEndpoint.class);

        //SZKサーバ用
        //Server server = new Server("172.31.69.25", 5000, "/app", null, LobbyServerEndpoint.class);

        //TNKサーバ用
        //Server server = new Server("172.30.162.37", 5000, "/app", null, LobbyServerEndpoint.class);
        //------------------------------------------------------------------------//

        //アプリケーションサーバへの接続
        ServerConnector.getInstance().setApplicationServerAccessManager(new WebSocketManager(applicationServerEndpoint));
        ServerConnector.getInstance().connectToApplicationServer();

        ServerConnector.getInstance().setClientAccessManager(new ClientAccessManager());
        ServerConnector.getInstance().setDatabaseAccessManager(new DatabaseAccessManager());

        try{
            server.start();
            System.in.read();
        } finally {
            server.stop();
        }
    }
}
import connector.endpoint.ApplicationServerEndpoint;
import connector.manager.ClientAccessManager;
import connector.manager.DatabaseAccessManager;
import connector.ServerConnector;
import connector.manager.LobbyServerAccessManager;
import org.glassfish.tyrus.server.Server;

public class ApplicationServer {

    public static void main(String[] args) throws Exception{
        //サーバの起動 --------------------------------------------------------------//
        //ローカル用
        Server server = new Server("localhost", 8081, "/app", null, ApplicationServerEndpoint.class);

        //SZKサーバ用
        //Server server = new Server("172.31.69.25", 5001, "/app", null, ApplicationServerEndpoint.class);

        //TNKサーバ用
//        Server server = new Server("172.30.162.37", 5001, "/app", null, ApplicationServerEndpoint.class);
        //------------------------------------------------------------------------//

        new ApplicationServerEndpoint();
        ServerConnector.getInstance().setLobbyServerAccessManager(new LobbyServerAccessManager());
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

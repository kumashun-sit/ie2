package connector.endpoint;

import connector.ServerConnector;
import connector.manager.LobbyServerAccessManager;
import connector.storage.SessionStorage;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.sql.SQLException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * WebSocket通信のエンドポイント
 * @author 鈴木直人
 */
@ServerEndpoint("/application")
public class ApplicationServerEndpoint {
    @OnOpen
    public void onOpen(Session session){
        System.out.println("[ApplicationServerEndpoint] onOpen : " + session.getId());
        if(LobbyServerAccessManager.getLobbyServerSession() == null) LobbyServerAccessManager.setLobbyServerSession(session);
    }
    @OnMessage
    public String onMessage(final String message, Session session) throws SQLException {
        return ServerConnector.getInstance().receiveData(message, session);
    }
    @OnClose
    public void onClose(Session session){
        System.out.println("[ApplicationServerEndpoint] onClose : " + session.getId() + ", userID : " + SessionStorage.getInstance().get(session));
    }
    @OnError
    public void onError(Session session, Throwable error){
        System.out.println("[ApplicationServerEndpoint] onError : " + session.getId() + " error : " + error);
    }
}
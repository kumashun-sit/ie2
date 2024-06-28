package connector.endpoint;

import connector.ServerConnector;

import javax.websocket.*;

/**
 * ロビーサーバをクライアントとするWebSocket通信のエンドポイント
 * @author 鈴木直人
 */
@ClientEndpoint
public class LobbyServerClientEndpoint {
    @OnOpen
    public void onOpen(Session session){
        System.out.println("[LobbyServerClientEndpoint] onOpen {session=" + session.getId()+"}");
    }
    @OnMessage
    public void onMessage(final String message, final Session session) {
        System.out.println("[LobbyServerClientEndpoint] onMessage from (session : " + session.getId() + ") message : " + message);
        ServerConnector.getInstance().receiveData(message, session);
    }
    @OnClose
    public void onClose(Session session){
        System.out.println("[LobbyServerClientEndpoint] onClose {session=" + session.getId() + "}");
    }
    @OnError
    public void onError(Session session, Throwable error) {
        System.out.println("[LobbyServerClientEndpoint] onError {session=" + session.getId() + ", error=" + error.getMessage()+ "}");
    }
}

package connector.endpoint;

import com.google.gson.Gson;
import connector.ServerConnector;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;

/**
 * ロビーサーバをサーバとするWebSocket通信のエンドポイント
 * @author 鈴木直人
 */
@ServerEndpoint("/lobby")
public class LobbyServerEndpoint {
    private final Gson gson = new Gson();

    @OnOpen
    public void onOpen(Session session){
        System.out.println("[LobbyServerEndpoint] onOpen() {session=" + session.getId() + "}");
    }
    @OnMessage
    public String onMessage(final String message, final Session session) {
        System.out.println("[LobbyServerEndpoint] onMessage from (session : " + session.getId() + ") msg : " + message);
        return ServerConnector.getInstance().receiveData(message, session);
    }
    @OnClose
    public void onClose(Session session){
        System.out.println("[LobbyServerEndpoint] onClose() {session=" + session.getId() + "}");
    }
    @OnError
    public void onError(Session session, Throwable error) {
        System.out.println("[LobbyServerEndpoint] onError() {session=" + session.getId() + ", error=" + error.getMessage() + "}");
    }
}
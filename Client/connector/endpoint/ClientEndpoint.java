package connector.endpoint;

import connector.ServerConnector;

import javax.websocket.*;

/**
 * WebSocket通信のエンドポイント
 * @author 鈴木直人
 */
@javax.websocket.ClientEndpoint
public class ClientEndpoint {
    @OnOpen
    public void onOpen(Session session){
        System.out.println("[ClientEndpoint] onOpen() {session=" + session.getId() + "}");
    }
    @OnMessage
    public void onMessage(final String message, final Session session) {
        System.out.println("[ClientEndpoint] onMessage from (session : " + session.getId() + ") message : " + message);
        ServerConnector.getInstance().receiveData(message);
    }
    @OnClose
    public void onClose(Session session){
        System.out.println("[ClientEndpoint] onClose() {session=" + session.getId() + "}");
    }
    @OnError
    public void onError(Session session, Throwable error){
        System.out.println("[ClientEndpoint] onError() {session=" + session.getId() + ", error=" + error.getMessage() + "}");
    }
}
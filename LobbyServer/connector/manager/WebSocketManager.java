package connector.manager;

import com.google.gson.Gson;
import connector.dto.Dto;
import connector.endpoint.LobbyServerClientEndpoint;

import javax.websocket.ContainerProvider;
import javax.websocket.DeploymentException;
import javax.websocket.Session;
import javax.websocket.WebSocketContainer;
import java.io.IOException;
import java.net.URI;

/**
 * WebSocket通信を開始する時に利用されるクラス
 * @author 鈴木直人
 */
public class WebSocketManager {
    private Session session;
    private WebSocketContainer webSocketContainer;
    private URI uri;
    private final Gson gson = new Gson();
    public WebSocketManager(String uriString){
        this.setWebSocketContainer(ContainerProvider.getWebSocketContainer());
        this.setUri(URI.create(uriString));
    }
    public void sendData(Dto dto) {
        System.out.println("[WebSocketManager] sendData() {uri=" + uri + ", dto=" + dto + "}");
        this.session.getAsyncRemote().sendText(gson.toJson(dto));
    }

    public void setSession(Session session) {
        this.session = session;
    }
    public void setWebSocketContainer(WebSocketContainer webSocketContainer) {
        this.webSocketContainer = webSocketContainer;
    }
    public void setUri(URI uri) {
        this.uri = uri;
    }

    public Session getSession() {
        return session;
    }

    public WebSocketContainer getWebSocketContainer() {
        return webSocketContainer;
    }

    public URI getUri() {
        return uri;
    }

    public void connect(){
        System.out.println("[WebSocketManager] connect() {uri=" + uri + "}");
        try{
            this.setSession(webSocketContainer.connectToServer(new LobbyServerClientEndpoint(), uri));
        } catch (DeploymentException | IOException e){
            e.printStackTrace();
        }
    }
    @Override
    public String toString() {
        return "WebSocketManager{" +
                "session=" + session +
                ", webSocketContainer=" + webSocketContainer +
                ", uri=" + uri +
                '}';
    }
}

package connector.manager;

import com.google.gson.Gson;
import connector.dto.Dto;
import connector.endpoint.ClientEndpoint;

import javax.websocket.ContainerProvider;
import javax.websocket.DeploymentException;
import javax.websocket.Session;
import javax.websocket.WebSocketContainer;
import java.io.IOException;
import java.net.URI;

/**
 * WebSocket通信の管理を行うクラス
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
    public void sendData(Dto dto) throws IOException{
        System.out.println("[WebSocketManager] sendData() {uri = " + uri + ", dto=" + dto + "}");
        this.session.getBasicRemote().sendText(gson.toJson(dto));
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
    public void connect(){
        System.out.println("[WebSocketManager] connect() {uri=" + uri + "}");
        try{
            this.setSession(webSocketContainer.connectToServer(new ClientEndpoint(), uri));
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

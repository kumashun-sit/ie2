package connector.manager;

import com.google.gson.Gson;
import connector.storage.SessionStorage;
import connector.dto.Dto;

import javax.websocket.Session;

/**
 * クライアントへの通信を管理するクラス
 * @author 鈴木直人
 */
public class ClientAccessManager {
    private final Gson gson = new Gson();
    public void sendData(Session session, Dto dto){
        System.out.println("[ClientAccessManager] sendData() {session=" + session.getId() + ", dto=" + dto +"}");
        session.getAsyncRemote().sendText(gson.toJson(dto));
    }
    public void broadcast(Dto dto){
        System.out.println("[ClientAccessManager] broadcast() {dto=" + dto + "}");
        SessionStorage.getInstance()
                .getMap()
                .values()
                .forEach(session -> session.getAsyncRemote().sendText(gson.toJson(dto)));
    }
}

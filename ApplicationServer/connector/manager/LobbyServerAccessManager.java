package connector.manager;

import com.google.gson.Gson;
import connector.dto.Dto;

import javax.websocket.Session;

/**
 * ロビーサーバへの通信を管理するクラス
 * @author 鈴木直人
 */
public class LobbyServerAccessManager {
    private static Session lobbyServerSession;
    private final Gson gson = new Gson();

    public static Session getLobbyServerSession() {
        return lobbyServerSession;
    }

    public static void setLobbyServerSession(Session lobbyServerSession) {
        LobbyServerAccessManager.lobbyServerSession = lobbyServerSession;
    }

    public void sendData(Dto dto){
        System.out.println("[ClientAccessManager] sendData() {session=" + lobbyServerSession.getId() + ", dto=" + dto + "}");
        lobbyServerSession.getAsyncRemote().sendText(gson.toJson(dto));
    }
}

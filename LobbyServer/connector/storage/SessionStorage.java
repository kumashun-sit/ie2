package connector.storage;

import javax.websocket.Session;
import java.util.HashMap;
import java.util.Map;

/**
 * ユーザIDとSessionを紐づけるためのストレージ
 * 互いをキーとしてO(logN)で要素取得可能
 * ServerConnectorの内部で利用する
 * @author 鈴木直人
 */
public class SessionStorage {
    private final Map<String, Session> map = new HashMap<>();
    private final Map<Session, String> reverse = new HashMap<>();
    private static final SessionStorage instance = new SessionStorage();
    public static SessionStorage getInstance(){
        return instance;
    }
    public Map<String, Session> getMap(){
        return instance.map;
    }
    public boolean contains(String userID){
        return instance.map.containsKey(userID);
    }
    public boolean contains(Session session){
        return instance.reverse.containsKey(session);
    }
    public void put(String userID, Session session){
        if(instance.map.containsKey(userID) || instance.reverse.containsKey(session)) this.remove(userID);
        instance.map.put(userID, session);
        instance.reverse.put(session, userID);
    }
    public Session get(String userID){
        if(!instance.map.containsKey(userID)) throw new IllegalArgumentException("User (" + userID + ") is not registered");
        return instance.map.get(userID);
    }
    public String get(Session session){
        if(!instance.reverse.containsKey(session)) throw new IllegalArgumentException("Session (" + session + ") is not registered");
        return instance.reverse.get(session);
    }
    public void remove(String userID){
        Session session = instance.map.get(userID);
        instance.map.remove(userID);
        instance.reverse.remove(session);
    }
    public void remove(Session session){
        this.remove(instance.get(session));
    }
    public void show(){
        System.out.println("[SessionStorage] show()");
        map.forEach((key, session) -> System.out.println(key + " : " + session.getId()));
    }
}

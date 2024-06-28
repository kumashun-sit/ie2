package controller;
//担当：光山誠人

import connector.enumerate.ProcessNumber;
import connector.ServerConnector;
import connector.dto.User;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ManagementController {
  int accountNumber = 0;
  private final List<String> userList = new ArrayList<>();
  private static final ManagementController instance = new ManagementController();

  public static ManagementController getInstance(){
    return instance;
  }

  public List<String> getUserList() {
    return instance.userList;
  }

  /**
   *  アカウント作成
   *  from ServerConnector(LobbyServer) to ServerConnector(LobbyServer) pN1
   */
  public void createAccountUserID(String userID, String password) throws SQLException {
    System.out.println("[ManagementController] createAccountUserID() {userID=" + userID + ", password=" + password + "}");
    ServerConnector.getInstance().createAccount(userID, password);
  }


  /**
   * ログイン
   * from ServerConnector(LobbyServer) to ServerConnector(DatabaseServer) pN2
   */
  public User login(String userID, String password) throws SQLException {
    System.out.println("[ManagementController] login() {userID=" + userID + ", password=" + password + "}");
    if(!ServerConnector.getInstance().userCheck(userID, password)) throw new SQLException("userID or password is incorrect");
    return ServerConnector.getInstance().login(userID, password);
  }


  /**
   * マッチング開始
   *  from ServerConnector(LobbyServer) to ServerConnector(LobbyServer) pN4
   */
  public void matching(String userID) {

    System.out.println("[ManagementController] matching() {userID=" + userID + ", accountNumber=" + accountNumber + "}");

    //ユーザを待機しているユーザリストに追加する
    instance.accountNumber++;
    instance.userList.add(userID);

    //待機中のユーザが4人以上ならゲームを開始できる
    if( instance.accountNumber >= 4 ) {
      //ユーザ4人のリストが必要
      ServerConnector.getInstance().startGame(instance.userList.toArray(new String[0]));

      //index番号 0, 1, 2, 3 の要素をまとめて削除する
      instance.userList.subList(0,4).clear();
      instance.accountNumber = instance.accountNumber - 4;
    }
  }


  /**
   * マッチング中断
   * from ServerConnector(LobbyServer) to ServerConnector(LobbyServer) pN5
   */
  public void stopMatching(String userID) {
    System.out.println("[ManagementController] stopMatching() {userID=" + userID + "}");
    instance.accountNumber--;
    int n = instance.userList.indexOf(userID);
    if(n != -1) { instance.userList.remove(n); }
  }

  public List<User> getRank() throws SQLException {
    System.out.println("[ManagementController] getRank()");
    return ServerConnector.getInstance().getRank();
  }
}

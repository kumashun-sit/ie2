package controller;

import boundary.*;
import connector.ServerConnector;
import connector.dto.*;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.LineBorder;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import entity.Answer;
import entity.Rank;

public class BoundaryController extends Frame {

  private static final BoundaryController instance = new BoundaryController();
  public static BoundaryController getInstance(){ return instance; }

  private JPanel cardPanel; //画面遷移に用いる
  private String myUserID = null; //自分のユーザID
  public int myRate; //自分のレート
  private int myRoomID; //自分の部屋のID
  private int myTurn; //ゲーム開始時に1のとき入力画面 2,3,4のとき待機画面
  private int roomTurn; //部屋のターン
  public List<String> playerList = new ArrayList<>(); //自分の部屋のプレイヤーリスト
  public List<Integer> rateList = new ArrayList<>(); //自分の部屋のプレイヤーのレートリスト
  public DefaultListModel<Answer> logModel = new DefaultListModel<>(); //ログ
  public DefaultListModel<Rank> rankModel = new DefaultListModel<>(); //ランキング
  public boolean isWaitMatching = false;
  public boolean isPlayingGame = false;

  public void setCardPanel(JPanel cardPanel) { this.cardPanel = cardPanel; }

  /**
   * 内部メソッド
   */

  public void changeScreen(String screenName) {
    CardLayout cardLayout = (CardLayout) cardPanel.getLayout();
    cardLayout.show(cardPanel, screenName);
  }

  public void showMessage(String message) {
    JOptionPane.showMessageDialog(null, message);
  }

  public void showErrorMessage(String errorMessage) {
    JLabel label = new JLabel(errorMessage);
    label.setForeground(Color.RED);
    JOptionPane.showMessageDialog(null, label);
  }

  private void setMyTurn() {
    GameScreen.announceLabel.setText("あなたのターンです");
    GameScreen.announceLabel.setForeground(Color.RED);

    for (int i=0; i<4; i++) {
      if (i == myTurn) GameScreen.playerLabelList.get(i).setBorder(new LineBorder(new Color(151, 195, 243), 4,true));
      else GameScreen.playerLabelList.get(i).setBorder(new LineBorder(Color.BLACK, 0, true));
    }

    GameScreen.timerLabel.setForeground(Color.BLACK);
    GameScreen.timerCounter = 30;
    GameScreen.timer.restart();

    for (int i=0; i<12; i++) GameScreen.buttonList.get(i).setEnabled(true);
  }

  private void setOtherTurn() {
    GameScreen.announceLabel.setForeground(Color.BLACK);

    for (int i=0; i<4; i++) {
      if (i == roomTurn % 4)
        GameScreen.playerLabelList.get(roomTurn % 4).setBorder(new LineBorder(new Color(151, 195, 243), 4, true));
      else GameScreen.playerLabelList.get(i).setBorder(new LineBorder(Color.BLACK, 0, true));
    }

    GameScreen.timerLabel.setForeground(Color.BLACK);
    GameScreen.timerCounter = 30;
    GameScreen.timer.restart();

    GameScreen.announceLabel.setText(playerList.get(roomTurn % 4) + "のターンです");

    for (int i=0; i<12; i++) GameScreen.buttonList.get(i).setEnabled(false);
  }

  private static boolean isHalfAlphanumeric(String string) {
    if (!isEmpty(string)) {
      return string.matches("^[0-9a-zA-Z]+$");
    } else {
      return false;
    }
  }

  private static boolean isEmpty(String target) {
    return target == null;
  }

  private static String getOrdinalNumber(int num) {

    if((num % 10 == 1) && (num % 100 != 11)) {
      return num + "st";
    } else if ((num % 10 == 2) && (num % 100 != 12)) {
      return num + "nd";
    } else if ((num % 10 == 3) && (num % 100 != 13)) {
      return num + "rd";
    } else {
      return num + "th";
    }

  }

  /**
   * デバッグ用
   */

  public void debug(String userID, String password) {
    this.myUserID = userID;
    loginRequest(userID, password);
    startMatchingRequest();
  }

  /**
   * アカウントの新規作成
   */

  public void createAccountRequest(String userID, String password) {
    System.out.println("[BoundaryController] createAccountRequest() {userID=" + userID + ", password=" + password + "}");
    //ユーザIDとパスワードが半角英数字かどうかを判定
    if (isHalfAlphanumeric(userID) && isHalfAlphanumeric(password)) {
      //ユーザIDが10文字以内であるかを判定
      if (userID.length() <= 10) {
        try {
          ServerConnector.getInstance().createAccount(userID, password);
        } catch (Exception e) {
          throw new RuntimeException(e);
        }
      } else {
        showErrorMessage("エラー：ユーザIDは10文字以内です．");
        CreateAccountScreen.createAccountButton.setEnabled(true);
      }
    } else {
      showErrorMessage("エラー：不適切な文字列です．");
      CreateAccountScreen.createAccountButton.setEnabled(true);
    }
  }

  public void createAccountSuccess() {
    System.out.println("[BoundaryController] createAccountSuccess(){}");
    showMessage("アカウントの新規作成が完了しました．");

    CreateAccountScreen.userIDField.setText(null);
    CreateAccountScreen.passwordField1.setText(null);
    CreateAccountScreen.passwordField2.setText(null);
    LoginScreen.userIDField.setText(null);
    LoginScreen.passwordField.setText(null);
    CreateAccountScreen.createAccountButton.setEnabled(true);

    changeScreen("Login");
  }

  public void createAccountFailure(String message) {
    System.out.println("[BoundaryController] createAccountFailure(){message=" + message + "}");
    showMessage("アカウントの新規作成に失敗しました．");
    showErrorMessage("エラー：" + message);
    CreateAccountScreen.createAccountButton.setEnabled(true);
  }

  /**
   * ログイン
   */

  public void loginRequest(String userID, String password) {
    System.out.println("[BoundaryController] loginRequest() {userID=" + userID + ", password=" + password + "}");
    //ユーザIDとパスワードが半角英数字かどうかを判定
    if (isHalfAlphanumeric(userID) && isHalfAlphanumeric(password)) {
      try {
        ServerConnector.getInstance().login(userID, password);
      } catch (Exception e) {
        throw new RuntimeException(e);
      }
    } else {
      showErrorMessage("エラー：不適切な文字列です．");
      LoginScreen.loginButton.setEnabled(true);
    }
  }

  public void loginSuccess(String userID, int rate) {
    System.out.println("[BoundaryController] loginSuccess() {}");

    this.myUserID = userID; //ユーザIDを保存
    this.myRate = rate; //レートを保存

    LoginScreen.loginButton.setEnabled(true);
    showMessage("ログインしました．");

    LobbyScreen.playerLabel.setText(myUserID + " としてログイン中" );
    LobbyScreen.rateLabel.setText("現在のレート : " + myRate);
    MatchingScreen.playerLabel.setText(myUserID + " としてログイン中" );
    MatchingScreen.rateLabel.setText("現在のレート : " + myRate);
    LoginScreen.loginButton.setEnabled(true);

    changeScreen("Lobby");
  }

  public void loginFailure(String message) {
    System.out.println("[BoundaryController] loginFailure() {}");
    showMessage("ログインに失敗しました．");
    showErrorMessage("エラー：" + message);
    LoginScreen.loginButton.setEnabled(true);
  }

  /**
   * ログアウト
   */

  public void logout() {
    System.out.println("[BoundaryController] logout() {}");
    this.myUserID = null; //保存したユーザIDを初期化
    showMessage("ログアウトしました．");

    try {
      ServerConnector.getInstance().logout();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    LoginScreen.userIDField.setText(null);
    LoginScreen.passwordField.setText(null);

    changeScreen("Title");
  }

  /**
   * ランキング取得
   */

  public void getRankRequest() {
    System.out.println("[BoundaryController] getRankRequest() {}");
    try {
      ServerConnector.getInstance().getRank();
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  public void getRankSuccess(List<User> userList) {
    System.out.println("[BoundaryController] getRankSuccess() {}");

    Color rankColor = null;
    Color userColor;
    int rankStyle;
    int userStyle;

    for (int i=0; i<userList.size(); i++) {

      if (Objects.equals(userList.get(i).getUserID(), myUserID)) {
        userColor = Color.RED;
        userStyle = Font.BOLD;
      } else {
        userColor = Color.BLACK;
        userStyle = Font.PLAIN;
      }

      if (i<3) {
        rankStyle = Font.BOLD;
        if (i==0) rankColor = new Color(211,169,8);
        if (i==1) rankColor = new Color(132,157,170);
        if (i==2) rankColor = new Color(188,112,53);
      } else {
        rankColor = Color.BLACK;
        rankStyle = Font.PLAIN;
      }

      rankModel.addElement(new Rank(getOrdinalNumber(i+1),
              userList.get(i).getUserID(), userList.get(i).getRate(),
              rankColor, rankStyle, userColor, userStyle));
    }

    LobbyScreen.rankingButton.setEnabled(true);
    changeScreen("Ranking");
  }

  public void getRankFailure() {
    System.out.println("[BoundaryController] getRankFailure() {}");
    showMessage("ランキングの取得に失敗しました．");
    LobbyScreen.rankingButton.setEnabled(true);
  }

  /**
   * マッチング開始
   */

  public void startMatchingRequest() {
    System.out.println("[BoundaryController] startMatchingRequest() {}");
    try {
      ServerConnector.getInstance().startMatching(myUserID);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  public void startMatchingSuccess() {
    System.out.println("[BoundaryController] startMatchingSuccess() {}");

    isWaitMatching = true;

    MatchingScreen.textTimer.restart();
    MatchingScreen.textTimerCounter = 1;
    MatchingScreen.stateLabel.setText("マッチング中です.");
    MatchingScreen.stateLabel.setHorizontalAlignment(JLabel.LEFT);
    MatchingScreen.stateLabel.setBounds(210, 150, 380, 50);
    MatchingScreen.stopMatchingButton.setEnabled(false);
    MatchingScreen.buttonTimer.restart();
    MatchingScreen.playerLabel.setVisible(true);
    MatchingScreen.rateLabel.setVisible(true);

    for (int i=0; i<4; i++) MatchingScreen.gameInfoLabelList.get(i).setVisible(false);

    changeScreen("Matching");
  }

  public void startMatchingFailure() {
    System.out.println("[BoundaryController] startMatchingFailure() {}");
    showMessage("マッチング開始に失敗しました．");
  }

  /**
   * マッチング中断
   */

  public void stopMatchingRequest() {
    System.out.println("[BoundaryController] stopMatchingRequest() {}");
    try {
      ServerConnector.getInstance().stopMatching(myUserID);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  public void stopMatchingSuccess() {
    System.out.println("[BoundaryController] stopMatchingSuccess() {}");
    isWaitMatching = false;
    showMessage("マッチングを中断しました．");
    changeScreen("Lobby");
  }

  public void stopMatchingFailure() {
    System.out.println("[BoundaryController] stopMatchingFailure() {}");
    showMessage("マッチングの中断に失敗しました．");
  }

  /**
   * ゲーム開始
   */

  public void startGame(int roomID, int turn, String[] player, int[] rate) {
    this.isPlayingGame = true;

    System.out.println("[BoundaryController] startGame() {roomID=" + roomID + ", turn=" + turn +
            ", player=" + Arrays.toString(player) + ", rate=" + Arrays.toString(rate) +
            "}");

    isWaitMatching = false;

    MatchingScreen.textTimer.stop();
    MatchingScreen.stateLabel.setText("マッチングしました！");
    MatchingScreen.stateLabel.setHorizontalAlignment(JLabel.CENTER);
    MatchingScreen.stateLabel.setBounds(200, 150, 400, 50);
    MatchingScreen.buttonTimer.stop();
    MatchingScreen.stopMatchingButton.setEnabled(false);
    MatchingScreen.playerLabel.setVisible(false);
    MatchingScreen.rateLabel.setVisible(false);

    playerList = List.of(player);
    rateList = Arrays.stream(rate).boxed().toList();

    //マッチングしたプレイヤーとそのレートを表示
    for (int i=0; i<4; i++) {
      MatchingScreen.gameInfoLabelList.get(i).setText("P" + (i+1) + " " + playerList.get(i) + " : " + rateList.get(i));
      MatchingScreen.gameInfoLabelList.get(i).setVisible(true);
    }

    //5秒間待機
    try {
      Thread.sleep(5000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }

    this.myRoomID = roomID;
    this.myTurn = turn - 1;

    roomTurn = 0; //部屋のターンの初期化
    logModel.clear(); //ログの初期化

    for (int i=0; i<4; i++) GameScreen.playerLabelList.get(i).setText("P" + (i+1) + " " + playerList.get(i)); //プレイヤー名の設定

    if (myTurn == 0) setMyTurn();
    else setOtherTurn();

    changeScreen("Game");

  }

  /**
   * 数字を入力
   */

  public void inputNumber(int[] answer) {
    System.out.println("[BoundaryController] inputNumber() {answer1=" + Arrays.toString(answer) + "}");
    try {
      ServerConnector.getInstance().inputNumber(myRoomID, answer);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  public void inputNumberFailure() {
    System.out.println("[BoundaryController] inputNumberFailure() {}");
    showMessage("入力された数字の送信に失敗しました．");
    showMessage("ゲームを終了し，ロビー画面に戻ります．");
    changeScreen("Lobby");
  }

  /**
   * ターン交代
   */

  public void changeTurn(int hit, int blow, int[] answer, boolean isTimeUp) {

    System.out.println("[BoundaryController] changeTurn() {hit=" + hit +",blow=" + blow +",answer" + Arrays.toString(answer) + ", isTimeUp=" + isTimeUp +"}");

    roomTurn++;

    if (roomTurn % 4 == myTurn) {
      setMyTurn();
    } else {
      setOtherTurn();
    }

    if (!isTimeUp) {
      logModel.add(0, new Answer(hit, blow, answer[0], answer[1], answer[2], answer[3], (roomTurn - 1) % 4));
    } else {
      for (int i=0; i<4; i++) GameScreen.answerLabelList.get(i).setText(null);
      GameScreen.myAnswer.clear();
      GameScreen.numCounter = 0;
    }

  }

  public void exitGame(){
    System.out.println("[BoundaryController] exitGame() {}");
    try {
      ServerConnector.getInstance().exitGame(myUserID, myRoomID);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * ゲーム終了
   */

  public void endGame(String winner, int[] answer, int[] afterRates, boolean is8TurnSkipped) {
    System.out.println("[BoundaryController] endGame() {winner=" + winner +
            ", answer=" + Arrays.toString(answer) + ", afterRates=" + Arrays.toString(afterRates) +" ,is8TurnSkipped="+is8TurnSkipped+"}");
    this.isPlayingGame = false;

    List<Integer> answerNumber = Arrays.stream(answer).boxed().toList();
    List<Integer> beforeRate = rateList;
    List<Integer> afterRate = Arrays.stream(afterRates).boxed().toList();

    if (!is8TurnSkipped) {
      for (int i=0; i<12; i++) GameScreen.buttonList.get(i).setEnabled(false);

      changeScreen("Result");

      ResultScreen.winnerLabel.setText("Winner is " + winner + " !");

      for (int i=0; i<4; i++) {
        ResultScreen.answerLabelList.get(i).setText(String.valueOf(answerNumber.get(i)));
        ResultScreen.resultLabelList.get(i).setText("P" + (i+1) + " " + playerList.get(i) + " : " + beforeRate.get(i) + " → " + afterRate.get(i));

        int deltaRate = afterRate.get(i) - beforeRate.get(i);

        if (deltaRate > 0) {
          ResultScreen.deltaRateLabelList.get(i).setText("+" + deltaRate);
          ResultScreen.deltaRateLabelList.get(i).setForeground(new Color(255, 90, 0));
        } else {
          ResultScreen.deltaRateLabelList.get(i).setText("-" + Math.abs(deltaRate));
          ResultScreen.deltaRateLabelList.get(i).setForeground(new Color(0, 90, 255));
        }
      }

      //レートの上書き処理
      myRate = afterRate.get(myTurn);
      LobbyScreen.rateLabel.setText("現在のレート : " + myRate);
      MatchingScreen.rateLabel.setText("現在のレート : " + myRate);

    }else{
      showMessage("8ターン連続でスキップされたため，ゲームを終了します．");
      changeScreen("Lobby");
    }

    logModel.clear();
  }

}
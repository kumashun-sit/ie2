package controller;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import connector.ServerConnector;
import connector.dto.*;
import connector.enumerate.ProcessNumber;
import entity.Room;

//deleteRoom()はなし　ゲームが動いている限りルームのリストが増えていく
public class GameController extends Thread{
    static int i=-1;  //ルームID初期値
    static List<Room> roomList = new ArrayList<Room>();
    static List<String> playingUserList = new ArrayList<String>();  //プレイ中のユーザリスト

    //get
    //ゲーム開始時にIDとレートを得る  [ロビーサーバーから]  "引数 (4人のユーザーIDが入ったString[4])"
    //★ロビーサーバからゲーム開始時にユーザ4人のIDが入ったStringを受け取る
    public static void createRoom (String[] userID)  {
        System.out.println("[GameController] createRoom() {users=" + Arrays.toString(userID) + "}");
        int[] rate = new int [4];
        for(int j=0;j<4;j++) {
            try {
                rate[j] = ServerConnector.getInstance().getRate(userID[j]);  //レートの取得
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            playingUserList.add(userID[j]);  //プレイ中のユーザ
        }
        i++;//ルームの数0から　リスト番号として使用
        roomList.add(new Room());  //ルームの作成(arraylistにより作成)
        roomList.get(i).runRoom(userID,rate,i);
    }

    //★回答が入力された場合に計算をする  [クライアントから]  "引数 (回答 int[4],  ルームID int)"
    public static void setAnswer(int[] userAnswer, int roomID) {
        System.out.println("[GameController] setAnswer() userAnswer1="+ Arrays.toString(userAnswer)+",roomID="+roomID+"}");
        roomList.get(roomID).setUserAnswer(userAnswer);
    }

//    ユーザがいなくなった場合にそのIDをプレイ中IDリストから削除
    public static void setOnCloseUser(String userID){
        if(playingUserList.contains(userID)) {
            playingUserList.remove(playingUserList.indexOf(userID));
        }
    }


    //send

    //ゲーム開始
    public void sendGameInfo(String[] userID,int[] rate,int roomID) {
        System.out.println("[GameController] sendGameInfo() " +
                        "userID[0]="+userID[0]+ ",userID[1]="+userID[1]+",userID[2]="+userID[2]+",userID[3]="+userID[3]+",roomID="+roomID+
                        ",rate[0]="+rate[0]+",rate[1]="+rate[1]+",rate[2]="+rate[2]+",rate[3]="+rate[3]+ "}");

          for(int i=0;i<4;i++) {  //ゲームスタートのDTO
              if (playingUserList.contains(userID[i])) {
                  try {
                      ServerConnector.getInstance().sendDataToClient(userID[i], new DtoGameInfo(ProcessNumber.START_GAME, roomID, i + 1, userID, rate));
                  } catch (IllegalArgumentException e){
                      e.printStackTrace();
                  }
              }
          }
    }


    //ターンが変わる処理 (入力された場合)   (ヒットの数,ブローの数,プレイヤーのID,前プレイヤーの回答内容,ルームID)
    //ルームが動いているうちは最初のプレイヤーの回答内容からすべてクライアント画面側で保持しておいてもらう？
    //★クライアントへ(回答画面へ)　次の回答者String userID[]:intにてヒットとブロー,前のプレイヤーの回答内容のint[],ルームID
    //★クライアントへ(待機画面へ)　次の回答者以外の3人のString userID[]：intにてヒットとブロー,前のプレイヤーの回答内容のint[]とルームID
    public void sendUserAnswer(int hit, int blow, String[] userID,int[] theOneBeforeUserAnswer,int roomID) {
        System.out.println("[GameController] sendUserAnswer() hit="+hit+",blow="+blow+",userID[0]="
                +userID[0]+",userID[1]="+userID[1]+",userID[2]="+userID[2]+",userID[3]="+userID[3]+
                ",theOneBeforeUserAnswer[0]="+theOneBeforeUserAnswer[0]+",theOneBeforeAnswer[1]="
                +theOneBeforeUserAnswer[1]+",theOneBeforeAnswer[2]="+theOneBeforeUserAnswer[2]+
                ",theOneBeforeAnswer[3]="+theOneBeforeUserAnswer[3]+",roomID="+roomID+"}");
        boolean timeUp = false;

        for(int i=0;i<4;i++) { //DTO
            if (playingUserList.contains(userID[i])) {
                try {
                    ServerConnector.getInstance().sendDataToClient(userID[i], new DtoChangeTurn(ProcessNumber.CHANGE_TURN, hit, blow, theOneBeforeUserAnswer, timeUp));
                } catch (IllegalArgumentException e){
                    e.printStackTrace();
                }
            }
        }

    }

    //ターンが変わる処理  (回答なし)   (プレイヤーのID,ルームID)
    //★クライアントへ　ルームの一番最初のターンと何も回答が入力されなかったターンに
    //★次の回答者(回答画面)にString userID[],ルームID　それ以外のプレイヤー(待機画面)にString userID[],ルームIDを送る
    public void sendChangeTurn(String[] userID,int roomID) {
        System.out.println("[GameController] sendChangeTurn() userID[0]="+userID[0]+ ",userID[1]="
                +userID[1]+",userID[2]="+userID[2]+",userID[3]="+userID[3]+",roomID="+roomID+"}");
        boolean timeUp = true;

        for(int i=0;i<4;i++) {  //DTO
            if (playingUserList.contains(userID[i])) {
                try{
                    ServerConnector.getInstance().sendDataToClient(userID[i], new DtoChangeTurn(ProcessNumber.CHANGE_TURN,99, 99, new int[]{99, 99, 99, 99}, timeUp));
                }catch (IllegalArgumentException e){
                    e.printStackTrace();
                }
            }
        }

    }

    //ゲームセットの場合  (通常)  (勝者のID,プレイヤーのID,正解,ゲーム後のレート,ルームID)
    //★クライアントに　プレイヤー全員へ　勝者のString ID,int[]の正解の数字,全員のゲーム終了後のレートint[],int ルームID
    public void sendGameSet(String winnerID,String[] userID,int[] roomAnswer,int[] afterRate,int roomID) throws SQLException {
        System.out.println("[GameController] sendGameSet() {winnerID="+winnerID+",userID[0]="
                + Arrays.toString(userID) +", roomAnswer" + Arrays.toString(roomAnswer) +",afterRate=" + Arrays.toString(afterRate) + ",roomID="+roomID+"}");
        for (int k=0;k<4;k++) {
            sendRate(userID[k],afterRate[k]);
        }
        for(int i=0;i<4;i++) { //DTO
            if (playingUserList.contains(userID[i])) {
                try{
                    ServerConnector.getInstance().sendDataToClient(userID[i], new DtoEndGame(ProcessNumber.END_GAME,
                            winnerID, roomAnswer, afterRate, false));
                    playingUserList.remove(userID[i]);
                }catch (IllegalArgumentException e){
                    e.printStackTrace();
                }
            }
            }
    }

    //ゲームセット(全員いなくなった場合)  (プレイヤーのID,ルームID)
    //★クライアントに　全員のString userID[]、int　ルームID
    public void sendGamesetException(String[] userID,int roomID) {
        System.out.println("[GameController] sendGamesetException() {userID[0]="+userID[0]+
                ",userID[1]="+userID[1]+",userID[2]="+userID[2]+",userID[3]="+userID[3]+",roomID="+roomID+"}");
        for(int i=0;i<4;i++) {  //DTO
            if ( playingUserList.contains(userID[i])) {
                try{
                    ServerConnector.getInstance().sendDataToClient(userID[i], new DtoEndGame(ProcessNumber.END_GAME,"aa", new int[]{99, 99, 99, 99}, new int[]{99, 99, 99, 99}, true));
                    playingUserList.remove(userID[i]);
                }catch (IllegalArgumentException e){
                    e.printStackTrace();
                }
            }
        }
    }

    //DBにゲーム後のレートを送信  (プレイヤーのID,ルームID)
    //★関数を呼び出したとき、全員分のString userID[],int ゲーム終了後のレート
    public static void sendRate (String userID, int afterRate) {
        System.out.println("[GameController] sendRate() {userID="+userID+",afterRate="+afterRate+"}");
        try {
            ServerConnector.getInstance().updateRate(userID,afterRate);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}

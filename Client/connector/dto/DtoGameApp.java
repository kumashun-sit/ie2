package connector.dto;


/*　(仮)
 * 引数や返り値に配列があるとき、参照渡しはこの場合うまくいく？？
 * 複数のデータをリターンするとき、メソッドを分けるか、まとめてStringかオブジェクトクラスの配列にしてあとから分解するか
 *　↑いちおうオブジェクトクラスの配列を採用した
 * プロセスナンバーはこのファイルの最後のコメントアウト
 */

import connector.enumerate.ProcessNumber;

public class DtoGameApp extends Dto {
    private int roomID;
    private int[] userAnswer = new int[4];//ターン中のプレイヤーが入力した値
    //private int[] userRate = new int[4];
    private int userRate1;
    private int userRate2;
    private int userRate3;
    private int userRate4;
    private String[] makeRoomID = new String[4];
    //private String[] userID = new String[4];
    private String userID1;
    private String userID2;
    private String userID3;
    private String userID4;
    //private String[] waitPlayers = new String[3];
    private int hit = 0;
    private int blow = 0;
    private int beforeAnswer=0;
    private String nextPlayerID;
    private String waitPlayer1;
    private String waitPlayer2;
    private String waitPlayer3;
    private String winnerID;
    private int resultAnswer;

    public DtoGameApp(ProcessNumber processNumber) {
        super(processNumber);
    }

    //★ルームをつくるためのプレイヤーID
    public void getPlayerID(String ID[]) {
        this.makeRoomID[0] = ID[0];
        this.makeRoomID[1] = ID[1];
        this.makeRoomID[2] = ID[2];
        this.makeRoomID[3] = ID[3];
    }

    //★回答が入力された場合にクライアントから受け取り計算する  "引数 (回答 int[4],  ルームID int)" RECEIVE_ANSWER
    public void getClientAnswer(int userAnswer[], int roomID) {
        this.userAnswer[0] = userAnswer[0];
        this.userAnswer[1] = userAnswer[1];
        this.userAnswer[2] = userAnswer[2];
        this.userAnswer[3] = userAnswer[3];
        this.roomID = roomID;
    }


    //★クライアントへ(回答画面へ)　次の回答者String userID[]:intにてヒットとブロー,
    //前のプレイヤーの回答内容のint[],ルームID TO_ANSWERSCREEN
    public Object[] setNextPlayer(String nextPlayer,int hit,int blow,int beforeAnswer,int roomID) {
        this.nextPlayerID = nextPlayer;
        this.hit=hit;
        this.blow=blow;
        this.beforeAnswer=beforeAnswer;
        this.roomID=roomID;
        Object[] toNextPlayer = {this.nextPlayerID, this.hit, this.blow, this.beforeAnswer,this.roomID};
        return toNextPlayer;
    }

    //★クライアントへ(待機画面へ)　次の回答者以外の3人のString userID[]：intにてヒットとブロー,
    // 前のプレイヤーの回答内容のint[]とルームID TO_WAITSCREEN
    public Object[] setWaitingPlayer(String waitPlayer1,String waitPlayer2,String waitPlayer3
            ,int hit,int blow,int beforeAnswer,int roomID) {
        this.waitPlayer1=waitPlayer1;
        this.waitPlayer2=waitPlayer2;
        this.waitPlayer3=waitPlayer3;
        this.hit=hit;
        this.blow=blow;
        this.roomID=roomID;
        Object[] toWaitPlayer = {this.waitPlayer1,this.waitPlayer2,this.waitPlayer3,this.hit,this.blow,this.roomID};
        return toWaitPlayer;
    }

    //★クライアントへ　ルームの一番最初のターンと何も回答が入力されなかったターンに TO_EXSCREEN
    //次の回答者(回答画面)にString userID[],ルームID　それ以外のプレイヤー(待機画面)にString userID[],ルームIDを送る
    public Object[] setNextPlayerEX(String nextPlayer,int roomID) {
        this.nextPlayerID=nextPlayer;
        this.roomID=roomID;
        Object[] toNextPlayerEX = {this.nextPlayerID,this.roomID};
        return toNextPlayerEX;
    }

    public Object[] setWaitPlayerEX(String waitPlayer1,String waitPlayer2,String waitPlayer3,int roomID){
        this.waitPlayer1=waitPlayer1;
        this.waitPlayer2=waitPlayer2;
        this.waitPlayer3=waitPlayer3;
        this.roomID=roomID;
        Object[] toWaitPlayerEX = {this.waitPlayer1,this.waitPlayer2,this.waitPlayer3,this.roomID};
        return toWaitPlayerEX;
    }

    //★クライアントに　プレイヤー全員へ勝者のString ID,int[]の正解の数字,全員のゲーム終了後のレートint[],int ルームID END_GAME
    public Object[] setResult(String winnerID,int answer,int rate1,int rate2,int rate3,int rate4,int roomID) {
        this.winnerID=winnerID;
        this.resultAnswer=answer;
        this.userRate1=rate1;
        this.userRate2=rate2;
        this.userRate3=rate3;
        this.userRate4=rate4;
        this.roomID=roomID;
        Object[] toAllPlayer = {this.winnerID,this.resultAnswer,this.userRate1,this.userRate2,this.userRate3
                ,this.userRate4,this.roomID};
        return toAllPlayer;
    }

    //★クライアントに　全員のString userID[]、int　ルームID TO_INFO
    public Object[] setInfo(String userID1,String userID2,String userID3,String userID4,int roomID) {
        this.userID1=userID1;
        this.userID2=userID2;
        this.userID3=userID3;
        this.userID4=userID4;
        this.roomID=roomID;
        Object[] info = {this.userID1,this.userID2,this.userID3,this.userID4,this.roomID};
        return info;
    }
}

/*
＊＊プロセスナンバー設定＊＊
public enum ProcessNumber {
RECEIVE_PLAYERS,
RECEIVE_ANSWER,
RECEIVE_RATE,
TO_ANSWERSCREEN,
TO_WAITSCREEN,
TO_EXSCREEN,
END_GAME,
TO_INFO,
SEND_RATE
}
 */

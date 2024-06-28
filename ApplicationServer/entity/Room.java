package entity;

/*
 * Roomクラス (ゲーム開始から終了までの管理)
 *  メンバー管理/答え作成/レート計算
 *   入1:入力はRoomクラスのlineに4桁の数字を (int userAnswer)  "配列にも変更可能?" ターンごと  ロビー→アプリ☆
 *   入2:メンバーはIDに格納 (String ID[0to4])  ゲームスタート初回のみ                          ロビー→アプリ☆
 *   入3:メンバーのプレイ前のレート (int rateBeforeAllay[0to3]) (仮)  ゲームスタート初回のみ   ロビーorDB?→アプリ☆
 *   出1:答えはこちらで自動生成 (int roomAnswer[0to4])  ゲーム終了時のみ                       アプリ☆→クライアント
 *   出2:ゲームの勝者 (String winnerID)  ゲーム終了時のみ                                      アプリ☆→クライアント
 *   出3:メンバーのプレイ後のレート (int rateAfterAllay[0to3]) (仮)  ゲーム終了時のみ          アプリ☆→クライアントorDB?
 */

//ゲーム開始 and 入力待ち状態維持
public class Room extends Thread{
   // public int continue = 0;
   //public String winnerID;
    public int roomAnswer[] = new int [4]; //答えセット(自動生成)
    public int userAnswer[] = new int [4];
    public int rateAve = 0;
    public int roomID;
    public int rateBeforeArray[] = new int [4];
    public String[] tmpID = new String[4];
    Turn turn;

    public void runRoom (String[] mem,int[] num,int a) {//mem プレイヤー、num レート、a ルームID

        int tmpArray[] = new int [10];  //tmp
        int tmp=0; //tmp
        roomID = a;

        //プレーヤーの設定
        String[] ID = mem;  //テスト用 入2☆☆☆
        for (int i = 0; i<4; i++) {
            tmpID[i] = ID[i];
        }

        //プレーヤーのレート定義
        rateBeforeArray = num;  //テスト用 入3☆☆☆

        //レート平均値計算
        for (int i = 0; i<4; i++) {
            tmp = tmp + rateBeforeArray[i];
        }
        rateAve = tmp / 4;

        //ランダムな正解を作成(ここから)
        for (int i = 0; i < 10 ; i ++) {
            tmpArray[i] = i;
        }
        //配列の中身を並べ替え(シャッフル)
        for (int i = tmpArray.length-1; i > 0; i--) {
            int r = (int) (Math.random() * (i + 1));
            tmp =  tmpArray[i];
            tmpArray[i] =  tmpArray[r];
            tmpArray[r] = tmp;
        }
        //代入
        for (int i = 0; i < 4 ; i++) {
            roomAnswer[i] =  tmpArray[i];
        }
        //答え表示
        System.out.printf("roomID : "+roomID+" answer is ");
        for (int i=0;i<4;i++) {
            System.out.printf(roomAnswer[i]+",");
        }
        System.out.printf("\n\n");

        turn = new Turn();
        turn.setTurnRoomID(ID,num,roomID);
        turn.setTurnRoomAnswer(roomAnswer,rateAve,rateBeforeArray);
        turn.start();
    }

    public void setUserAnswer(int[] num) {
        userAnswer = num;
        turn.setUserAnswer(userAnswer);
        //userAnswer[]に格納されている

        try { //文字入力完了処理
            turn.interrupt();  //sleep停止命令
            Thread.sleep(100);  //処理待ち用
        }catch(InterruptedException e){ //割り込み処理 (基本的にはない?)
        }
    }
}

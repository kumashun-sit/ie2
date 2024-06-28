package entity;

import controller.GameController;

import java.sql.SQLException;

/*
* Turnクラス (ターンの管理)
*  ヒットブロー計算/時間管理/結果判定
*   出4:プレイ中のプレイヤー (String turnIDAllay[0])
*       プレイ中以外のプレイヤー (String turnIDAllay[1to3]) ターン開始ごと        アプリ☆→クライアント
*   出6:入力された数字(int turnUserAnswer[0to4]) ターン終わりごと                アプリ☆→クライアント
*   出7:hitとblowの数 (int hit, int blow) ターン終わりごと                      アプリ☆→クライアント
*/
public class Turn extends Thread{
    int[] turnUserAnswer = new int [4];
    String[] turnIDArray ; // ターン用の配列
    int[] turnRoomAnswer = new int [4];
    public int[] rateBeforeArray = new int [4];
    public int[] rateAfterArray = new int [4];
    public int rateAve;
    public int roomID;
    public String winnerID;
    int flag = 0;

    public void run() {

        int hit=0; //hit格納用
        int blow=0; //blow格納用

        int count=0;

        String tmpID[] = new String [4];  //保存用
        for (int i = 0; i<4; i++) {
            tmpID[i] = turnIDArray[i];
        }

        GameController run = new GameController();

        run.sendGameInfo(turnIDArray,rateBeforeArray,roomID);  //ゲーム開始

        try{
            Thread.sleep(5000);  //マッチング成功画面表示
        }catch(InterruptedException e){
        }

        //ターンループ (30秒経過 or 入力されたらループが回る → ターンを回す)
        while(true) {
            //System.out.println("ID: "+turnIDArray[0]+" さんの番です。→ ");  //出4☆☆☆
            try {  //時間オーバー処理
                count++;
                Thread.sleep(31000);  //待ち時間設定
                System.out.printf("roomID : "+roomID+", userID : "+turnIDArray[0]+", timeOver!!\n\n");
                changeTurn(turnIDArray);//次のターンに回す(ゲーム続行)
                if (count != 8) {
                    run.sendChangeTurn(turnIDArray,roomID);  //時間制限オーバー
                }
                flag = 0;
            }catch(InterruptedException e){  //入力成功時処理
                flag = 1;
            }

            if (flag == 1){
                try { //文字入力完了処理
                    Thread.sleep(100);  //処理待ち用
                }catch(InterruptedException e){ //割り込み処理 (基本的にはない?)
                }
                count = 0;

                hit = getHitNumber( turnUserAnswer,turnRoomAnswer);  //ヒット計算
                blow = getBlowNumber( turnUserAnswer,turnRoomAnswer,hit);  //ブロー計算

                System.out.printf("roomID : "+roomID+" ,userID : "+turnIDArray[0]+" ,inputNumber : ");  //出6☆☆☆
                for (int i=0; i<4;i++) {
                    System.out.printf( turnUserAnswer[i]+",");
                }
                System.out.printf("  HIT = "+hit+" BLOW = "+blow+"\n\n");  //出7☆☆☆

                changeTurn(turnIDArray);//次のターンに回す(ゲーム続行)

                if (hit != 4) {
                    run.sendUserAnswer(hit,blow,turnIDArray,turnUserAnswer,roomID);  //入力成功時のターン回し
                }

            }

            if (hit == 4) {  //ゲーム終了
                //レート計算 rateAfter[0to3]に格納 (ここから)
                winnerID = turnIDArray[3];
                for (int i=0; i<4; i++) {
                    if (winnerID.equals(tmpID[i])) {
                        rateAfterArray[i] = calcNewRate(rateBeforeArray[i],rateAve,true);
                    }else {
                        rateAfterArray[i] = calcNewRate(rateBeforeArray[i],rateAve,false);
                    }
                }
                System.out.printf("roomID: "+roomID+" endGame!!\n");
                for (int i=0; i<4; i++) {
                    System.out.printf("ID: "+tmpID[i]+", rate "+rateBeforeArray[i]+" → "+rateAfterArray[i]+" \n");  //ゲーム終了後レート出力  出3☆☆☆
                }
                System.out.printf( "\n");

                //レート計算 rateAfter[0to3]に格納 (ここまで)
                try {
                    run.sendGameSet(turnIDArray[3], tmpID, turnRoomAnswer, rateAfterArray  , roomID);  //通常のゲームセット
                } catch (SQLException e) {
                    e.printStackTrace();
                }


                break;
            } else if (count == 8) {  //ゲーム終了
                run.sendGamesetException(tmpID,roomID);  //全員いなくなった場合のゲームセット
                System.out.printf("roomID: "+roomID+" endGame!! (8 turn passed)\n");
                break;
            }

        }
    }

    public void changeTurn (String[] allay) {

        String allayTmp = allay[0];
        for (int i=0;i<3; i++) {
            allay[i]= allay[i+1];
        }
        allay[3]= allayTmp;
    }

    //hitの算出
    public int getHitNumber(int a[], int b[]) {
        int hit=0;
        for (int i = 0; i< 4; i++) {
            if (a[i]==b[i]) {
                hit++;
            }
        }
        return hit;
    }

    //blowの算出
    public int getBlowNumber(int a[], int b[],int c) {
        int blow=0;
        int tmp2[] = {0,0,0,0};

        for (int i = 0; i< 4; i++) {
            tmp2[i] = b[i];
        }


        for (int i = 0; i< 4; i++) {
            if (a[i]==b[i]) {
                tmp2[i] = -1;
            }
        }

        for (int i=0; i<4;i++) {
            for(int j=0;j<4;j++) {
                if (i != j && a[i] ==tmp2[j]) {
                    blow++;
                    tmp2[j] = -1;
                }
            }
        }
        return blow;
    }

    public void setUserAnswer(int[] num) {  //4桁→配列に変換 (今後変更予定)
        this.turnUserAnswer = num;
    }

    public void setTurnRoomID(String[] ID,int[] rate,int num) {
        this.turnIDArray= ID;
        this.rateBeforeArray= rate;
        this.roomID = num;
    }

    public void setTurnRoomAnswer(int[] num,int ave, int[] rate) {
        this.rateBeforeArray = rate;
        this.turnRoomAnswer = num;
        rateAve = ave;
    }

    //レート計算
    public static int calcNewRate(int rate, int ave, boolean isWin) {

        final int baseWinPoint = 200; //勝った時に増える基礎ポイント
        final int baseLosePoint = 50; //負けた時に減る基礎ポイント

        final int rangeDif = 1000; //平均レートと自分のレートの差をいくつまで加味するかを表す

        final double rangeWinDelta = 160; //勝った時のレートの変動値の範囲
        final double rangeLoseDelta = 40; //負けた時のレートの変動値の範囲

        final double winKeisuu = rangeWinDelta / rangeDif; //勝った時の基礎ポイントにかかる係数
        final double loseKeisuu = rangeLoseDelta / rangeDif; //負けた時の基礎ポイントにかかる係数

        int deltaRate; //レートの変動値
        int newRate; //最終的に算出するレート
        double dif = rate - ave; //平均レートと自分のレートの差

        if (Math.abs(dif) > rangeDif) dif = Math.signum(dif) * rangeDif; //rangeDif以上の差は加味しない

        if (isWin) deltaRate = (int) (baseWinPoint + (-dif * winKeisuu)); //勝った時のレートの変動値を計算
        else deltaRate = (int) -(baseLosePoint + (dif * loseKeisuu)); //負けた時のレートの変動値を計算

        newRate = rate + deltaRate;

        if (newRate < 0) newRate = 0; //レートの下限は0
        if (newRate > 9999) newRate = 9999; //レートの上限は9999

        return newRate;

    }

}

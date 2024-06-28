import java.util.Random;

public class RateTest {

    public static void main(String[] args){

        int trialNumber = 50;  //テスト回数

        int[] rate = new int [4];
        int totalRate;
        int ave = 1000;
        int winnerID;
        int tmp = 0;
        int[] winCounter = new int [4];

        for (int i=0; i<4; i++){
            rate[i] = ave;
            winCounter[i] = 0;
        }

        Random rand = new Random();

        for (int i=0; i<trialNumber; i++) {

            totalRate = 0;
            winnerID = rand.nextInt(4);
            //if (winnerID==4) winnerID = 0; //1人だけ強い場合

            for (int j=0; j<4; j++){
                if (winnerID == j){
                    rate[j] = calcNewRate(rate[j], ave, true);
                    winCounter[j]++;
                }else{
                    rate[j] = calcNewRate(rate[j], ave, false);
                }
            }

            for (int j=0; j<4; j++){
                tmp = rate[j] + tmp;
            }

            ave = tmp/4;
            tmp = 0;

            System.out.print((i+1) + "回目 ... ");

            for (int j=0; j<4; j++) {
                System.out.print("user" + j + ":" + rate[j] + ", ");
                totalRate += rate[j];
            }

            System.out.println("totalRate:" + totalRate);

        }

        System.out.println("-----------------------------------------");
        System.out.println(trialNumber + "回プレイした後のレートは");

        totalRate = 0;

        for (int i=0; i<4; i++) {
            System.out.println("ID:user" + i + ", rate:" + rate[i] + ", 勝利数:" + winCounter[i]);
            totalRate += rate[i];
        }

        System.out.println("totalRate:" + totalRate + "\n");
        
    }

    public static int calcNewRate(int rate, int ave, boolean isWin) {

        final int baseWinPoint = 200; //勝った時に増える基礎ポイント 要求仕様書に従うと40
        final int baseLosePoint = 50; //負けた時に減る基礎ポイント 要求仕様書に従うと12

        final int rangeDif = 1000; //平均レートと自分のレートの差をいくつまで加味するかを表す 要求仕様書に従うと31

        final double rangeWinDelta = 160; //勝った時のレートの変動値の範囲 要求仕様書に従うと20
        final double rangeLoseDelta = 40; //負けた時のレートの変動値の範囲 要求仕様書に従うと7

        final double winKeisuu = rangeWinDelta / rangeDif; //勝った時の基礎ポイントにかかる係数 要求仕様書に従うと20/31=0.645...
        final double loseKeisuu = rangeLoseDelta / rangeDif; //負けた時の基礎ポイントにかかる係数 要求仕様書に従うと7/31=0.226...

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

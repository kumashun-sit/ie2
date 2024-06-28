/**
 * Userデータクラス
 */

package connector.dto;

public class User {
    private String ID;
    private String Password;
    private int Rate;

    /**
     * Userコンストラクタ
     * @param id; ユーザID
     * @param password; パスワード
     * @param rate; レート
     */
    public User(String id, String password, int rate) {
        /* 引数をフィールドに格納 */
        this.ID = id;
        this.Password = password;
        this.Rate = rate;
    }

    /**
     * UserID格納メソッド
     * @param userID; ユーザID
     */
    public void setUserID(String userID) {
        if(userID == null) throw new IllegalArgumentException("userID must not be null");
        this.ID = userID;
    }

    /**
     * UserPassword格納メソッド
     * @param password; パスワード
     */
    public void setPassword(String password) {
        if(password == null) throw new IllegalArgumentException("password must not be null");
        this.Password = password;
    }

    /**
     * UserRate格納メソッド
     * @param rate; レート
     */
    public void setRate(int rate) {
        this.Rate = rate;
    }

    /**
     * UserIDを返すメソッド
     */
    public String getUserID() {
        return this.ID;
    }

    /**
     * UserPasswordを返すメソッド
     */
    public String getPassword() {
        return this.Password;
    }

    /**
     * UserRateを返すメソッド
     */
    public int getRate() {
        return this.Rate;
    }

    @Override
    public String toString() {
        return "User{" +
                "ID='" + ID + '\'' +
                ", Password='" + Password + '\'' +
                ", Rate=" + Rate +
                '}';
    }
}

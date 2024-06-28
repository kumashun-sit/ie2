package connector.manager;

import java.sql.*;

public class DatabaseAccessManager {
    private static final String sqlDriverName = "com.mysql.jdbc.Driver";

    // SQLサーバの指定
    private static final String url = "jdbc:mysql://sql.yamazaki.se.shibaura-it.ac.jp";
    private static final String sqlServerPort = "13308";

    private static final String sqlDatabaseName = "db_group_c";
    private static final String sqlUserId   = "group_c";
    private static final String sqlPassword = "group_c";
    private static final String target = url + ":" + sqlServerPort + "/" + sqlDatabaseName;


    public DatabaseAccessManager() throws ClassNotFoundException {
        Class.forName(sqlDriverName);
    }
    /**
     * ユーザの既存レートを送信する
     * (備考) ログインでidの存在を前提にしているため、idがない場合の処理は考えていない
     * @param id:対象のユーザーID
     * @return 対象ユーザの既存レート
     */
    public int getRate(String id) throws SQLException{
        System.out.println("[DatabaseAccessManager] getRate() {userId=" + id + "}");
        int rate = 0;
        // 対象ユーザのレートを取得するsql文の作成
        String query = "SELECT * FROM User WHERE ID = '" + id + "';";
        // 接続先情報と"MySQLへログインするための"ユーザIDとパスワードから接続を行う
        Connection con = DriverManager.getConnection(this.target, this.sqlUserId, this.sqlPassword);
        // MySQLに問い合わせるためのStatementオブジェクトを構築する
        Statement stmt = con.createStatement();
        // Statementオブジェクトとクエリメッセージを使い，実際に問い合わせて結果を得る
        ResultSet rs = stmt.executeQuery(query);

        while(rs.next()){
            rate = rs.getInt("Rate");
        }
        stmt.close();
        con.close();

        // レートの取得成功
        System.out.println("Getting Rate Successful!");
        return rate;
    }

    /**
     * ユーザのレートを更新する
     * (備考) ログインでidの存在を前提にしているため、idがない場合の処理は考えていない
     * @param id:対象のユーザID
     * @param rate:対象ユーザの更新後レート
     */
    public void setRate(String id, int rate) throws SQLException{
        System.out.println("[DatabaseAccessManager] setRate() {id=" + id + ", rate=" + rate+ "}");
        // 対象ユーザのレートを更新するsql文の作成
        String query = "UPDATE User SET Rate = " + rate + " WHERE ID = '" + id + "'";
        // 接続先情報と"MySQLへログインするための"ユーザIDとパスワードから接続を行う
        Connection con = DriverManager.getConnection(this.target, this.sqlUserId, this.sqlPassword);
        // MySQLに問い合わせるためのStatementオブジェクトを構築する
        Statement stmt = con.createStatement();
        // MySQLに問い合わせ
        int num = stmt.executeUpdate(query);
        stmt.close();
        con.close();

        // レートの更新成功
        System.out.println(num + "レコード更新しました");
        System.out.println("Rate Updated!");
    }

    @Override
    public String toString() {
        return "DatabaseAccessManager{" +
                "sqlDriverName='" + sqlDriverName + '\'' +
                ", url='" + url + '\'' +
                ", sqlServerPort='" + sqlServerPort + '\'' +
                ", sqlDatabaseName='" + sqlDatabaseName + '\'' +
                ", sqlUserId='" + sqlUserId + '\'' +
                ", sqlPassword='" + sqlPassword + '\'' +
                '}';
    }
    /*
    public static void main(String[] args){
        try {
            DatabaseAccessManager dbam = new DatabaseAccessManager();
            dbam.setRate("testid", 1250);
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }
     */
}

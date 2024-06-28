package connector.manager;

import connector.dto.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

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
     * データベースにアカウントを追加する
     * (備考) IDの重複についてはテーブルにUniqueの制約を作ったため考える必要はない
     * @param user: データ受け渡しオブジェクト
     * @throws SQLException:
     */
    public void createAccount(User user) throws SQLException{
        System.out.println("[DatabaseAccessManager] createAccount() {user=" + user + "}");
        // アカウント追加のsql文を作成
        String query = "INSERT INTO User (ID, Password, Rate) VALUES ('" + user.getUserID() + "', '" + user.getPassword() + "', 1000);";
        // 接続先情報と"MySQLへログインするための"ユーザIDとパスワードから接続を行う
        Connection con = DriverManager.getConnection(this.target, this.sqlUserId, this.sqlPassword);
        // MySQLに問い合わせるためのStatementオブジェクトを構築する
        Statement stmt = con.createStatement();
        // Statementオブジェクトとクエリメッセージを使い，実際に問い合わせて結果(更新頻度)を得る
        int num = stmt.executeUpdate(query);
        stmt.close();
        con.close();

        // アカウント新規作成の成功
        System.out.println(num+"レコード更新しました");
        System.out.println("Account Created!");
    }

    /**
     * 対象ユーザのログイン処理
     * @param user: 対象ユーザ
     * @return user: レートを含めた対象ユーザ
     * @throws SQLException:
     */
    public User login(User user) throws SQLException{
        System.out.println("[DatabaseAccessManager] login() {user=" + user + "}");
        if(toBoolean(userCheck(user))) {
            // 対象ユーザのデータを取得
            String query = "SELECT * FROM User WHERE ID = '" + user.getUserID() + "' AND Password = '" + user.getPassword() + "';";
            // 接続先情報と"MySQLへログインするための"ユーザIDとパスワードから接続を行う
            Connection con = DriverManager.getConnection(this.target, this.sqlUserId, this.sqlPassword);
            // MySQLに問い合わせるためのStatementオブジェクトを構築する
            Statement stmt = con.createStatement();
            // MySQLに問い合わせ
            ResultSet rs = stmt.executeQuery(query);
            // 対象データのレートレコード取得
            while (rs.next()) {
                user.setRate(rs.getInt("Rate"));
            }
            stmt.close();
            con.close();

            //ログイン成功
            System.out.println("Login Successful!");
        }
        else {System.out.println("Login Failure!");}
        return user;
    }

    /**
     * ユーザのレートランキングをリストにして返す
     * @return rank:レート順に並べたユーザリスト
     * @throws SQLException:
     */
    public List<User> getRank() throws SQLException{
        List<User> rank = new ArrayList<User>();
        // 対象ユーザのデータを取得
        String query = "SELECT * FROM User ORDER BY Rate DESC LIMIT 100;";
        // 接続先情報と"MySQLへログインするための"ユーザIDとパスワードから接続を行う
        Connection con = DriverManager.getConnection(this.target, this.sqlUserId, this.sqlPassword);
        // MySQLに問い合わせるためのStatementオブジェクトを構築する
        Statement stmt = con.createStatement();
        // Mysqlに問い合わせ
        ResultSet rs = stmt.executeQuery(query);

        while(rs.next()){
            User user = new User("","",-1);
            user.setUserID(rs.getString("ID"));
            user.setRate(rs.getInt("Rate"));
            rank.add(user);
        }
        return rank;
    }
    /**
     * ユーザの存在を確認する
     * @param user: 対象ユーザ
     * @return flag: 存在すれば 1を、存在しなければ 0を返す
     * @throws SQLException:
     */
    public int userCheck(User user) throws SQLException{
        System.out.println("[DatabaseAccessManager] userCheck() {user=" + user + "}");
        // データが存在するかのsql文
        String query = "SELECT EXISTS(SELECT * FROM User WHERE ID = '"+user.getUserID()+"' AND Password = '"+ user.getPassword() +"')as user_check;";
        // 接続先情報と"MySQLへログインするための"ユーザIDとパスワードから接続を行う
        Connection con = DriverManager.getConnection(this.target, this.sqlUserId, this.sqlPassword);
        // MySQLに問い合わせるためのStatementオブジェクトを構築する
        Statement stmt = con.createStatement();
        // Mysqlに問い合わせ
        ResultSet rs = stmt.executeQuery(query);

        int flag = 0;
        while(rs.next()){
            flag = rs.getInt("user_check");
        }
        stmt.close();
        con.close();

        return flag;
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

    public Boolean toBoolean(int i){
        if(i == 0) return false;
        return true;
    }
/*
    public static void main(String[] args){
        try {
            DatabaseAccessManager dbam = new DatabaseAccessManager();
            List<User> rank = dbam.getRank();
            for(int i = 0; i < rank.size(); i++){
                System.out.println(rank.get(i).getUserID());
            }
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }
*/
}

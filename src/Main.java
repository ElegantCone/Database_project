import interfaces.LoginWindow;
import java.sql.*;

public class Main {
    public static void main(String[] args) throws SQLException {

        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        LoginWindow Logwindow = new LoginWindow();
        /*

        String create = "create table TEST1 (id number(10) NOT NULL, city varchar2(50))";
        droptable("TEST1", conn);
        createTable(create, conn);
        filltable("TEST1", conn);
        MainWindow window = new MainWindow(conn);
        String sql = "select count(*) from TEST1";
        PreparedStatement preStatement = conn.prepareStatement(sql);
        ResultSet result = preStatement.executeQuery();
        while (result.next()) {

            int count = result.getInt(1);
            System.out.println("Total number of records in TEST table: " + count);
        }
        System.out.println("!!!");*/
    }


    static void createTable(String sql, Connection conn) {
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = conn.prepareStatement(sql);
            ResultSet result = preparedStatement.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Table already created");
        }
    }

    static void filltable(String tableName, Connection conn) {
        try {
            char curCity = 'A';
            int id = 1;
            for (int i = 0; i < 15; i++) {
                String sql = "insert into TEST1 (id, city) values (" + id + ", \'" + curCity + "\')";
                id++;
                curCity++;
                PreparedStatement preparedStatement = conn.prepareStatement(sql);
                ResultSet result = preparedStatement.executeQuery();
                //suk
            }
        } catch (SQLException e){
            System.out.println("Table already filled up");
        }
    }

    static void droptable(String table, Connection conn) {
        try {
            String sql = "drop table \"" + table + "\"";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            ResultSet result = preparedStatement.executeQuery();
        } catch (SQLException e){
            System.out.println("Table doesn't exist");
        }
    }
}

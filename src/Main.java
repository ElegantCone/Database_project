import View.LoginWindow;
import java.sql.*;

public class Main {
    public static void main(String[] args) {

        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        LoginWindow Logwindow = new LoginWindow();
    }
}

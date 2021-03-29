package controllers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Locale;
import java.util.Properties;
import java.util.TimeZone;

public class Server {
    Connection conn;

    public Server(String url, Properties props) throws SQLException {
        TimeZone timeZone = TimeZone.getTimeZone("GMT+7");
        TimeZone.setDefault(timeZone);
        Locale.setDefault(Locale.ENGLISH);
        conn = DriverManager.getConnection(url, props);
    }

    public Connection getConn() {
        return conn;
    }

}

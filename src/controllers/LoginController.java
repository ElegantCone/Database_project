package controllers;


import View.LoginWindow;
import View.MainWindow;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

public class LoginController implements ActionListener {
    private LoginWindow window;
    private JTextField login;
    private JTextField pswd;
    private JTextField server;
    private JLabel err;

    String url = "jdbc:oracle:thin:@";
    Connection conn;

    public LoginController(LoginWindow window, JTextField srv, JTextField lgn, JTextField pass, JLabel error){
        server = srv;
        login = lgn;
        pswd = pass;
        err = error;
        this.window = window;
    }

    //установка соединения с oracle
    @Override
    public void actionPerformed(ActionEvent e) {
        err.setText("");
        if (!server.getText().isEmpty())
            url += server.getText() + ":";
        else {
            err.setText("Server is empty");
            return;
        }
        if (login.getText().isEmpty()) {
            err.setText("Login is empty");
            return;
        }
        if (pswd.getText().isEmpty()){
            err.setText("Password is empty");
        }
        Properties props = new Properties();
        props.setProperty("user", login.getText());
        props.setProperty("password", pswd.getText());

        try{
            Server server = new Server(url, props);
            window.delete();
            MainWindow main = new MainWindow(server.getConn());
        } catch (SQLException ex) {
            err.setText("Can't connect to the server. Try later or check data.");
            url = "jdbc:oracle:thin:@";
        }



    }

    public Connection getConn() {
        return conn;
    }
}

package controllers;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DefLoginController implements ActionListener {

    JTextField server;
    JTextField login;
    JTextField pass;

    public DefLoginController(JTextField srv, JTextField lgn, JTextField pass){
        server = srv;
        login = lgn;
        this.pass = pass;
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        server.setText("84.237.50.81:1521");
        login.setText("18207_Saraeva");
        pass.setText("fakepasswordalwaysworks");
    }
}

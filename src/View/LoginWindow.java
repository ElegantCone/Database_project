package View;

import controllers.DefLoginController;
import controllers.LoginController;

import javax.swing.*;
import java.awt.*;

public class LoginWindow extends JFrame {

    private JFrame window;

    public LoginWindow(){
        changeToLogin();
    }

    public void changeToLogin(){
        JButton loginBtn = new JButton("Connect");
        loginBtn.setPreferredSize(new Dimension(355, 20));

        JButton dfltLoginBtn = new JButton("Default settings");
        dfltLoginBtn.setPreferredSize(new Dimension(355, 20));

        JTextField serverFld = new JTextField(32);
        JTextField loginFld = new JTextField(32);
        JTextField pswdFld = new JTextField(32);

        JLabel serverLbl = new JLabel("Server (IP and port): ");
        JLabel loginLbl = new JLabel("Login: ");
        JLabel pswdLbl = new JLabel("Password: ");
        JLabel error = new JLabel("");
        error.setForeground(Color.RED);

        JPanel contents = new JPanel(new FlowLayout(FlowLayout.LEFT));
        contents.add(serverLbl);
        contents.add(serverFld);
        contents.add(loginLbl);
        contents.add(loginFld);
        contents.add(pswdLbl);
        contents.add(pswdFld);
        contents.add(loginBtn);
        contents.add(dfltLoginBtn);
        contents.add(error);

        window = new JFrame();
        window.setBounds(450, 100, 380, 400);
        window.setResizable(false);
        window.getContentPane().add(contents);
        window.setVisible(true);
        window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        LoginController lgnCtrl = new LoginController(this, serverFld, loginFld, pswdFld, error);
        loginBtn.addActionListener(lgnCtrl);

        DefLoginController defLoginController = new DefLoginController(serverFld, loginFld, pswdFld);
        dfltLoginBtn.addActionListener(defLoginController);
    }

    public void delete(){
        window.setVisible(false);
        dispose();
    }
}

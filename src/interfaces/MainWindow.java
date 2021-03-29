package interfaces;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MainWindow extends JFrame implements ActionListener {

    private Connection conn;
    private JFrame window;

    public MainWindow(Connection conn){
        this.conn = conn;

        initWindow();

        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.addWindowListener(connClose());
        window.setBounds(250, 50, 800, 600);
        window.setResizable(false);
        window.setVisible(true);
    }

    private WindowAdapter connClose(){
        return new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                try {
                    conn.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        };
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String sql = "select count(*) from TEST1";
        try {
            PreparedStatement preStatement = conn.prepareStatement(sql);
            ResultSet result = preStatement.executeQuery();
            while (result.next()) {

                int count = result.getInt(1);
                JLabel label = (JLabel) window.getContentPane().getComponent(0);
                label.setText("Total number of records in COUNTRIES table: " + count);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            try {
                conn.close();
            } catch (SQLException exc) {
                exc.printStackTrace();
            }
        }
    }

    private void initWindow(){
        JPanel contentLeft = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JPanel contentRight = new JPanel(new FlowLayout(FlowLayout.LEFT));
    }
}

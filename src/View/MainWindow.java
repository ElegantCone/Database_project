package View;

import sql.SQLController;

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
    private SQLController sqlController;
    private JPanel contents = new JPanel(new FlowLayout());

    public MainWindow(Connection conn) throws SQLException {
        this.conn = conn;
        window = new JFrame();
        initWindow();
        sqlController = new SQLController(conn);
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
        window.getContentPane().remove(contents);
        contents = new JPanel(new FlowLayout());

        JButton changeTables = new JButton("Создать/удалить таблицы");
        JButton showTable = new JButton("Вывести таблицы");
        JButton setData = new JButton("Ввести данные в таблицы");
        JButton closeApp = new JButton("Закрыть приложение");

        changeTables.setPreferredSize(new Dimension(200, 30));
        showTable.setPreferredSize(new Dimension(200, 30));
        setData.setPreferredSize(new Dimension(200, 30));
        closeApp.setPreferredSize(new Dimension(200, 30));
        changeTables.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    ChangeTableDialog changeDialog = new ChangeTableDialog("Изменение таблиц", conn);
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        });

        showTable.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                changeToShow();
            }
        });

        setData.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                changeToSetData();
            }
        });

        closeApp.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                window.dispose();
                try {
                    conn.close();
                    System.exit(0);
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        });
        contents.add(showTable);
        contents.add(changeTables);
        contents.add(setData);
        contents.add(closeApp);


        window.getContentPane().add(contents);
        window.invalidate();
        window.validate();
        window.repaint();
    }

    public void changeToShow(){
        window.remove(contents);
        contents = new JPanel(new FlowLayout());
        Box box = Box.createVerticalBox();
        box.setSize(600, 200);
        contents.setSize(600, 200);

        JLabel mainLbl = new JLabel("Показать таблицу");
        JButton showSubs = new JButton("Абоненты");
        JButton showATE = new JButton("АТС");
        JButton backBtn = new JButton("Вернуться назад");

        mainLbl.setSize(new Dimension(200, 30));
        showSubs.setSize(new Dimension(200, 30));
        showATE.setSize(new Dimension(200, 30));
        backBtn.setSize(new Dimension(700, 30));

        backBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                initWindow();
            }
        });

        showSubs.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    ShowTableDialog dialog = new ShowTableDialog("subscribers", sqlController);
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        });

        showATE.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    ShowTableDialog dialog = new ShowTableDialog("ate", sqlController);
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        });

        partsInit(box, mainLbl, showSubs, showATE, backBtn);
    }

    public void changeToSetData(){
        window.remove(contents);
        contents = new JPanel(new FlowLayout());
        Box box = Box.createVerticalBox();
        box.setSize(600, 200);
        contents.setSize(600, 200);

        JLabel mainLbl = new JLabel("Внести данные");
        JButton setDataSubs = new JButton("Абоненты");
        JButton setDataATE = new JButton("АТС");
        JButton backBtn = new JButton("Вернуться назад");

        mainLbl.setSize(new Dimension(200, 30));
        setDataSubs.setSize(new Dimension(200, 30));
        setDataATE.setSize(new Dimension(200, 30));
        backBtn.setSize(new Dimension(700, 30));

        backBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                initWindow();
            }
        });

        setDataSubs.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    SetDataDialog dialog = new SetDataDialog("subscribers", sqlController);
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        });

        setDataATE.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    SetDataDialog dialog = new SetDataDialog("ate", sqlController);
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        });

        partsInit(box, mainLbl, setDataSubs, setDataATE, backBtn);
    }

    private void partsInit(Box box, JLabel mainLbl, JButton setDataSubs, JButton setDataATE, JButton backBtn) {
        box.add(mainLbl);
        box.add(setDataSubs);
        box.add(setDataATE);
        box.add(backBtn);
        contents.add(box, BorderLayout.CENTER);

        window.getContentPane().add(contents);
        window.invalidate();
        window.validate();
        window.repaint();
    }
}

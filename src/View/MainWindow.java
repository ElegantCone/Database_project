package View;

import sql.SQLController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Vector;

public class MainWindow extends JFrame {

    private Connection conn;
    private JFrame window;
    private SQLController sqlController;
    private JPanel contents = new JPanel(new FlowLayout());
    private Vector <JButton> tblBtns = new Vector<>();
    private String[] tblNamesRu = new String[]
            {"ГТС", "АТС", "Типы АТС", "Атрибуты АТС", "Таксофоны", "Запросы на подключение", "Люди", "Абоненты",
                    "Междугородние вызовы", "Телефонные номера", "Внутренняя сеть", "Типы телефонов", "Платежи", "Оповещения", "Привилегии"};
    private String[] tblNamesEng = new String[]
            {"ctn", "ate", "ate_types", "ate_attrs", "payphones", "connection_requests", "people", "subscribers",
                    "intercity_calls", "phone_numbers", "internal_network", "phone_types", "payment_cheque", "notifications", "subs_privileges"};

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

    private void initWindow(){
        window.getContentPane().remove(contents);
        contents = new JPanel(new FlowLayout());

        JButton changeTables = new JButton("Создать/удалить таблицы");
        JButton showTable = new JButton("Вывести таблицы");
        JButton setData = new JButton("Ввести данные в таблицы");
        JButton updateData = new JButton("Изменить данные");
        JButton closeApp = new JButton("Закрыть приложение");

        changeTables.setPreferredSize(new Dimension(200, 30));
        showTable.setPreferredSize(new Dimension(200, 30));
        setData.setPreferredSize(new Dimension(200, 30));
        updateData.setPreferredSize(new Dimension(200, 30));
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

        updateData.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                changeToUpdateData();
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
        contents.add(updateData);
        contents.add(closeApp);

        window.getContentPane().add(contents);
        window.invalidate();
        window.validate();
        window.repaint();
    }

    public void changeToShow(){
        initWindowWithButtons("show");
    }

    public void changeToSetData(){
        initWindowWithButtons("insert");
    }

    public void changeToUpdateData() {
        initWindowWithButtons("update");
    }

    private void partsInit(Box box, JLabel mainLbl, JButton setDataSubs, JButton setDataATE,JButton setDataCTN, JButton backBtn) {
        box.add(mainLbl);
        box.add(setDataSubs);
        box.add(setDataATE);
        box.add(setDataCTN);
        box.add(backBtn);
        contents.add(box, BorderLayout.CENTER);

        window.getContentPane().add(contents);
        window.invalidate();
        window.validate();
        window.repaint();
    }

    private void initTblBtns (JPanel contents, String type) {
        for (int i = 0; i < tblNamesEng.length; i++){
            JButton btn = new JButton(tblNamesRu[i]);
            int k = i;
            btn.setSize(new Dimension(300, 40));
            switch(type) {
                case "show":
                    btn.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            try {
                               ShowTableDialog dialog = new ShowTableDialog(tblNamesEng[k], sqlController);
                             } catch (SQLException ex) {
                              ex.printStackTrace();
                            }
                         }
                    });
                    break;
                case "insert":
                    btn.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            try {
                                SetDataDialog dialog = new SetDataDialog(tblNamesEng[k], sqlController);
                            } catch (SQLException ex) {
                                ex.printStackTrace();
                            }
                        }
                    });
                    break;
                case "update":
                    btn.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            try {
                                UpdateDataDialog dialog = new UpdateDataDialog(tblNamesEng[k], sqlController);
                            } catch (SQLException ex) {
                                ex.printStackTrace();
                            }
                        }
                    });
                    break;
            }
            tblBtns.add(btn);
            contents.add(btn);
        }
    }

    private JButton initBckBtn() {
        JButton backBtn = new JButton("Вернуться назад");
        backBtn.setPreferredSize(new Dimension(700, 30));
        backBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                initWindow();
            }
        });
        return backBtn;
    }

    private JLabel initMainLbl() {
        JLabel mainLbl = new JLabel("Выберите таблицу");
        mainLbl.setHorizontalAlignment(SwingConstants.CENTER);
        mainLbl.setPreferredSize(new Dimension(400, 40));

        return mainLbl;
    }

    private void initWindowWithButtons (String type) {
        window.remove(contents);
        contents = new JPanel(new BorderLayout());
        JPanel centerBtns = new JPanel(new FlowLayout());

        contents.setSize(600, 200);
        
        initTblBtns(centerBtns, type);
        contents.add(initMainLbl(), BorderLayout.NORTH);
        contents.add(centerBtns, BorderLayout.CENTER);
        contents.add(initBckBtn(), BorderLayout.SOUTH);
        
        window.getContentPane().add(contents);
        window.invalidate();
        window.validate();
        window.repaint();
    }

}

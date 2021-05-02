package View;

import controllers.ChangeTableController;
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
    private Container contents = new Container();
    private Vector <JButton> tblBtns = new Vector<>();
    private String[] tblNamesRu = new String[]
            {"ГТС", "АТС", "Типы АТС", "Атрибуты АТС", "Таксофоны", "Запросы на подключение", "Люди", "Абоненты",
                    "Междугородние вызовы", "Телефонные номера", "Внутренняя сеть", "Типы телефонов", "Платежи", "Оповещения", "Привилегии"};
    private String[] tblNamesEng = new String[]
            {"ctn", "ate", "ate_types", "ate_attrs", "payphones", "connection_requests", "people", "subscribers",
                    "intercity_calls", "phone_numbers", "internal_network", "phone_types", "payment_cheque", "notifications", "subs_privileges"};

    public MainWindow(Connection conn) {
        this.conn = conn;
        setNewWindow();
        initWindow();
        sqlController = new SQLController(conn);
    }

    private void setNewWindow() {
        window = new JFrame();
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
        setNewWindow();
        contents = new JPanel(new BorderLayout());

        JPanel centerBtns = new JPanel(new FlowLayout());

        JButton changeTables = new JButton("Создать/удалить таблицы");
        JButton showTable = new JButton("Вывести таблицы");
        JButton closeApp = new JButton("Закрыть приложение");

        changeTables.setPreferredSize(new Dimension(600, 50));
        showTable.setPreferredSize(new Dimension(600, 50));
        closeApp.setPreferredSize(new Dimension(600, 50));

        changeTables.addActionListener(e -> changeToChange());
        showTable.addActionListener(e -> {
            try {
                changeToShow();
            } catch (SQLException ex) {
                ex.getLocalizedMessage();
            }
        });
        closeApp.addActionListener(e -> {
            window.dispose();
            try {
                conn.close();
                System.exit(0);
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        });

        centerBtns.add(showTable);
        centerBtns.add(changeTables);
        contents.add(centerBtns);
        contents.add(closeApp, BorderLayout.SOUTH);

        window.getContentPane().add(contents);
        window.invalidate();
        window.validate();
        window.repaint();
    }

    public void changeToShow() throws SQLException {
        //initWindowWithButtons("show");
        TableViewWindow tw = new TableViewWindow();
        tw.createGUI(sqlController, window, initBckBtn(), conn);
    }

    public void changeToSetData(){
        initWindowWithButtons("insert");
    }

    public void changeToUpdateData() {
        initWindowWithButtons("update");
    }

    public void changeToChange() { initWindowWithButtons("change");}

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
            btn.setSize(new Dimension(600, 50));
            btn.addActionListener(e -> {
                TableViewWindow tw = new TableViewWindow();
                try {
                    tw.createGUI(sqlController, window, initBckBtn(), conn);
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            });
            tblBtns.add(btn);
            contents.add(btn);
        }
    }

    private JButton initBckBtn() {
        JButton backBtn = new JButton("Вернуться назад");
        backBtn.setPreferredSize(new Dimension(700, 50));
        backBtn.addActionListener(e -> {
            window.dispose();
            initWindow();
        });
        return backBtn;
    }

    private JLabel initMainLbl() {
        JLabel mainLbl = new JLabel("Выберите таблицу");
        mainLbl.setHorizontalAlignment(SwingConstants.CENTER);
        mainLbl.setPreferredSize(new Dimension(600, 50));

        return mainLbl;
    }

    private void initWindowWithButtons (String type) {
        window.remove(contents);
        contents = new JPanel(new BorderLayout());
        JPanel centerBtns = new JPanel(new FlowLayout());

        contents.setSize(600, 200);
        if (type.equals("change")) initChangeBtns(centerBtns);
        else {
            initTblBtns(centerBtns, type);
            contents.add(initMainLbl(), BorderLayout.NORTH);
        }
        contents.add(centerBtns, BorderLayout.CENTER);
        contents.add(initBckBtn(), BorderLayout.SOUTH);
        
        window.getContentPane().add(contents);
        window.invalidate();
        window.validate();
        window.repaint();
    }

    private void initChangeBtns(JPanel centerBtns) {
        JButton createTable = new JButton("Создать таблицы");
        JButton dropTable = new JButton("Удалить таблицы");
        JButton fillTable = new JButton("Заполнить тестовыми данными");
        JLabel errorLbl = new JLabel("");

        createTable.setPreferredSize(new Dimension(600, 50));
        dropTable.setPreferredSize(new Dimension(600, 50));
        fillTable.setPreferredSize(new Dimension(600, 50));
        errorLbl.setPreferredSize(new Dimension(600, 50));

        ChangeTableController controller = new ChangeTableController(new SQLController(conn), errorLbl);

        fillTable.addActionListener(controller.fillTableListener());
        createTable.addActionListener(controller.createTableListener());
        dropTable.addActionListener(controller.dropTableListener());

        centerBtns.add(createTable);
        centerBtns.add(dropTable);
        centerBtns.add(fillTable);
        centerBtns.add(errorLbl);

    }

}

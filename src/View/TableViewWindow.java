package View;

import controllers.ChangeTableController;
import controllers.SetDataController;
import controllers.ShowTableController;
import controllers.UpdateDataController;
import sql.SQLController;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Vector;

public class TableViewWindow extends JFrame {
    ShowTableController showController;
    JTable table;
    JFrame window;
    JLabel errLbl;
    JButton addRowBtn;
    JButton saveChangesBtn;
    JButton backBtn;
    JButton editView;
    SetDataController setController;
    UpdateDataController updateController;
    Vector<JTextField> fields = new Vector<>();
    Vector<JLabel> labels = new Vector<>();
    int[] selectedRows;
    Connection conn;
    private String[] tblNamesRu = new String[]
            {"", "ГТС", "АТС", "Типы АТС", "Атрибуты АТС", "Таксофоны", "Запросы на подключение", "Люди", "Абоненты",
                    "Междугородние вызовы", "Телефонные номера", "Внутренняя сеть", "Типы телефонов", "Платежи", "Оповещения", "Привилегии"};
    private String[] tblNamesEng = new String[]
            {"","ctn", "ate", "ate_types", "ate_attrs", "payphones", "connection_requests", "people", "subscribers",
                    "intercity_calls", "phone_numbers", "internal_network", "phone_types", "payment_cheque", "notifications", "subs_privileges"};
    private JComboBox<String> tablesBox;

    public void createGUI(SQLController sqlcontroller, JFrame window, JButton bckBtn, Connection connection) {
        this.conn = connection;
        this.window = window;
        backBtn = bckBtn;
        this.setController = new SetDataController(sqlcontroller);
        createSelectionBox();
        this.showController = new ShowTableController(sqlcontroller, table);
        this.updateController = new UpdateDataController(sqlcontroller, table);
        setDefaultView();
    }

    private void setDefaultView() {
        table = new JTable();
        errLbl = new JLabel("");
        backBtn.setText("Вернуться");

        Container container = window.getContentPane();
        container.removeAll();
        container.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
        container.setLayout(new GridBagLayout());

        GridBagConstraints constraints = new GridBagConstraints();
        addSelectionBox(container, constraints);
        constraints.fill = GridBagConstraints.BOTH;
        constraints.weightx = 0.5;
        constraints.weighty = 1.0;
        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.gridwidth = 2;

        //добавили таблицу
        container.add(new JScrollPane(table), constraints);

        Vector <JButton> btns = new Vector<>();
        btns.add(backBtn);
        addBtns(container, constraints, btns);

        window.setContentPane(container);
        window.invalidate();
        window.validate();
        window.repaint();
    }

    private void setNonEditView(String tablename) throws SQLException {
        table = showController.showTable(tablename);
        errLbl = new JLabel("");
        editView = new JButton("Режим изменения");
        backBtn.setText("Вернуться");
        table.setBackground(Color.WHITE);

        editView.addActionListener(e -> {
            try {
                setEditView(tablename);
            } catch (SQLException ex) {
                errLbl.setText(ex.getLocalizedMessage());
            }
        });

        Container container = window.getContentPane();
        container.removeAll();
        container.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
        container.setLayout(new GridBagLayout());

        GridBagConstraints constraints = new GridBagConstraints();
        addSelectionBox(container, constraints);

        constraints.fill = GridBagConstraints.BOTH;
        constraints.weightx = 0.5;
        constraints.weighty = 1.0;
        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.gridwidth = 2;

        //добавили таблицу
        container.add(new JScrollPane(table), constraints);
        //добавили кнопки
        Vector <JButton> btns = new Vector<>();
        btns.add(editView);
        btns.add(backBtn);
        addBtns(container, constraints, btns);

        window.setContentPane(container);
        window.invalidate();
        window.validate();
        window.repaint();
    }

    private void setEditView(String tablename) throws SQLException {
        updateController.getFkeys(tablename, conn.getMetaData());
        table = updateController.showTable(tablename);
        table.setBackground(Color.PINK);
        window.getContentPane().removeAll();
        addRowBtn = new JButton("Добавить строку");
        saveChangesBtn = new JButton("Сохранить изменения");
        errLbl = new JLabel("");
        JButton retViewBtn = new JButton("Вернуться в режим просмотра");
        JButton delBtn = new JButton("Удалить выделенную строку");

        addRowBtn.addActionListener(e -> {
            setController.setLabelsFields(labels, fields, errLbl);
            try {
                setController.insertData(tablename);
                errLbl.setText("Данные добавлены в таблицу");
            } catch (SQLException ex) {
                ex.printStackTrace();
                errLbl.setText(ex.getMessage());
            }
        });

        retViewBtn.addActionListener(e -> {
            try {
                setNonEditView(tablename);
            } catch (SQLException ex) {
                errLbl.setText(ex.getLocalizedMessage());
            }
        });

        saveChangesBtn.addActionListener(e -> {
            try {
                updateController.editTable(tablename);
                errLbl.setText("Данные обновлены");
            } catch (SQLException ex) {
                errLbl.setText(ex.getMessage());
                System.out.println(ex.getMessage());
            }
        });

        ListSelectionModel selMod = table.getSelectionModel();
        selMod.addListSelectionListener(e -> selectedRows = table.getSelectedRows());

        delBtn.addActionListener(e -> {
            try{
                updateController.deleteRow(tablename, selectedRows);
                errLbl.setText("Строки удалены");
            } catch (Exception ex) {
                errLbl.setText(ex.getMessage());
                System.out.println(ex.getMessage());
            }
        });


        Container container = window.getContentPane();
        container.removeAll();
        container.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
        container.setLayout(new GridBagLayout());

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.BOTH;
        constraints.weightx = 0.5;
        constraints.weighty = 1.0;
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.gridwidth = 2;

        //добавили таблицу
        container.add(new JScrollPane(table), constraints);
        //добавили поля для ввода данных
        Vector<String> colNames = setController.getColTable(tablename);
        createLabels(colNames);
        createFields();
        addLabelsAndFields(container, constraints);
        //добавили кнопки
        Vector <JButton> btns = new Vector<>();
        btns.add(saveChangesBtn);
        btns.add(addRowBtn);
        btns.add(backBtn);
        btns.add(delBtn);
        btns.add(retViewBtn);
        addBtns(container, constraints, btns);
        window.setContentPane(container);
        window.invalidate();
        window.validate();
        window.repaint();
    }

    private void createSelectionBox () {
        JComboBox<String> comboBox = new JComboBox<>(tblNamesRu);

        comboBox.addItemListener(e -> {
            Vector<String> tblNames = new Vector<>(Arrays.asList(tblNamesRu));
            int index = tblNames.indexOf(e.getItem());
            if (!tblNamesEng[index].equals(""))
                try {
                    setNonEditView(tblNamesEng[index]);
                } catch (SQLException ex) {
                    errLbl.setText(ex.getLocalizedMessage());
                }
        });
        tablesBox = comboBox;
    }

    private void addSelectionBox(Container container, GridBagConstraints constraints) {
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.weightx = 0;
        constraints.weighty = 0;
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.gridwidth = 2;

        container.add(tablesBox, constraints);
    }

    private void addBtns(Container container, GridBagConstraints constraints, Vector<JButton> btns) {
        for (int i = 0; i < btns.size(); i++){
            constraints.fill = GridBagConstraints.HORIZONTAL;
            if (i % 2 == 0) constraints.anchor = GridBagConstraints.LINE_END;
            else
                constraints.anchor = GridBagConstraints.LINE_START;
            constraints.weighty = 0;
            constraints.weightx = 0.5;
            constraints.gridx = i % 2;
            constraints.gridy = labels.size() + 3 + (i / 2);
            constraints.gridwidth = 1;
            container.add(btns.get(i), constraints);
        }
    }

    private void createLabels(Vector<String> colNames){
        for (String name: colNames){
            JLabel label = new JLabel(name);
            label.setPreferredSize(new Dimension(100, 30));
            labels.add(label);
        }
    }

    private void createFields(){
        for (int i = 0; i < labels.size(); i++) {
            JTextField field = new JTextField();
            field.setPreferredSize(new Dimension(200, 30));
            fields.add(field);
        }
    }

    private void addLabelsAndFields(Container container, GridBagConstraints constraints){
        for (int i = 0; i < labels.size(); i++){
            constraints.fill = GridBagConstraints.HORIZONTAL;
            constraints.anchor = GridBagConstraints.LINE_START;
            constraints.weightx = 0.25;
            constraints.weighty = 0;
            constraints.gridx = 0;
            constraints.gridy = 2 + i;
            constraints.gridwidth = 1;
            container.add(labels.get(i), constraints);

            constraints.fill = GridBagConstraints.HORIZONTAL;
            constraints.anchor = GridBagConstraints.LINE_END;
            constraints.weightx = 0.75;
            constraints.weighty = 0;
            constraints.gridx = 1;
            constraints.gridy = 2 + i;
            constraints.gridwidth = 1;
            container.add(fields.get(i), constraints);
        }
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.anchor = GridBagConstraints.LINE_START;
        constraints.weightx = 0.25;
        constraints.weighty = 0;
        constraints.gridx = 0;
        constraints.gridy = labels.size() + 2;
        constraints.gridwidth = 1;
        container.add(errLbl, constraints);
    }
}

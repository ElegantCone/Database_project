package View;

import controllers.UpdateDataController;
import sql.SQLController;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

public class UpdateDataDialog extends JDialog{
    UpdateWindow window;
    UpdateDataController controller;
    JTable table;
    int[] selectedRows;
    //выводим таблицу с названием tit
    UpdateDataDialog(String tit, SQLController SQLcontroller) throws SQLException {
        this.controller = new UpdateDataController(SQLcontroller, table);
        table = controller.showTable(tit);
        table.setRowHeight(30);
        TableColumnModel cols = table.getColumnModel();
        JPanel btns = new JPanel();
        table.setTableHeader(new JTableHeader(cols));
        table.setAutoCreateRowSorter(true);
        table.setGridColor(Color.BLACK);
        JButton updBtn = new JButton("Сохранить изменения");
        JButton delBtn = new JButton("Удалить строку");
        JLabel errLbl = new JLabel("");
        updBtn.addActionListener(e -> {
            try {
                controller.editTable(tit);
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
                controller.deleteRow(tit, selectedRows);
                errLbl.setText("Строки удалены");
            } catch (Exception ex) {
                errLbl.setText(ex.getMessage());
                System.out.println(ex.getMessage());
            }
        });


        JScrollPane scrollPane = new JScrollPane();
        int x = table.getColumnCount() * 100;
        int y = 10 * 30; //10 rows on screen
        scrollPane.setSize(new Dimension(x, y));
        scrollPane.createVerticalScrollBar();
        scrollPane.createHorizontalScrollBar();
        window = new UpdateWindow(this, tit, true);
        if (x < table.getPreferredSize().width) x = table.getPreferredSize().width;
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setViewportView(table);
        btns.add(updBtn);
        btns.add(delBtn);
        if (x < 400) x += 200;
        table.setSize(new Dimension(x, y));
        window.setBounds(200, 200, x + delBtn.getWidth(), y);
        window.setPreferredSize(new Dimension(x + delBtn.getWidth(), y));
        window.add(btns, BorderLayout.NORTH);
        window.add(scrollPane, BorderLayout.CENTER);
        window.add(errLbl, BorderLayout.SOUTH);
        window.getContentPane().setLocation(0, 100);
        window.setDefaultCloseOperation(DISPOSE_ON_CLOSE);


        window.setVisible(true);
        window.setResizable(false);
    }

    private void close(){
        window.setVisible(false);
        dispose();
    }

    public JTable getTable(){ return table;}
}

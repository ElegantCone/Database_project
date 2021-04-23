package View;

import controllers.UpdateDataController;
import sql.SQLController;

import javax.swing.*;
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
    //выводим таблицу с названием tit
    UpdateDataDialog(String tit, SQLController SQLcontroller) throws SQLException {
        this.controller = new UpdateDataController(SQLcontroller, table);
        table = controller.showTable(tit);
        table.setRowHeight(30);
        TableColumnModel cols = table.getColumnModel();
        table.setTableHeader(new JTableHeader(cols));
        table.setAutoCreateRowSorter(true);
        table.setGridColor(Color.BLACK);
        JButton updBtn = new JButton("Обновить таблицу");
        JLabel errLbl = new JLabel("");
        updBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    controller.editTable(tit);
                    errLbl.setText("Данные обновлены");
                } catch (SQLException ex) {
                    errLbl.setText(ex.getMessage());
                    System.out.println(ex.getMessage());
                }
            }
        });
        JScrollPane scrollPane = new JScrollPane();
        int x = table.getColumnCount()*50;
        int y = 10*30; //10 rows on screen
        scrollPane.setSize(new Dimension(x, y));
        scrollPane.createVerticalScrollBar();
        scrollPane.createHorizontalScrollBar();
        window = new UpdateWindow(this, tit, true);
        if (x < table.getPreferredSize().width) x = table.getPreferredSize().width;
        table.setSize(new Dimension(x, y));
        window.setBounds(100, 200, x, y);

        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setViewportView(table);
        window.add(updBtn, BorderLayout.NORTH);
        window.add(scrollPane, BorderLayout.CENTER);
        window.add(errLbl, BorderLayout.SOUTH);
        window.getContentPane().setLocation(0, 100);

        /*Runnable onCloseEvent = () -> {
            controller.editTable();
            System.out.println("hoba");
        };
        window.setCloseEvent(onCloseEvent);*/

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

package View;

import controllers.ChangeTableController;
import controllers.ShowTableController;
import jdk.nashorn.internal.scripts.JD;
import sql.SQLController;

import javax.swing.*;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;
import java.awt.*;
import java.sql.Connection;
import java.sql.SQLException;

public class ShowTableDialog extends JDialog {
    JDialog window;
    ShowTableController controller;
    JTable table;
    //выводим таблицу с названием tit
    ShowTableDialog(String tit, SQLController SQLcontroller) throws SQLException {
        this.controller = new ShowTableController(SQLcontroller, table);

        table = controller.showTable(tit);
        table.setRowHeight(30);
        TableColumnModel cols = table.getColumnModel();
        table.setTableHeader(new JTableHeader(cols));
        table.setAutoCreateRowSorter(true);
        table.setGridColor(Color.BLACK);

        JScrollPane scrollPane = new JScrollPane();
        int x = table.getColumnCount()*50;
        int y = 10*30; //10 rows on screen
        scrollPane.setSize(new Dimension(x, y));
        scrollPane.createVerticalScrollBar();
        scrollPane.createHorizontalScrollBar();
        window = new JDialog(this, tit, true);
        if (x < table.getPreferredSize().width) x = table.getPreferredSize().width;
        table.setSize(new Dimension(x, y));
        window.setBounds(100, 200, x, y);

        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setViewportView(table);
        window.add(scrollPane, BorderLayout.CENTER);
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

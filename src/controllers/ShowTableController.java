package controllers;

import sql.SQLController;

import javax.swing.*;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Vector;


public class ShowTableController {
    SQLController controller;

    JTable table;

    public ShowTableController(SQLController controller, JTable table){
        this.controller = controller;
        this.table = table;
    }
    //обработка вывода таблицы
    public JTable showTable(String tableName) throws SQLException {
        String sql = controller.doSimpleSelect(tableName, new String[]{"*"});
        ResultSet resSet = controller.getResultSet(sql);
        Vector<String> colNames = controller.getColNames(resSet);
        Vector<Vector<Object>> data = controller.getData(resSet);
        return new JTable(data, colNames){
            @Override
            public boolean isCellEditable(int row, int column){
                return false;
            }
        };
    }


}

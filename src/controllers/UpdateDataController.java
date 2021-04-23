package controllers;

import sql.SQLController;

import javax.swing.*;
import javax.swing.table.TableModel;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Map;
import java.util.Vector;

public class UpdateDataController extends DefaultController{
    JTable table;
    ChangeTableModel model;
    private Vector<String> tblNamesEng = new Vector<>();
    private Vector<String> colWithTwoKeys = new Vector<>();


    public UpdateDataController(SQLController controller, JTable table){
        this.controller = controller;
        this.table = table;
        tblNamesEng.addAll(Arrays.asList("ctn", "ate", "ate_types", "ate_attrs", "payphones", "connection_requests", "people", "subscribers",
                "phone_numbers", "phone_types", "subs_privileges"));
        colWithTwoKeys.addAll(Arrays.asList("intercity_calls", "ate"));
    }
    //обработка вывода таблицы
    public JTable showTable(String tableName) throws SQLException {
        String sql = controller.doSimpleSelect(tableName, new String[]{"*"});
        ResultSet resSet = controller.getResultSet(sql);
        Vector<String> colNames = controller.getColNames(resSet);
        Vector<Vector<Object>> data = controller.getData(resSet);
        model = new ChangeTableModel(data, colNames);
        table = new JTable(model);
        return table;
    }

    public void editTable(String tableName) throws SQLException {
        Map<int[], String> changes = model.getChanges(); // int[] = {row, col}
        for (int[] coords: changes.keySet()) {
            if (coords[0] == 0 && tblNamesEng.contains(changes.get(coords))) continue;
            if (coords[0] == 1 && colWithTwoKeys.contains(changes.get(coords))) continue;
            String id = table.getValueAt(coords[0], 0).toString();
            prepareDataToUpdate(tableName, changes.get(coords), coords[0], coords[1], id);
        }
    }
}

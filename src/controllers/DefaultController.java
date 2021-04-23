package controllers;

import sql.SQLController;

import javax.swing.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

public class DefaultController {
    SQLController controller;

    //получение всех столбцов таблицы
    public Vector<String> getColTable(String tableName) throws SQLException {
        String sql = controller.doSimpleSelect(tableName, new String[]{"*"});
        ResultSet resSet = controller.getResultSet(sql);
        return controller.getColNames(resSet);
    }

    public void prepareDataToInsert(String tableName, Vector<String> data) throws SQLException {
        ResultSet rs = controller.getResultSet(controller.doSimpleSelect(tableName, new String[]{"*"}));
        Vector<String> dataTypes = controller.getColTypes(rs);
        Vector<String> newData = new Vector<>();
        Vector<String> colNames = controller.getColNames(rs);
        Vector<String> newColNames = new Vector<>();

        for (int i = 0; i < data.size(); i++){
            String prevData = data.elementAt(i);
            if (dataTypes.get(i).equals("String") && !data.get(i).isEmpty()) {
                newData.add(i, "'" + prevData + "'");
                newColNames.add(colNames.get(i));
            }
            else if (!data.get(i).isEmpty()){
                newData.add(data.get(i));
                newColNames.add(colNames.get(i));
            }
        }

        controller.insert(tableName, newColNames, newData);
    }

    public void prepareDataToUpdate(String tableName, String data, Integer row, Integer col, String id) throws SQLException {
        ResultSet rs = controller.getResultSet(controller.doSimpleSelect(tableName, new String[]{"*"}));
        String colName = controller.getColNames(rs).get(col);
        String dataType = controller.getColTypes(rs).get(col);
        controller.update(tableName, colName, data, Integer.parseInt(id));
    }

    public Vector<String> getDataFromFields(Vector<JTextField> fields){
        Vector<String> data = new Vector<>();
        for (JTextField field : fields){
            data.add(field.getText());
        }
        return data;
    }


}

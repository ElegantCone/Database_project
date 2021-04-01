package controllers;

import sql.SQLController;

import javax.swing.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

public class SetDataController {
    SQLController controller;

    Vector<JLabel> labels;
    Vector<JTextField> fields;
    JLabel error;

    public SetDataController(SQLController controller){
        this.controller = controller;
    }

    public void setLabelsFields(Vector<JLabel> labels, Vector<JTextField> fields, JLabel err){
        this.labels = labels;
        this.fields = fields;
        this.error = err;
    }

    //получение всех столбцов таблицы
    public Vector<String> getColTable(String tableName) throws SQLException {
        String sql = controller.doSimpleSelect(tableName, new String[]{"*"});
        ResultSet resSet = controller.getResultSet(sql);
        return controller.getColNames(resSet);
    }

    public void insertData(String tableName) throws SQLException {
        Vector<String> data = getDataFromFields();
        ResultSet rs = controller.getResultSet(controller.doSimpleSelect(tableName, new String[]{"*"}));
        Vector<String> dataTypes = controller.getColTypes(rs);
        Vector<String> newData = new Vector<>();
        Vector<String> colNames = controller.getColNames(rs);
        Vector<String> newColNames = new Vector<>();

        for (int i = 0; i < data.size(); i++){
            String prevData = data.elementAt(i);
            if (dataTypes.get(i).equals("String") && !data.get(i).isEmpty()) {
                newData.add(i, "\"" + prevData + "\"");
                newColNames.add(colNames.get(i));
            }
            else if (!data.get(i).isEmpty()){
                newData.add(data.get(i));
                newColNames.add(colNames.get(i));
            }
        }

        controller.insert(tableName, newColNames, newData);
    }

    private Vector<String> getDataFromFields(){
        Vector<String> data = new Vector<>();
        for (JTextField field : fields){
            data.add(field.getText());
        }
        return data;
    }



}

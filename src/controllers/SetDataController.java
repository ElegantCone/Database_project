package controllers;

import sql.SQLController;

import javax.swing.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

public class SetDataController extends DefaultController{
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

    public void insertData(String tit) throws SQLException {
        Vector <String> data = getDataFromFields(fields);
        prepareDataToInsert(tit, data);
    }




}

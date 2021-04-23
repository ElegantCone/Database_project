package controllers;

import sql.SQLController;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.sql.SQLException;

public class ChangeTableController {

    SQLController controller;
    JLabel errLbl;

    public ChangeTableController(SQLController controller, JLabel errLabel){
        this.controller = controller;
        errLbl = errLabel;
        controller.setLabel(errLabel);
    }

    public ActionListener fillTableListener() {
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                errLbl.setText("");
                try {
                    controller.fillTableTest(errLbl);
                } catch (NullPointerException ex){
                    errLbl.setText("Can't find file");
                } catch (IOException ex){
                    errLbl.setText("Bad input");
                }
            }
        };
    }

    public ActionListener createTableListener(){
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                errLbl.setText("");
                try {
                    controller.createTableTest();
                } catch (NullPointerException ex) {
                    errLbl.setText("Can't find file");
                } catch (IOException ex) {
                    errLbl.setText("Bad input");
                }
            }
        };
    }

    public ActionListener dropTableListener(){
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                errLbl.setText("");
                try {
                    controller.dropTableTest();
                } catch (NullPointerException ex) {
                    errLbl.setText("Can't find file");
                } catch (IOException ex) {
                    errLbl.setText("Bad input");
                }
            }
        };
    }


}

package View;

import controllers.SetDataController;
import sql.SQLController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.Vector;

public class SetDataDialog extends JDialog {
    JDialog window;
    SetDataController controller;
    Vector<JTextField> fields = new Vector<>();
    Vector<JLabel> labels = new Vector<>();
    JLabel errLabel = new JLabel(".");

    //вводим данные в таблицу tit
    SetDataDialog(String tit, SQLController SQLcontroller) throws SQLException {
        controller = new SetDataController(SQLcontroller);
        errLabel.setPreferredSize(new Dimension(200, 30));
        errLabel.setForeground(Color.RED);
        JPanel contents = new JPanel();
        JButton insertBtn = new JButton("Создать");
        insertBtn.setPreferredSize(new Dimension(200, 30));
        Vector<String> colNames = controller.getColTable(tit);
        createLabels(colNames);
        createFields();
        for (int i = 0; i < labels.size(); i++){
            contents.add(labels.get(i));
            contents.add(fields.get(i));
        }
        contents.add(errLabel);
        contents.add(insertBtn);

        insertBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.setLabelsFields(labels, fields, errLabel);
                try {
                    controller.insertData(tit);
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    errLabel.setText(ex.getMessage());
                }
            }
        });

        window = new JDialog(this, tit, true);
        window.setBounds(200, 200, 400, labels.size()*40 + 100);
        window.getContentPane().add(contents);
        window.getContentPane().setLocation(0, 0);
        window.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        window.setVisible(true);
        window.setResizable(false);
    }

    private void close(){
        window.setVisible(false);
        dispose();
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
}

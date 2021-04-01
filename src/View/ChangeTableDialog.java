package View;

import controllers.ChangeTableController;
import sql.SQLController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.SQLException;

public class ChangeTableDialog extends JDialog {

    JDialog window;
    ChangeTableController controller;

    ChangeTableDialog(String title, Connection conn) throws SQLException {
        window = new JDialog(this, title, true);

        JButton createTable = new JButton("Создать таблицы");
        JButton dropTable = new JButton("Удалить таблицы");
        JButton fillTable = new JButton("Заполнить тестовыми данными");
        JButton closeDialog = new JButton("Выход");
        JLabel errorLbl = new JLabel("");
        errorLbl.setForeground(Color.RED);

        createTable.setPreferredSize(new Dimension(400, 30));
        dropTable.setPreferredSize(new Dimension(400, 30));
        closeDialog.setPreferredSize(new Dimension(400, 30));
        fillTable.setPreferredSize(new Dimension(400, 30));

        controller = new ChangeTableController(new SQLController(conn), errorLbl);

        closeDialog.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                close();
            }
        });

        fillTable.addActionListener(controller.fillTableListener());
        createTable.addActionListener(controller.createTableListener());
        dropTable.addActionListener(controller.dropTableListener());

        JPanel content = new JPanel();
        content.add(createTable);
        content.add(dropTable);
        content.add(fillTable);
        content.add(closeDialog);
        content.add(errorLbl);

        window.setBounds(350, 100, 450, 400);
        window.getContentPane().add(content);
        window.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        window.setVisible(true);
        window.setResizable(false);
    }

    private void close(){
        window.setVisible(false);
        dispose();
    }

}

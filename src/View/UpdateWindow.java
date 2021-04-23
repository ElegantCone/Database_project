package View;

import javax.swing.*;
import java.util.Map;

public class UpdateWindow extends JDialog {
    public Runnable onCloseEvent;

    public UpdateWindow(UpdateDataDialog updateDataDialog, String tit, boolean b) {
        super(updateDataDialog, tit, b);
    }

    @Override
    public void setDefaultCloseOperation(int operation) {
        super.setDefaultCloseOperation(operation);
    }

    public void setCloseEvent(Runnable onCloseEvent) {
        this.onCloseEvent = onCloseEvent;
    }
}

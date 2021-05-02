package controllers;

import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

public class ChangeTableModel extends DefaultTableModel {
    Map<int[], Object> changes = new HashMap<>();
    Vector<String> delRow = new Vector<>();

    ChangeTableModel(Vector<Vector<Object>> data, Vector<String> colNames){
        super(data, colNames);
    }

    @Override
    public void setValueAt(Object value, int row, int column) {
        super.setValueAt(value, row, column);
        saveValue(value, row, column);
    }

    private void saveValue(Object value, int row, int column) {
        int[] coord = new int[] {row, column};
        changes.put(coord, value);
    }

    public Map<int[], String> getChanges() {
        Map<int[], String> strChanges = new HashMap<>();
        for (int[] coords: changes.keySet()){
            strChanges.put(coords, changes.get(coords).toString());
        }
        return strChanges;
    }
}

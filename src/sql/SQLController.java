package sql;

import javax.swing.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;

public class SQLController {
    Connection conn;
    PreparedStatement statement;
    JLabel errLabel;

    public SQLController(Connection connection) {
        conn = connection;
    }

    public void setLabel(JLabel lab){
        errLabel = lab;
    }

    public void fillTableTest(JLabel err) throws IOException, NullPointerException {
        parseSQL("testData", ";");
        if (errLabel.getText().equals("")) errLabel.setText("Таблицы заполнены");
    }

    public void createTableTest() throws IOException, NullPointerException {
        parseSQL("testTableCreate", ";");
        //parseSQL("testCreateTriggers", "END;/");
        if (errLabel.getText().equals("")) errLabel.setText("Таблицы созданы");
    }

    public void dropTableTest() throws IOException{
        parseSQL("dropTableTest", ";");
        if (errLabel.getText().equals("")) errLabel.setText("Таблицы удалены");
    }

    private void parseSQL(String text, String del) throws IOException {
        InputStream in = getClass().getClassLoader().getResourceAsStream(text);
        BufferedReader reader = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));
        String sqlPart;
        StringBuilder sql = new StringBuilder();

        while ((sqlPart = reader.readLine()) != null){
            if (sqlPart.contains(del) || sqlPart.equals("/")){
                sql.append(sqlPart);
                if (!sqlPart.equals("/")) sql.delete(sql.length()-1, sql.length());
                String ssql = sql.toString();
                execute(sql.toString());
                sql = new StringBuilder();
            }
            else {
                sql.append(sqlPart);
                sql.append("\n");
            }
        }
    }

    public ResultSet getResultSet(String sql) throws SQLException {
        statement = conn.prepareStatement(sql);
        return statement.executeQuery();
    }

    //получаем имена колонок и заполняем данными
    private void fillTables(String tableName, Object [] data) throws SQLException {
        List<String> names = new ArrayList<>();
        statement = conn.prepareStatement(getSQL("select all", tableName, new String[]{""}));
        ResultSetMetaData metaData = statement.executeQuery().getMetaData();
        for (int i = 0; i < metaData.getColumnCount(); i++){
            names.add(metaData.getColumnName(i));
        }

        //todo
    }

    private String getSQL (String type, String tableName, String[] data){
        String sql = "";
        if (type.equals("select all")) {
            sql = "select * from " + tableName;
        }

        return sql;
    }

    private void execute(String sql) {
        try {
            statement = conn.prepareStatement(sql);
            statement.executeQuery();
        } catch (SQLException e) {
            errLabel.setText(e.getMessage());
            System.out.println(e.getLocalizedMessage());
        }
    }

    public String doSimpleSelect(String tableName, String[] what){
        StringBuilder sql = new StringBuilder("select ");
        for (String col: what){
            sql.append(col);
            sql.append(",");
        }
        sql.delete(sql.length()-1, sql.length());
        sql.append(" from " + tableName);
        return sql.toString();
    }

    public Vector<String> getColNames(ResultSet rs) throws SQLException {
        ResultSetMetaData metaData = rs.getMetaData();
        Vector<String> cols = new Vector<>();
        for (int i = 1; i <= metaData.getColumnCount(); i++){
            cols.add(metaData.getColumnName(i));
        }
        return cols;
    }

    public Vector<String> getColTypes(ResultSet rs) throws SQLException {
        Vector<String> colTypes = new Vector<>();
        ResultSetMetaData metaData = rs.getMetaData();
        for (int i = 1; i <= metaData.getColumnCount(); i++){
            int k = metaData.getColumnType(i);
            if (k == Types.SMALLINT || k == Types.INTEGER || k == Types.NUMERIC){
                colTypes.add("Integer");
            }
            else if (k == Types.FLOAT || k == Types.DOUBLE){
                colTypes.add("Double");
            }
            else if (k == Types.VARCHAR){
                colTypes.add("String");
            }
        }
        return colTypes;
    }

    public Vector<Vector<Object>> getData(ResultSet rs) throws SQLException {
        ResultSetMetaData metaData = rs.getMetaData();
        Vector<Vector<Object>> data = new Vector<>();
        while (rs.next()){
            Vector<Object> vector = new Vector<>();
            for (int i = 1; i <= metaData.getColumnCount(); i++){
                vector.add(rs.getObject(i));
            }
            data.add(vector);
        }
        return data;
    }

    public void insert(String tableName, Vector<String> colNames, Vector<String> data) throws SQLException {
        StringBuilder sql = new StringBuilder("insert into " + tableName + " (");
        for (String colName: colNames){
            sql.append(colName);
            sql.append(",");
        }
        sql.delete(sql.length()-1, sql.length());
        sql.append(") values (");
        for (String value: data){
            sql.append(value);
            sql.append(",");
        }
        sql.delete(sql.length()-1, sql.length());
        sql.append(")");

        statement = conn.prepareStatement(sql.toString());
        statement.executeQuery();
    }

    public void update(String tableName, String colName, String data, Integer id) throws SQLException {
        String sql = "UPDATE " + tableName + " SET " + colName + " = ? WHERE id = ?";
        PreparedStatement preparedStatement = conn.prepareStatement(sql);
        preparedStatement.setString(1, data);
        preparedStatement.setString(2, String.valueOf(id));
        preparedStatement.execute();
    }
}

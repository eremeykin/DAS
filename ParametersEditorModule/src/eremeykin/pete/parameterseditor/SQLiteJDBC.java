package eremeykin.pete.parameterseditor;

import java.io.File;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SQLiteJDBC {

    private final Connection connection;
    private final File sqliteFile;

    public SQLiteJDBC(File file) throws ClassNotFoundException, SQLException {
        Class.forName("org.sqlite.JDBC");
        sqliteFile = file;
        connection = DriverManager.getConnection("jdbc:sqlite:" + file.getPath());
    }

    public String[] getItemsList(String tableName, String columnName) throws SQLException, UndefinedDBFile {
        try {
            List<String> items = new ArrayList<>();
            Statement st = getConnection().createStatement();
            ResultSet rs = st.executeQuery("select " + columnName + " from " + tableName + ";");

            while (rs.next()) {
                String currString = rs.getString(columnName);
                items.add(currString);
            }

            String[] result = new String[items.size()];
            items.toArray(result);
            return result;
        } catch (NullPointerException ex) {
            throw new UndefinedDBFile(ex);
        }
    }

    /**
     * This method allows to select specified data from data base.
     * @param table The table from which you want to fetch.
     * @param keyColumn The column witch is key for fetching.
     * @param valueColumn The column witch contains required data.
     * @param key The value of valueColumn witch indicates data.
     * @return Selected single value.
     * @throws UndefinedDBFile
     * @throws SQLException
     */
    public String getValue(String table, String keyColumn, String valueColumn, String key) throws UndefinedDBFile, SQLException {
        try {

            PreparedStatement ps = getConnection().prepareStatement("select " + valueColumn + " from " + table + " where " + keyColumn + "= ?");
            ps.setString(1, key);
            
            ResultSet rs = ps.executeQuery();
            rs.next();
            return rs.getString(1);

        } catch (NullPointerException ex) {
            throw new UndefinedDBFile(ex);
        }
    }

    /**
     * @return the connection
     */
    public Connection getConnection() {
        return connection;
    }

    /**
     * @return the sqliteFile
     */
    public File getSqliteFile() {
        return sqliteFile;
    }

    public class UndefinedDBFile extends SQLException {

        public UndefinedDBFile(Throwable e) {
            super(e);
        }
    }
}

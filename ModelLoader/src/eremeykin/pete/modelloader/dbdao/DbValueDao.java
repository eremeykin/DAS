/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eremeykin.pete.modelloader.dbdao;

import eremeykin.pete.modelapi.Value;
import eremeykin.pete.modelloader.dao.DaoException;
import eremeykin.pete.modelloader.dao.ValueDao;
import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author eremeykin@gmail.com
 */
public class DbValueDao implements ValueDao{

    private final File source;

    public DbValueDao(File source) {
        this.source = source;
    }

    @Override
    public List<Value> getAllSuitable(String table, String column, String keyTable) throws DaoException {
        Statement statement = null;
        Connection connection = null;
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:" + source.getPath());
            connection.setAutoCommit(false);
            final String sql = "select " + column + " as column ," + keyTable + " as key from " + table + ";";
            statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(sql);
            List<Value> valuesList = new ArrayList<>();
            while (rs.next()) {
                String k = rs.getString("key");//See sql alials
                String v = rs.getString("column");//See sql alials
                Value value = new Value(k, v);
                valuesList.add(value);
            }
            connection.commit();
            return valuesList;
        } catch (SQLException ex) {
            if (connection != null) {
                try {
                    connection.rollback();
                } catch (SQLException ex1) {
                    throw new DaoException("Can't rollback connection. Table: " + table + " column: " + "key:" + keyTable, ex);
                }
            }
            throw new DaoException("Can't get all suitable values from table: " + table + " column: "+column + " key:" + keyTable, ex);
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException ex) {
                throw new DaoException("Can't close statement/connection. Table: " + table + " column: " + "key:" + keyTable, ex);
            }
        }
    }

}

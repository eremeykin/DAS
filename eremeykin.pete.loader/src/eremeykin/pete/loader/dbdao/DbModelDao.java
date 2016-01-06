/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eremeykin.pete.loader.dbdao;

import eremeykin.pete.loader.dao.AbstractModelDao;
import eremeykin.pete.loader.dao.DaoException;
import eremeykin.pete.loader.dao.ModelParameterDao;
import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author eremeykin@gmail.com
 */
public class DbModelDao extends AbstractModelDao{

    private final File source;

    public DbModelDao(File source) {
        this.source = source;
    }
    
    private String getTableContent(String table) throws DaoException {
        Statement statement = null;
        Connection connection = null;
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:" + source.getPath());
            connection.setAutoCommit(false);
            final String sql = "select * from " + table + ";";
            statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(sql);
            String script = null;
            while (rs.next()) {
                script = rs.getString(1);
            }
            connection.commit();
            return script;
        } catch (SQLException ex) {
            if (connection != null) {
                try {
                    connection.rollback();
                } catch (SQLException ex1) {
                    throw new DaoException("Can't rollback connection.", ex);
                }
            }
            throw new DaoException("Can't get script form data base.", ex);
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException ex) {
                throw new DaoException("Can't close statement/connection.", ex);
            }
        }
    }

    @Override
    public String getObjModel() throws DaoException {
        return getTableContent(DbConstants.MODEL_TABLE);
    }

    @Override
    public String getScript() throws DaoException {
        return getTableContent(DbConstants.SCRIPT_TABLE);
    }

    @Override
    protected ModelParameterDao getDao() {
        return new DbModelParameterDao(source);
    }

}

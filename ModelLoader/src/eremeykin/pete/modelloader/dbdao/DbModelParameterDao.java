/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eremeykin.pete.modelloader.dbdao;

import eremeykin.pete.modelapi.ModelParameter;
import eremeykin.pete.modelloader.dao.AbstractModelParameterDao;
import eremeykin.pete.modelloader.dao.DaoException;
import eremeykin.pete.modelloader.dao.ValueDao;
import eremeykin.pete.modelloader.dao.entry.ParameterEntry;
import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author eremeykin@gmail.com
 */
public class DbModelParameterDao extends AbstractModelParameterDao {

    private final File source;
    private final ModelParameter root = null;

    public DbModelParameterDao(File source) {
        this.source = source;
    }

    @Override
    protected ValueDao getValueDao() {
        return new DbValueDao(source);
    }

    @Override
    protected Map<ParameterEntry, ModelParameter> getORMap() throws DaoException {
        Statement statement = null;
        Connection connection = null;
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:" + source.getPath());
            connection.setAutoCommit(false);
            final String sql = "select * from " + DbConstants.PARAMETERS_TABLE + ";";
            statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(sql);
            Map<ParameterEntry, ModelParameter> orMap = new HashMap<>();
            while (rs.next()) {
                String id = rs.getString(DbConstants.ID_COLUMN);
                String name = rs.getString(DbConstants.NAME_COLUMN);
                String parent = rs.getString(DbConstants.PARENT_COLUMN);
                String scriptArg = rs.getString(DbConstants.SCRIPTARG_COLUMN);
                String value = rs.getString(DbConstants.VALUE_COLUMN);
                String comment = rs.getString(DbConstants.COMMENT_COLUMN);
                String editorType = rs.getString(DbConstants.EDITOR_TYPE_COLUMN);
                String master = rs.getString(DbConstants.MASTER_COLUMN);
                String editorTable = rs.getString(DbConstants.EDITOR_TABLE_COLUMN);
                String editorColumn = rs.getString(DbConstants.EDITOR_COLUMN_COLUMN);
                ParameterEntry pEntry = new ParameterEntry(id, name, parent, scriptArg, value, comment, editorType, master, editorTable, editorColumn);
                ModelParameter parameter = buildModelParameter(pEntry);
                orMap.put(pEntry, parameter);
            }
            connection.commit();
            return orMap;
        } catch (SQLException ex) {
            if (connection != null) {
                try {
                    connection.rollback();
                } catch (SQLException ex1) {
                    throw new DaoException("Can't rollback connection.", ex1);
                }
            }
            throw new DaoException("Can't get all parameters form data base.", ex);
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException ex) {
                throw new DaoException("Can't close statement/connection.", ex);
            }
        }
    }
}

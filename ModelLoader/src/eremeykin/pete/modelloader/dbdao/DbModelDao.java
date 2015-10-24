/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eremeykin.pete.modelloader.dbdao;

import eremeykin.pete.modelapi.Model;
import eremeykin.pete.modelapi.ModelParameter;
import eremeykin.pete.modelloader.dao.DaoException;
import eremeykin.pete.modelloader.dao.ModelDao;
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
public class DbModelDao implements ModelDao {

    private final File source;

    public DbModelDao(File source) {
        this.source = source;
    }

//    @Override
//    protected List<ParameterEntry> selectAllParameters() throws DaoException {
//        try {
//            Connection connection = DriverManager.getConnection("jdbc:sqlite:" + source.getPath());
//            connection.setAutoCommit(false);
//            Statement st = connection.createStatement();
//            PreparedStatement prepStatement = connection.prepareStatement("select * from " + PARAMETERS_TABLE + ";");
//            ResultSet rSet = prepStatement.executeQuery();
//            List<ParameterEntry> EntryList = new ArrayList<>();
//            while (rSet.next()) {
//                String id = rSet.getString(ID_COLUMN);
//                String name = rSet.getString(NAME_COLUMN);
//                String parent = rSet.getString(PARENT_COLUMN);
//                String scriptArg = rSet.getString(SCRIPT_ARG_COLUMN);
//                String value = rSet.getString(VALUE_COLUMN);
//                String comment = rSet.getString(COMMENT_COLUMN);
//                String editorType = rSet.getString(EDITOR_TYPE_COLUMN);
//                String editorTable = rSet.getString(EDITOR_TABLE_COLUMN);
//                String editorColumn = rSet.getString(EDITOR_COLUMN_COLUMN);
//                ParameterEntry entry = new ParameterEntry(id, name, parent, scriptArg, value, comment, editorType, editorTable, editorColumn);
//                EntryList.add(entry);
//            }
//            return EntryList;
//        } catch (SQLException ex) {
//            throw new DaoException("Can't select all parameters. DataBase file: "+source.getAbsolutePath(), ex);
//        }
//    }
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
    public Model load() throws DaoException {
        DbModelParameterDao modelParameterDao = new DbModelParameterDao(source);
        ModelParameter root = modelParameterDao.getRoot();
        Model model = new Model(root, source, source);
        // model should know if it's parameter changes
        for (ModelParameter parameter : modelParameterDao.getAll()) {
            parameter.addParameterChangedListener(model);
        }
        return model;
    }

}

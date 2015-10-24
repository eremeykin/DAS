/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eremeykin.pete.modelloader.dbdao;

import eremeykin.pete.modelapi.ModelParameter;
import eremeykin.pete.modelapi.ModelParameter.CellProperties.Editor;
import eremeykin.pete.modelapi.Value;
import eremeykin.pete.modelloader.dao.DaoException;
import eremeykin.pete.modelloader.dao.ModelParameterDao;
import eremeykin.pete.modelloader.dao.ValueDao;
import eremeykin.pete.modelloader.dbdao.entry.ParameterEntry;
import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author eremeykin@gmail.com
 */
public class DbModelParameterDao implements ModelParameterDao {

    private final File source;
    private ModelParameter root = null;

    public DbModelParameterDao(File source) {
        this.source = source;
    }

    private Map<ParameterEntry, ModelParameter> getORMap() throws DaoException {
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
            coupleMasterSlaves(orMap);
            coupleChildParent(orMap);
            connection.commit();
            return orMap;
        } catch (SQLException ex) {
            if (connection != null) {
                try {
                    connection.rollback();
                } catch (SQLException ex1) {
                    throw new DaoException("Can't rollback connection.", ex);
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

//<editor-fold defaultstate="collapsed" desc="move to ModelParameterBuilder">
    private ModelParameter buildModelParameter(ParameterEntry entry) throws DaoException {
        String idString = entry.get(ParameterEntry.Field.ID);
        String name = entry.get(ParameterEntry.Field.NAME);
        String argScript = entry.get(ParameterEntry.Field.SCRIPT_ARG);
        String comment = entry.get(ParameterEntry.Field.COMMENT);
        String editorType = entry.get(ParameterEntry.Field.EDITOR_TYPE);
        String masterString = entry.get(ParameterEntry.Field.MASTER);
        String editorTable = entry.get(ParameterEntry.Field.EDITOR_TABLE);
        String editorColumn = entry.get(ParameterEntry.Field.EDITOR_COLUMN);

        Editor editor = parseEditor(editorType, editorTable, editorColumn);
        ModelParameter.CellProperties properties = new ModelParameter.CellProperties(editor);

        try {
            Integer id = Integer.valueOf(idString);
            Integer master = masterString == null ? null : Integer.valueOf(masterString);
            ModelParameter parameter = new ModelParameter(id, name, argScript, comment, master, properties);
            return parameter;
        } catch (NumberFormatException ex) {
            throw new DaoException("Can't parse value to Integer while building entry: " + entry, ex);
        }
    }

    private Editor.Type parseEditorType(String editorType) throws DaoException {
        if (editorType == null) {
            return Editor.Type.DEFAULT;
        }
        if (editorType.equals(DbConstants.TEXT_EDITOR_TYPE)) {
            return Editor.Type.TEXT_BOX;
        }
        if (editorType.equals(DbConstants.CBOX_EDITOR_TYPE)) {
            return Editor.Type.COMBO_BOX;
        }
        if (editorType.equals(DbConstants.AUTO_EDITOR_TYPE)) {
            return Editor.Type.AUTO;
        }
        throw new DaoException("Can't parse editor type: " + editorType);
    }

    private Editor parseEditor(String type, String table, String column) throws DaoException {
        if (type == null || table == null || column == null) {
            return new Editor(Editor.Type.DEFAULT);
        }
        Editor.Type t = parseEditorType(type);
        ValueDao valueDao = new DbValueDao(source);
        List<Value> allSuitable = valueDao.getAllSuitable(table, column, DbConstants.MATERIALS_KEY);
        Value[] values = new Value[allSuitable.size()];
        values = allSuitable.toArray(values);
        Editor editor = new Editor(t, values);
        return editor;
    }
//</editor-fold>

    private void coupleMasterSlaves(Map<ParameterEntry, ModelParameter> orMap) throws DaoException {
        for (Map.Entry<ParameterEntry, ModelParameter> orMapEntry : orMap.entrySet()) {
            ModelParameter currParameter = orMapEntry.getValue();
            ParameterEntry currEntry = orMapEntry.getKey();
            if (currParameter.getMaster() != null) {
                String masterRef = currParameter.getMaster().toString();
                ModelParameter master = findBySign(orMap, ParameterEntry.Field.ID, masterRef);
//                ModelParameter master = findMaster(orMap, masterRef);
                if (master == null) {
                    throw new DaoException("Can't find master for parameter. Slave id=" + currEntry.get(ParameterEntry.Field.ID) + ". Master id=" + masterRef);
                }
                master.addParameterChangedListener(currParameter);
            }
        }
    }

    private void coupleChildParent(Map<ParameterEntry, ModelParameter> orMap) throws DaoException {
        for (Map.Entry<ParameterEntry, ModelParameter> orMapEntry : orMap.entrySet()) {
            ModelParameter currParameter = orMapEntry.getValue();
            ParameterEntry currEntry = orMapEntry.getKey();
            String parentRef = currEntry.get(ParameterEntry.Field.PARENT);
            if (parentRef == null) {
                root = currParameter;
                continue;
            }
            ModelParameter parent = findBySign(orMap, ParameterEntry.Field.ID, parentRef);
//            ModelParameter parent = findParent(orMap, parentRef);
            parent.addChild(currParameter);
        }
    }

    private ModelParameter findBySign(Map< ParameterEntry, ModelParameter> orMap, ParameterEntry.Field sign, String signValue) {
        for (Map.Entry<ParameterEntry, ModelParameter> orMapEntry : orMap.entrySet()) {
            ModelParameter currParameter = orMapEntry.getValue();
            ParameterEntry currEntry = orMapEntry.getKey();
            if (currEntry.get(sign)!=null && currEntry.get(sign).equals(signValue)) {
                return currParameter;
            }
        }
        return null;
    }

//    private ModelParameter findMaster(Map< ParameterEntry, ModelParameter> orMap, String masterRef) {
//        for (Map.Entry<ParameterEntry, ModelParameter> orMapEntry : orMap.entrySet()) {
//            ModelParameter currParameter = orMapEntry.getValue();
//            ParameterEntry currEntry = orMapEntry.getKey();
//            if (currEntry.get(ParameterEntry.Field.ID).equals(masterRef)) {
//                return currParameter;
//            }
//        }
//        return null;
//    }
//    private ModelParameter findParent(Map< ParameterEntry, ModelParameter> orMap, String parentRef) {
//        for (Map.Entry<ParameterEntry, ModelParameter> orMapEntry : orMap.entrySet()) {
//            ModelParameter currParameter = orMapEntry.getValue();
//            ParameterEntry currEntry = orMapEntry.getKey();
//            if (currEntry.get(ParameterEntry.Field.PARENT).equals(parentRef)) {
//                return currParameter;
//            }
//        }
//        return null;
//    }
//    private void coupleMasterSlaves(Map<ParameterEntry, ModelParameter> orMap) {
//        for (Map.Entry<ParameterEntry, ModelParameter> orMapEntry : orMap.entrySet()) {
//            ModelParameter currParameter = orMapEntry.getValue();
//            if (currParameter.getEditor().getType().equals(Editor.Type.AUTO)) {
//                currParameter.setUpdater(new ModelParameter.Updater() {
//
//                    @Override
//                    public void update(ModelParameter master, ModelParameter slave) {
//                        String value = master.getValue();
//                        Editor editor = slave.getEditor();
//                        List<Value> availableValues = editor.getAvailableValues();
//                        for (Value v : availableValues) {
//                            if (v.getKey().equals(value)) {
//                                currParameter.setValue(v.getValue());
//                                return;
//                            }
//                        }
//                    }
//                });
//            }
//        }
//    }
//    private ModelParameter findMaster(Map.Entry<ParameterEntry, ModelParameter> slaveOrMapEntry, Map<ParameterEntry, ModelParameter> orMap) {
//        ParameterEntry slaveParameterEntry = slaveOrMapEntry.getKey();
//        ModelParameter slaveModelParameter = slaveOrMapEntry.getValue();
//        String masterRef = slaveParameterEntry.get(ParameterEntry.Field.EDITOR_TABLE).replaceAll("$", "");
//        for (Map.Entry<ParameterEntry, ModelParameter> entry : orMap.entrySet()) {
//            ParameterEntry currentParameterEntry = entry.getKey();
//            ModelParameter currentModelParameter = entry.getValue();
//            if (currentParameterEntry.get(ParameterEntry.Field.ID).equals(masterRef)) {
//                return currentModelParameter;
//            }
//        }
//        return null;
//    }
    @Override
    public ModelParameter getRoot() throws DaoException {
        getORMap();
        return root;
    }

    @Override
    public List<ModelParameter> getAll() throws DaoException {
        return new ArrayList<>(getORMap().values());
    }
}

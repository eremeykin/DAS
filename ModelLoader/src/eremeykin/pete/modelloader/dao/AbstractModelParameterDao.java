/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eremeykin.pete.modelloader.dao;

import eremeykin.pete.modelapi.ModelParameter;
import eremeykin.pete.modelapi.Value;
import eremeykin.pete.modelloader.dao.entry.ParameterEntry;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *
 * @author eremeykin@gmail.com
 */
public abstract class AbstractModelParameterDao implements ModelParameterDao {

    private ModelParameter root = null;

    protected abstract Map<ParameterEntry, ModelParameter> getORMap() throws DaoException;

    protected abstract ValueDao getValueDao();

    //<editor-fold defaultstate="collapsed" desc="move to ModelParameterBuilder">
    protected final ModelParameter buildModelParameter(ParameterEntry entry) throws DaoException {
        String idString = entry.get(ParameterEntry.Field.ID);
        String name = entry.get(ParameterEntry.Field.NAME);
        String argScript = entry.get(ParameterEntry.Field.SCRIPT_ARG);
        String comment = entry.get(ParameterEntry.Field.COMMENT);
        String editorType = entry.get(ParameterEntry.Field.EDITOR_TYPE);
        String masterString = entry.get(ParameterEntry.Field.MASTER);
        String editorTable = entry.get(ParameterEntry.Field.EDITOR_TABLE);
        String editorColumn = entry.get(ParameterEntry.Field.EDITOR_COLUMN);

        ModelParameter.CellProperties.Editor editor = parseEditor(editorType, editorTable, editorColumn);
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

    protected final ModelParameter.CellProperties.Editor.Type parseEditorType(String editorType) throws DaoException {
        if (editorType == null) {
            return ModelParameter.CellProperties.Editor.Type.DEFAULT;
        }
        if (editorType.equals(DaoConstants.TEXT_EDITOR_TYPE)) {
            return ModelParameter.CellProperties.Editor.Type.TEXT_BOX;
        }
        if (editorType.equals(DaoConstants.CBOX_EDITOR_TYPE)) {
            return ModelParameter.CellProperties.Editor.Type.COMBO_BOX;
        }
        if (editorType.equals(DaoConstants.AUTO_EDITOR_TYPE)) {
            return ModelParameter.CellProperties.Editor.Type.AUTO;
        }
        throw new DaoException("Can't parse editor type: " + editorType);
    }

    protected final ModelParameter.CellProperties.Editor parseEditor(String type, String table, String column) throws DaoException {
        if (type == null || table == null || column == null) {
            return new ModelParameter.CellProperties.Editor(ModelParameter.CellProperties.Editor.Type.DEFAULT);
        }
        ModelParameter.CellProperties.Editor.Type t = parseEditorType(type);
//        ValueDao valueDao = new DbValueDao(source);
        ValueDao valueDao = getValueDao();
        List<Value> allSuitable = valueDao.getAllSuitable(table, column, DaoConstants.MATERIALS_KEY);
        Value[] values = new Value[allSuitable.size()];
        values = allSuitable.toArray(values);
        ModelParameter.CellProperties.Editor editor = new ModelParameter.CellProperties.Editor(t, values);
        return editor;
    }
//</editor-fold>

    private void coupleMasterSlaves(Map<ParameterEntry, ModelParameter> orMap) throws DaoException {
        for (Map.Entry<ParameterEntry, ModelParameter> orMapEntry : orMap.entrySet()) {
            ModelParameter currParameter = orMapEntry.getValue();
            ParameterEntry currEntry = orMapEntry.getKey();
            if (currParameter.getMaster() != null) {
                String masterRef = currParameter.getMaster().toString();
                ModelParameter master = findById(orMap, masterRef);
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
            ModelParameter parent = findById(orMap, parentRef);
            parent.addChild(currParameter);
        }
    }

    protected final ModelParameter findById(Map< ParameterEntry, ModelParameter> orMap, String signValue) {
        for (Map.Entry<ParameterEntry, ModelParameter> orMapEntry : orMap.entrySet()) {
            ModelParameter currParameter = orMapEntry.getValue();
            ParameterEntry currEntry = orMapEntry.getKey();
            if (currEntry.get(ParameterEntry.Field.ID) != null && currEntry.get(ParameterEntry.Field.ID).equals(signValue)) {
                return currParameter;
            }
        }
        return null;
    }

    @Override
    public ModelParameter getRoot() throws DaoException {
        Map<ParameterEntry, ModelParameter> orMap = getORMap();
        coupleMasterSlaves(orMap);
        coupleChildParent(orMap);
        return root;
    }

    @Override
    public List<ModelParameter> getAll() throws DaoException {
        Map<ParameterEntry, ModelParameter> orMap = getORMap();
        coupleMasterSlaves(orMap);
        coupleChildParent(orMap);
        return new ArrayList<>(orMap.values());
    }

}

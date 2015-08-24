package eremeykin.pete.modelloader;

import eremeykin.pete.coreapi.loggerapi.Logger;
import eremeykin.pete.coreapi.loggerapi.LoggerManager;
import eremeykin.pete.coreapi.workspace.WorkspaceManager;
import eremeykin.pete.modelapi.Model;
import eremeykin.pete.modelapi.ModelParameter;
import eremeykin.pete.modelapi.ModelParameter.CellProperties;
import eremeykin.pete.modelapi.ModelParameter.Updater;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.Set;
import javax.swing.JOptionPane;

public class ModelLoader {

    private static final Logger LOGGER = LoggerManager.getLogger(ModelLoader.class);
    private Connection connection;
    private final File mFile;
    private static final String PARAMETERS_TABLE = "parameters";
    private static final String MODEL_TABLE = "obj_model";
    private static final String SCRIPT_TABLE = "refresh_script";
    private static final String ID_COLUMN = "id";
    private static final String NAME_COLUMN = "name";
    private static final String PARENT_COLUMN = "parent";
    private static final String SCRIPTARG_COLUMN = "script_arg";
    private static final String COMMENT_COLUMN = "comment";
    private static final String VALUE_COLUMN = "value";
    private static final String EDITOR_TYPE_COLUMN = "editor";
    private static final String EDITOR_TABLE_COLUMN = "table";
    private static final String EDITOR_COLUMN_COLUMN = "column";
    private static final String MODEL_COLUMN = "content";
    private static final String SCRIPT_COLUMN = "content";

    public ModelLoader(File file) throws ClassNotFoundException, SQLException {
        Class.forName("org.sqlite.JDBC");
        mFile = file;
        connection = DriverManager.getConnection("jdbc:sqlite:" + file.getPath());
    }

    public Model load() throws LoadingException, FileNotFoundException {
        try {
            Map<Integer, Row> map = new HashMap<>();//key is a ID and value is parameter
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery("select * from " + PARAMETERS_TABLE + ";");
            // читаем базу данных и записываем все в Map где ключ это id а значение -
            // собственно параметер
            while (rs.next()) {
                Integer id = getInteger(rs, ID_COLUMN);
                String name = rs.getString(NAME_COLUMN);
                Integer parentId = getInteger(rs, PARENT_COLUMN);
                Integer scarg = getInteger(rs, SCRIPTARG_COLUMN);
                String comment = rs.getString(COMMENT_COLUMN);
                String eType = rs.getString(EDITOR_TYPE_COLUMN);
                String eTable = rs.getString(EDITOR_TABLE_COLUMN);
                String eColumn = rs.getString(EDITOR_COLUMN_COLUMN);
                String value = rs.getString(VALUE_COLUMN);
                CellProperties cProperties = extractEditor(eType, eTable, eColumn);
                ModelParameter p = new ModelParameter(id, name, scarg, comment, cProperties);
                if (value != null) {
                    p.setValue(value);
                }
                Row r = new Row(p, parentId, eType, eTable, eColumn);
                map.put(r.parameter.getId(), r);
            }
            // root параметр который не имеет родителей, т.е. 
            // parent == null
            // ищем root
            ModelParameter root = findRoot(map.values());
            List<ModelParameter> parentList = new ArrayList<>();
            parentList.add(root);
            //accord parents with children
            while (!parentList.isEmpty()) {
                List<ModelParameter> childrenList;
                for (ListIterator<ModelParameter> iterator = parentList.listIterator(); iterator.hasNext();) {
                    ModelParameter currParent = iterator.next();
                    childrenList = findChildren(currParent, map.values());
                    currParent.setChildren(childrenList);
                    iterator.remove();
                    for (ModelParameter child : childrenList) {
                        iterator.add(child);
                    }
                }
            }
            // link auto editors
            linkEditors(map);
            //get model Reader
            File modelFile = unpackColumnToWorkspaceFile(MODEL_TABLE, MODEL_COLUMN, "modelFile.obj");
            File scriptFile = unpackColumnToWorkspaceFile(SCRIPT_TABLE, SCRIPT_COLUMN, "script.py");
            Model model = new Model(root, modelFile, scriptFile);
            // добавляем для каждого параметра модель в слушатели
            // чтоб она потом могла узнать что её параметры изменились
            for (Row r : map.values()) {
                r.parameter.addParameterChangedListener(model);
            }
            LOGGER.info("Model " + mFile.getAbsolutePath() + " has been successfully loaded. ");
            return model;
        } catch (SQLException ex) {
            LoadingException lex = new LoadingException();
            lex.initCause(ex);
            throw lex;
        }
    }

    private void printStringToFile(String str, File file) throws FileNotFoundException {
        PrintWriter out = new PrintWriter(file);
        out.print(str);
        out.close();
    }

    private File unpackColumnToWorkspaceFile(String table, String column, String fileName) throws SQLException, FileNotFoundException {
        Statement st = connection.createStatement();
        ResultSet resSet = st.executeQuery("select * from " + table + ";");
        String str = resSet.getString(column);
        File resultFile = new File(WorkspaceManager.INSTANCE.getWorkspace().getAbsolutePath() + "\\" + fileName);
        printStringToFile(str, resultFile);
        return resultFile;
    }

    private Integer getInteger(ResultSet rs, String columnLabel) throws SQLException {
        Integer integer = rs.getInt(columnLabel);
        if (rs.wasNull()) {
            integer = null;
        }
        return integer;
    }

    private ModelParameter findRoot(Collection<Row> rows) throws LoadingException {
        ModelParameter root = null;
        for (Row r : rows) {
            ModelParameter parameter = r.parameter;
            Integer parentId = r.parentId;
            if (parentId == null && root != null) {
                throw new LoadingException("Больше одного корня.");
            }
            if (parentId == null && root == null) {
                root = parameter;// first null was found
            }
        }
        if (root != null) {
            return root;
        } else {
            throw new LoadingException("Нет ни одного корня");
        }

    }

    private List<ModelParameter> findChildren(ModelParameter parent, Collection<Row> rows) {
        Integer parentId = parent.getId();
        List<ModelParameter> children = new ArrayList<>();
        for (Row r : rows) {
            ModelParameter currParameter = r.parameter;
            Integer currParentId = r.parentId;
            if (currParentId != null && currParentId.equals(parentId)) {//value == null if key is root
                children.add(currParameter);
            }
        }
        return children;
    }

    private CellProperties extractEditor(String editorType, String editorTable, String editorColumn) throws SQLException {
        if (editorType == null) {
            CellProperties.Editor editor = new CellProperties.Editor(CellProperties.Editor.Type.DEFAULT);
            CellProperties cp = new CellProperties(editor);
            return cp;
        }
        if (editorType.equals("text")) {
            CellProperties.Editor editor = new CellProperties.Editor(CellProperties.Editor.Type.TEXT_BOX);
            CellProperties cp = new CellProperties(editor);
            return cp;
        }
        if (editorType.equals("cbox")) {
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery("select " + editorColumn + " from " + editorTable + ";");
            List items = new ArrayList();
            while (rs.next()) {
                items.add(rs.getString(editorColumn));
            }
            CellProperties.Editor editor = new CellProperties.Editor(CellProperties.Editor.Type.COMBO_BOX, items.toArray());
            CellProperties cp = new CellProperties(editor);
            return cp;
        }
        CellProperties.Editor editor = new CellProperties.Editor(CellProperties.Editor.Type.DEFAULT);
        CellProperties cp = new CellProperties(editor);
        return cp;
    }

    private void linkEditors(Map<Integer, Row> map) throws LoadingException {
        Set<Row> autoEditable = new HashSet<>();
        for (Row r : map.values()) {
            if (r.editorType != null && r.editorType.equals("auto")) {
                autoEditable.add(r);
            }
        }
        for (Row r : autoEditable) {
            if (!r.editorTable.startsWith("$")) {
                throw new LoadingException("В таблице найдена неправильная запись авторедактируемой ячейки.");
            }
            try {
                Integer masterParameterId = new Scanner(r.editorTable.substring(1)).nextInt();
                ModelParameter slaveParameter = r.parameter;
                Row masterRow = map.get(masterParameterId);
                ModelParameter masterParameter = map.get(masterParameterId).parameter;
                slaveParameter.setUpdater(new Updater() {
                    @Override
                    public void update(String value) {
                        try {
                            Statement st = connection.createStatement();
                            String qString = "select " + r.editorColumn + " from " + masterRow.editorTable + " where " + masterRow.editorColumn + "='" + value.toString() + "';";
                            ResultSet rs = st.executeQuery(qString);
                            while (rs.next()) {
                                slaveParameter.setValue(rs.getString(1));
                            }
                        } catch (SQLException ex) {
                            JOptionPane.showMessageDialog(null, "Произошла SQL ошибка при обновлении значения подчиненного параметра.");
                        }
                    }
                });
                masterParameter.addParameterChangedListener(slaveParameter);
            } catch (NoSuchElementException ex) {
                throw new LoadingException("В таблице найдена неправильная запись авторедактируемой ячейки.");

            }
        }

    }

    private static class Row {

        private ModelParameter parameter;
        private Integer parentId;
        private String editorType;
        private String editorTable;
        private String editorColumn;

        Row(ModelParameter parameter, Integer parentId, String eType, String eTable, String eColumn) {
            this.parameter = parameter;
            this.parentId = parentId;
            this.editorType = eType;
            this.editorTable = eTable;
            this.editorColumn = eColumn;
        }
    }

    public static class LoadingException extends Exception {

        public LoadingException(String message) {
            super(message);
        }

        public LoadingException() {
        }

    }
}

package eremeykin.pete.parameterseditor;

import java.io.File;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Map.Entry;
import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.JTextField;

public class ModelLoader {

    private final Connection connection;
    private final File sqliteFile;
    private static final String PARAMETERS_TABLE = "parameters";
    private static final String ID_COLUMN = "id";
    private static final String NAME_COLUMN = "name";
    private static final String PARENT_COLUMN = "parent";
    private static final String SCRIPTARG_COLUMN = "script_arg";
    private static final String COMMENT_COLUMN = "comment";
    private static final String EDITOR_TYPE_COLUMN = "editor";
    private static final String EDITOR_TABLE_COLUMN = "table";
    private static final String EDITOR_COLUMN_COLUMN = "column";

    public ModelLoader(File file) throws ClassNotFoundException, SQLException {
        Class.forName("org.sqlite.JDBC");
        sqliteFile = file;
        connection = DriverManager.getConnection("jdbc:sqlite:" + file.getPath());
    }

    public Parameter load() throws LoadingException {
        try {
            Map<Parameter, Integer> table = new HashMap<>();
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery("select * from " + PARAMETERS_TABLE + ";");
            while (rs.next()) {
                Integer id = getInteger(rs, ID_COLUMN);
                String name = rs.getString(NAME_COLUMN);
                Integer parentId = getInteger(rs, PARENT_COLUMN);
                Integer scarg = getInteger(rs, SCRIPTARG_COLUMN);
                String comment = rs.getString(COMMENT_COLUMN);
                String eType = rs.getString(EDITOR_TYPE_COLUMN);
                String eTable = rs.getString(EDITOR_TABLE_COLUMN);
                String eColumn = rs.getString(EDITOR_COLUMN_COLUMN);
                DefaultCellEditor editor = extractEditor(eType, eTable, eColumn);
                Parameter p = new Parameter(id, name, scarg, comment, editor);
                table.put(p, parentId);
            }
            Parameter root = getRoot(table);
            List<Parameter> parentList = new ArrayList<>();
            parentList.add(root);
            while (!parentList.isEmpty()) {
                List<Parameter> childrenList;
                for (ListIterator<Parameter> iterator = parentList.listIterator(); iterator.hasNext();) {
                    Parameter currParent = iterator.next();
                    childrenList = findChildren(currParent, table);
                    currParent.setChildren(childrenList);
                    iterator.remove();
                    for (Parameter child : childrenList) {
                        iterator.add(child);
                    }

                }
            }
            return root;
        } catch (SQLException ex) {
            LoadingException lex = new LoadingException();
            lex.initCause(ex);
            throw lex;
        }
    }

    private Integer getInteger(ResultSet rs, String columnLabel) throws SQLException {
        Integer integer = rs.getInt(columnLabel);
        if (rs.wasNull()) {
            integer = null;
        }
        return integer;
    }

    private Parameter getRoot(Map<Parameter, Integer> table) throws LoadingException {
        Parameter root = null;
        for (Entry<Parameter, Integer> entry : table.entrySet()) {
            Parameter parameter = entry.getKey();
            Integer parentId = entry.getValue();
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

    private List<Parameter> findChildren(Parameter parent, Map<Parameter, Integer> table) {
        Integer parentId = parent.getId();
        List<Parameter> children = new ArrayList<>();
        for (Entry<Parameter, Integer> entrySet : table.entrySet()) {
            Parameter key = entrySet.getKey();
            Integer value = entrySet.getValue();
            if (value != null && value.equals(parentId)) {//value == null if key is root
                children.add(key);
            }
        }
        return children;
    }

    private DefaultCellEditor extractEditor(String editorType, String editorTable, String editorColumn) throws SQLException {
        if (editorType == null) {
            return null;
        }
        if (editorType.equals("text")) {
            return new DefaultCellEditor(new JTextField());
        }
        if (editorType.equals("cbox")) {
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery("select " + editorColumn + " from " + editorTable + ";");
            List items = new ArrayList();
            while (rs.next()) {
                items.add(rs.getString(editorColumn));
            }
            return new DefaultCellEditor(new JComboBox(items.toArray()));
        }
        return null;
    }

    public static class LoadingException extends Exception {

        public LoadingException(String message) {
            super(message);
        }

        public LoadingException() {
        }

    }
}

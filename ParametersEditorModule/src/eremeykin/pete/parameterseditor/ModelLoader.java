package eremeykin.pete.parameterseditor;

import eremeykin.pete.parameterseditor.Parameter.CellProperties;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.EventObject;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.Set;
import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellEditor;
import org.openide.util.Exceptions;

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
            Map<Integer, Row> map = new HashMap<>();//key is a ID and value is parameter
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
                CellProperties cProperties = extractEditor(eType, eTable, eColumn);
                Parameter p = new Parameter(id, name, scarg, comment, cProperties);
                Row r = new Row(p, parentId, eType, eTable, eColumn);
                map.put(r.parameter.getId(), r);
            }
            Parameter root = findRoot(map.values());
            List<Parameter> parentList = new ArrayList<>();
            parentList.add(root);
            //accord parents wiht children
            while (!parentList.isEmpty()) {
                List<Parameter> childrenList;
                for (ListIterator<Parameter> iterator = parentList.listIterator(); iterator.hasNext();) {
                    Parameter currParent = iterator.next();
                    childrenList = findChildren(currParent, map.values());
                    currParent.setChildren(childrenList);
                    iterator.remove();
                    for (Parameter child : childrenList) {
                        iterator.add(child);
                    }
                }
            }
            // link auto editors
            linkEditors(map);
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

    private Parameter findRoot(Collection<Row> rows) throws LoadingException {
        Parameter root = null;
        for (Row r : rows) {
            Parameter parameter = r.parameter;
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

    private List<Parameter> findChildren(Parameter parent, Collection<Row> rows) {
        Integer parentId = parent.getId();
        List<Parameter> children = new ArrayList<>();
        for (Row r : rows) {
            Parameter currParameter = r.parameter;
            Integer currParentId = r.parentId;
            if (currParentId != null && currParentId.equals(parentId)) {//value == null if key is root
                children.add(currParameter);
            }
        }
        return children;
    }

    private CellProperties extractEditor(String editorType, String editorTable, String editorColumn) throws SQLException {
        if (editorType == null) {
            DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
            CellProperties cp = new CellProperties(null, renderer);
            return cp;
        }
        if (editorType.equals("text")) {
            DefaultCellEditor editor = new DefaultCellEditor(new JTextField());
            DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
            CellProperties cp = new CellProperties(editor, renderer);
            return cp;
        }
        if (editorType.equals("cbox")) {
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery("select " + editorColumn + " from " + editorTable + ";");
            List items = new ArrayList();
            while (rs.next()) {
                items.add(rs.getString(editorColumn));
            }
            DefaultCellEditor editor = new DefaultCellEditor(new JComboBox(items.toArray()));
            DefaultTableCellRenderer renderer = new DefaultTableCellRenderer() {

                @Override
                public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                    value = value == null ? "" : value.toString();
                    Component c = new JComboBox(new String[]{value.toString()});
                    return c;
                }
            };
            CellProperties cp = new CellProperties(editor, renderer);
            return cp;
        }
        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
        CellProperties cp = new CellProperties(null, renderer);
        return cp;
    }

    private void linkEditors(Map<Integer, Row> map) throws LoadingException {
        Set<Row> autoEditable = new HashSet();
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
                Parameter slaveParameter = r.parameter;
                Row masterRow = map.get(masterParameterId);
                Parameter masterParameter = map.get(masterParameterId).parameter;
                slaveParameter.setDecorator(new Parameter.Decorator(slaveParameter){

                    @Override
                    void perform(Object newValue) {

                        try {
                            Statement st = connection.createStatement();
                            String qString = "select " + r.editorColumn + " from " + masterRow.editorTable + " where " + masterRow.editorColumn + "='" + newValue.toString() + "';";
//                            System.out.println(qString);
                            ResultSet rs = st.executeQuery(qString);
                            while (rs.next()) {
                                p.setValue(rs.getString(1));
                            }
                        } catch (SQLException ex) {
                            JOptionPane.showMessageDialog(null, "Произошла SQL ошибка при обновлении значения подчиненного параметра.");
                        }
                    }
                    
                });
//                slaveParameter = new Parameter(slaveParameter) {
//
//                    @Override
//                    public void masterChangedValue(Object newValue) {
//                        try {
//                            Statement st = connection.createStatement();
//                            String qString = "select " + r.editorColumn + " from " + masterRow.editorTable + " where " + masterRow.editorColumn + "='" + newValue.toString() + "';";
////                            System.out.println(qString);
//                            ResultSet rs = st.executeQuery(qString);
//                            while (rs.next()) {
//                                this.setValue(rs.getString(1));
//                            }
//                        } catch (SQLException ex) {
//                            JOptionPane.showMessageDialog(null, "Произошла SQL ошибка при обновлении значения подчиненного параметра.");
//                        }
//                    }
//
//                };
                masterParameter.addSlaveParameter(slaveParameter);
//                masterParameter.getEditor().addCellEditorListener((CellEditorListener) slaveParameter.getEditor());
//                DefaultCellEditor editor = (DefaultCellEditor) masterParameter.getEditor();
//                ((JComboBox) editor.getComponent()).addActionListener((ActionListener) slaveParameter.getEditor());
            } catch (NoSuchElementException ex) {
                throw new LoadingException("В таблице найдена неправильная запись авторедактируемой ячейки.");
            }
        }

    }

    private static class Row {

        private Parameter parameter;
        private Integer parentId;
        private String editorType;
        private String editorTable;
        private String editorColumn;

        Row(Parameter parameter, Integer parentId, String eType, String eTable, String eColumn) {
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

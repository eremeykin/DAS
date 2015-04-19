package eremeykin.pete.modelloader;

import eremeykin.pete.modelapi.Model;
import eremeykin.pete.modelapi.Parameter;
import eremeykin.pete.modelapi.ModelParameter;
import eremeykin.pete.modelapi.ModelParameter.CellProperties;
import java.awt.Component;
import java.io.File;
import java.io.Reader;
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
import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableCellRenderer;
import org.openide.util.lookup.InstanceContent;

public class ModelLoader {
    
    private Connection connection;
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
    private InstanceContent content = new InstanceContent();
    
    public ModelLoader(File file) throws ClassNotFoundException, SQLException {
        Class.forName("org.sqlite.JDBC");
        connection = DriverManager.getConnection("jdbc:sqlite:" + file.getPath());
    }
    
    public Model load() throws LoadingException {
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
                String value = rs.getString(VALUE_COLUMN);
                CellProperties cProperties = extractEditor(eType, eTable, eColumn);
                ModelParameter p = new ModelParameter(id, name, scarg, comment, cProperties);
                if (value != null) {
                    p.setValue(value);
                }
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
            //get model Reader
            Statement st2 = connection.createStatement();
            ResultSet rs2 = st2.executeQuery("select * from " + MODEL_TABLE + ";");
            Reader modelReader = rs2.getCharacterStream(MODEL_COLUMN);
            //get script Reader
            Statement st3 = connection.createStatement();
            ResultSet rs3 = st3.executeQuery("select * from " + SCRIPT_TABLE + ";");
            Reader scriptReader = rs3.getCharacterStream(SCRIPT_COLUMN);
            Model model = new Model(root, modelReader, scriptReader);
            return model;
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
    
    private List<Parameter> findChildren(Parameter parent, Collection<Row> rows) {
        Integer parentId = parent.getId();
        List<Parameter> children = new ArrayList<>();
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
                slaveParameter.setUpdater(new Parameter.Updater() {
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
                masterParameter.addSlaveParameter(slaveParameter);
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

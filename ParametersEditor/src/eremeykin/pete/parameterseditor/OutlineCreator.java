/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eremeykin.pete.parameterseditor;

import eremeykin.pete.modelapi.ModelParameter;
import eremeykin.pete.modelapi.ModelParameter.CellProperties.Editor;
import eremeykin.pete.modelapi.ModelParameter.CellProperties.Renderer;
import java.awt.Color;
import java.awt.Component;
import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import org.netbeans.swing.outline.DefaultOutlineModel;
import org.netbeans.swing.outline.Outline;
import org.netbeans.swing.outline.OutlineModel;
import org.netbeans.swing.outline.RowModel;
import javax.swing.DefaultCellEditor;
import javax.swing.JTextField;
import org.netbeans.swing.outline.DefaultOutlineCellRenderer;

/**
 *
 * @author eremeykin
 */
public class OutlineCreator {

    private Outline outline;

    public OutlineCreator(ModelParameter root) {

        TreeModel treeMdl = new TreeModel(root);
        OutlineModel mdl = DefaultOutlineModel.createOutlineModel(treeMdl, new TableModel(), true, "Название");
        outline = new Outline() {

            @Override
            public TableCellEditor getCellEditor(int row, int column) {
                int modelRow = convertRowIndexToModel(row);
                int modelColumn = convertColumnIndexToModel(column);
                if (modelColumn != 2) {
                    return null;
                }
                ModelParameter selected = (ModelParameter) (this.getModel().getValueAt(modelRow, 0));
                Editor editor = selected.getEditor();
                if (editor == null) {
                    return null;
                }
                switch (editor.getType()) {
                    case DEFAULT:
                        return null;
                    case COMBO_BOX: {
                        JComboBox jcb = new JComboBox(editor.getAvailableValues());
                        jcb.setSelectedIndex(0);
                        DefaultCellEditor e = new DefaultCellEditor(jcb);
                        return e;
                    }
                    case TEXT_BOX: {
                        DefaultCellEditor e = new DefaultCellEditor(new JTextField());
                        return e;
                    }
                    default:
                        throw new AssertionError(editor.getType().name());
                }
//                return null;
            }

            @Override
            public TableCellRenderer getCellRenderer(int row, int column) {
                TableCellRenderer renderer = super.getCellRenderer(row, column);
                int modelRow = convertRowIndexToModel(row);
                int modelColumn = convertColumnIndexToModel(column);
                ModelParameter selected = (ModelParameter) (this.getModel().getValueAt(modelRow, 0));
                Renderer selectedRenderer = selected.getRenderer();
                if (modelColumn == 2) {
                    switch (selectedRenderer.getType()) {
                        case DEFAULT:
                            return new DefaultOutlineCellRenderer();
                        case COMBO_BOX: {

                            DefaultTableCellRenderer dtcr = new DefaultTableCellRenderer() {
                                @Override
                                public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column
                                ) {
                                    value = value == null ? "" : value.toString();
                                    JComboBox c = new JComboBox(new String[]{value.toString()});
                                    c.setSelectedIndex(0);
                                    return c;
                                }
                            };
                            return dtcr;
                        }
                        default:
                            throw new AssertionError(selectedRenderer.getType().name());
                    }
                }
                return renderer;
            }
        };
        outline.setRootVisible(true);
        outline.setModel(mdl);
        outline.setDefaultRenderer(String.class, new RendererForChangeable());
        outline.getTableHeader().setVisible(true);
        outline.setRowSorter(null);
    }

    public Outline getOutline() {
        return outline;
    }

    private static class TreeModel implements javax.swing.tree.TreeModel {

        private ModelParameter root;

        public TreeModel(ModelParameter root) {
            this.root = root;
        }

        @Override
        public void addTreeModelListener(javax.swing.event.TreeModelListener l) {
            //do nothing
        }

        @Override
        public Object getChild(Object parent, int index) {
            ModelParameter p = (ModelParameter) parent;
            return p.getChildren().get(index);
        }

        @Override
        public int getChildCount(Object parent) {
            ModelParameter p = (ModelParameter) parent;
            return p.getChildren().size();
        }

        @Override
        public int getIndexOfChild(Object parent, Object child) {
            ModelParameter p = (ModelParameter) parent;
            ModelParameter ch = (ModelParameter) child;
            return p.getChildren().indexOf(ch);
        }

        @Override
        public Object getRoot() {
            return root;
        }

        @Override
        public boolean isLeaf(Object object) {
            return ((ModelParameter) object).getChildren().isEmpty();
        }

        @Override
        public void removeTreeModelListener(javax.swing.event.TreeModelListener l) {
            //do nothing
        }

        @Override
        public void valueForPathChanged(javax.swing.tree.TreePath path, Object newValue) {
            //do nothing
        }
    }

    private class TableModel extends DefaultTableModel implements RowModel {

        @Override
        public Class getColumnClass(int column) {
            switch (column) {
                case 0:
                    return String.class;
                case 1:
                    return String.class;
                default:
                    assert false;
            }
            return null;
        }

        @Override
        public int getColumnCount() {
            return 2;
        }

        @Override
        public String getColumnName(int column) {
            return column == 0 ? "Комментарий" : "Значение";
        }

        @Override
        public Object getValueFor(Object object, int column) {
            ModelParameter parameter = (ModelParameter) object;
            switch (column) {
                case 0:
                    return parameter.getComment();
                case 1:
                    return parameter.getValue();
                default:
                    assert false;
            }
            return null;
        }

        @Override
        public boolean isCellEditable(Object node, int column) {
            return column == 1;
        }

        @Override
        public void setValueFor(Object object, int column, Object value) {
            ModelParameter parameter = (ModelParameter) object;
            parameter.setValue(value.toString());
            outline.repaint();
        }
    }

    private static class RendererForChangeable extends DefaultTableCellRenderer {

        Color backgroundColor = getBackground();

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            int column2 = table.convertColumnIndexToModel(column);
            if (!isSelected && column2 == 2 && table.getCellEditor(row, column) != null) {
                Color col = new Color(230, 230, 250);
                c.setBackground(col);
            } else if (!isSelected) {
                c.setBackground(backgroundColor);
            }

            return c;
        }
    }

}

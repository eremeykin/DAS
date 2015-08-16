/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eremeykin.pete.parameterseditor;

import eremeykin.pete.modelapi.Parameter;
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

/**
 *
 * @author eremeykin
 */
public class OutlineCreator {

    private Outline outline;

    public OutlineCreator(Parameter root) {

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
                Parameter selected = (Parameter) (this.getModel().getValueAt(modelRow, 0));
                return selected.getEditor();
            }

            @Override
            public TableCellRenderer getCellRenderer(int row, int column) {
                TableCellRenderer renderer = super.getCellRenderer(row, column);
                int modelRow = convertRowIndexToModel(row);
                int modelColumn = convertColumnIndexToModel(column);
                Parameter selected = (Parameter) (this.getModel().getValueAt(modelRow, 0));
                if (modelColumn == 2) {
                    renderer = selected.getRenderer();
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

    private static class TreeModel  implements javax.swing.tree.TreeModel {

        private Parameter root;

        public TreeModel(Parameter root) {
            this.root = root;
        }

        @Override
        public void addTreeModelListener(javax.swing.event.TreeModelListener l) {
            //do nothing
        }

        @Override
        public Object getChild(Object parent, int index) {
            Parameter p = (Parameter) parent;
            return p.getChildren().get(index);
        }

        @Override
        public int getChildCount(Object parent) {
            Parameter p = (Parameter) parent;
            return p.getChildren().size();
        }

        @Override
        public int getIndexOfChild(Object parent, Object child) {
            Parameter p = (Parameter) parent;
            Parameter ch = (Parameter) child;
            return p.getChildren().indexOf(ch);
        }

        @Override
        public Object getRoot() {
            return root;
        }

        @Override
        public boolean isLeaf(Object object) {
            return ((Parameter) object).getChildren().isEmpty();
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
            Parameter parameter = (Parameter) object;
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
            Parameter parameter = (Parameter) object;
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
            if (!isSelected && column2 == 2 && table.getCellEditor(row, column) !=null) {
                Color col = new Color(230, 230, 250);
                c.setBackground(col);
            }else if(!isSelected) {
                c.setBackground(backgroundColor);
            }
        
        return c;
    }
}

}

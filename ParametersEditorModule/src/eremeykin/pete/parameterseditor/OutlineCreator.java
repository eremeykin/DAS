/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eremeykin.pete.parameterseditor;

import java.awt.Component;
import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.tree.TreeModel;
import org.netbeans.swing.outline.DefaultOutlineModel;
import org.netbeans.swing.outline.Outline;
import org.netbeans.swing.outline.OutlineModel;

/**
 *
 * @author eremeykin
 */
public class OutlineCreator {

    private Outline outline;

    public OutlineCreator(Parameter root) {

        TreeModel treeMdl = new ParametersModel(root);
        OutlineModel mdl = DefaultOutlineModel.createOutlineModel(treeMdl, new TreeTableRowModel(), true, "Название");
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
    }

    public Outline getOutline() {
        return outline;
    }

}

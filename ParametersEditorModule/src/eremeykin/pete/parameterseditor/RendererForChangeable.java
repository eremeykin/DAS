/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eremeykin.pete.parameterseditor;

import java.awt.Color;
import java.awt.Component;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

/**
 *
 * @author eremeykin
 */
public class RendererForChangeable extends DefaultTableCellRenderer {

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

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eremeykin.pete.parameterseditor;

import org.netbeans.swing.outline.RowModel;

/**
 *
 * @author Pete
 */
public class TreeTableRowModel implements RowModel {

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
    }
}

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
class ParameterRowModel implements RowModel {

    public ParameterRowModel() {
    }

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
    public Object getValueFor(Object node, int column) {
        return "Test";
    }

    @Override
    public boolean isCellEditable(Object node, int column) {
        return false;
    }

    @Override
    public void setValueFor(Object node, int column, Object value) {
        //do nothing for now
    }

}

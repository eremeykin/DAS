/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eremeykin.pete.parameterseditor;

import javax.swing.tree.TreeModel;

/**
 *
 * @author Pete
 */
public class ParametersModel implements TreeModel {

    private Parameter root;

    public ParametersModel(Parameter root) {
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

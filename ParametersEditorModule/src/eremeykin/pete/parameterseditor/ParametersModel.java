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

    private Node root;

    public ParametersModel(Node root) {
        this.root = root;
    }

    @Override
    public void addTreeModelListener(javax.swing.event.TreeModelListener l) {
        //do nothing
    }

    @Override
    public Object getChild(Object parent, int index) {
        Node n = (Node) parent;
        return n.getChildren().get(index);
    }

    @Override
    public int getChildCount(Object parent) {
        Node node = (Node) parent;
        return node.getChildren().size();
    }

    @Override
    public int getIndexOfChild(Object parent, Object child) {
        Node par = (Node) parent;
        Node ch = (Node) child;
        return par.getChildren().indexOf(ch);
    }

    @Override
    public Object getRoot() {
        return root;
    }

    @Override
    public boolean isLeaf(Object node) {
        return ((Node) node).getChildren().isEmpty();
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

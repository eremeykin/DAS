/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eremeykin.pete.parameterseditor;

import javax.swing.tree.TreeModel;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.nodes.Node;

/**
 *
 * @author Pete
 */
class ParameterTreeModel implements TreeModel {

    private Node root;

    public ParameterTreeModel(Node root) {
        this.root = root;
    }

    @Override
    public Object getChild(Object parent, int index) {
        //Node n = (Node) parent;
        ParameterChildFactory factory = new ParameterChildFactory();
        return new AbstractNode(Children.create(factory, true));
//        return n.getChildren().getNodeAt(index);
    }

    @Override
    public int getChildCount(Object parent) {
        Node node = (Node) parent;
        return node.getChildren().getNodesCount();
    }

    @Override
    public int getIndexOfChild(Object parent, Object child) {
//        if (parent == null || child == null) {
//            return -1;
//        }
//        Node par = (Node) parent;
//        Node ch = (Node) child;
//        Node children[] = par.getChildren().getNodes();
//        for (int i = 0; i < children.length; i++) {
//            Node children1 = children[i];
//            if (children1.equals(ch)) {
//                return i;
//            }
//        }
//        return -1;
        return -1;
    }

    @Override
    public Object getRoot() {
        return root;
    }

    @Override
    public boolean isLeaf(Object node) {
        return false;//((Node) node).getChildren().getNodesCount() == 0;
    }

    @Override
    public void valueForPathChanged(javax.swing.tree.TreePath path, Object newValue) {
        //do nothing
    }

    @Override
    public void addTreeModelListener(javax.swing.event.TreeModelListener l) {
        //do nothing
    }

    @Override
    public void removeTreeModelListener(javax.swing.event.TreeModelListener l) {
        //do nothing
    }
}

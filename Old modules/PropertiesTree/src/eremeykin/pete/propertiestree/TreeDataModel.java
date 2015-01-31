/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eremeykin.pete.propertiestree;

import javax.swing.tree.TreeModel;
import org.netbeans.swing.outline.RowModel;

/**
 *
 * @author eremeykin
 */
public class TreeDataModel {

    public static class PropertiesModel implements TreeModel {

        private Node root;

        public PropertiesModel(Node root) {
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

    public static class TreeTableRowModel implements RowModel {

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
            Node n = (Node) node;
            switch (column) {
                case 0:
                    return n.getDescription();
                case 1:
                    return n.getValue();
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
        public void setValueFor(Object node, int column, Object value) {
            //do nothing for now
            Node n = (Node) node;
            n.setValue(value.toString());
        }

    }
}

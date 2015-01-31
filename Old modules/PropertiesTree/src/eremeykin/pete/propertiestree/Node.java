/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eremeykin.pete.propertiestree;

import ansexp.toolkit.DataSource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.DefaultCellEditor;

/**
 *
 * @author eremeykin
 */
public class Node implements DataSource {

    private final String name;
    private String value;
    private final String description;
    private final DefaultCellEditor editor;
    private final String id;

    private final List<Node> children = new ArrayList<>();

    public Node(String name, String description, String value, String id, DefaultCellEditor editor) {
        this.name = name;
        this.description = description;
        this.value = value;
        this.editor = editor;
        this.id = id;
    }

    public List<Node> getChildren() {
        return children;
    }

    public String getDescription() {
        return this.description;
    }

    public String getValue() {
        return this.value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return getName();
    }

    public boolean isLeaf() {
        return children.isEmpty();
    }

    /**
     * Generates a string representation of the node in the tree view
     *
     * @param node
     * @param count
     * @return string representation
     */
    public static String printChildren(Node node, int count) {
        class Helper {

            private String printSpace(int count) {
                String result = "";
                for (int i = 0; i < count; i++) {
                    result += "  ";
                }
                return result;
            }
        }
        String result = node.getName() + "(" + node.value + ")";
        count++;
        for (Node n : node.getChildren()) {
            result += "\n " + new Helper().printSpace(count) + printChildren(n, count);
        }
        return result;
    }

    /**
     * Copies all the editors of the nodes into the specified ArrayList
     *
     * @param res result ArrayList with all the editors
     * @param node the root Node
     * @deprecated use {@link #getEditor(Node node)} instead. 
     */
    @Deprecated
    public static void getEditors(List<DefaultCellEditor> res, Node node) {
        for (Node n : node.children) {
            res.add(n.editor);
            getEditors(res, n);
        }
    }
    
    public DefaultCellEditor getEditor() {
        return this.editor;
        }
    

    /**
     * Copies all the nodes into the specified ArrayList
     *
     * @param res result ArrayList with all the Nodes
     * @param root the root Node
     */
    private static void getNodes(List<Node> res, Node root) {
        for (Node n : root.children) {
            res.add(n);
            getNodes(res, n);
        }
    }

    private Map<String, String> spreadToMap() {
        Map<String, String> result = new HashMap<>();
        List<Node> nodes = new ArrayList<>();
        getNodes(nodes, this);
        for (Node node : nodes) {
            result.put(node.id, node.value);
        }
        return result;
    }

    @Override
    public String getValueById(String id) {
        Map<String, String> idMap = this.spreadToMap();
        return idMap.get(id);
    }

    @Override
    public void setValueById(String id, String value) {
        List<Node> nodes = new ArrayList<>();
        getNodes(nodes, this);
        for (Node node : nodes) {
            if (node != null && node.id != null && node.id.equals(id)) {
                node.value = value;
            }
        }
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }
}

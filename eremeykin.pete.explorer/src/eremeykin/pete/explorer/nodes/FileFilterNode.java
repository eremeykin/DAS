/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eremeykin.pete.explorer.nodes;

import java.util.Arrays;
import java.util.List;
import javax.swing.Action;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.ChildFactory;
import org.openide.nodes.Children;
import org.openide.nodes.Node;

/**
 *
 * @author Pete
 */

public class FileFilterNode extends AbstractNode/*FilterNode */ {

    private String name;

    public FileFilterNode(Node original) {
        super(Children.create(new GeneralizingChildFactory(original), true));
    }

    public FileFilterNode(Item item) {
        super(Children.create(new TestChildFactory(), true));
    }

    @Override
    public String getHtmlDisplayName() {
        return "<b>Model " + name + "</b>";
    }

    @Override
    public Action[] getActions(boolean context) {
        return new Action[0];
    }

}

//class FileChildren extends FilterNode.Children {
//
//    public FileChildren(Node or) {
//        super(or);
////        setKeys(keys);
//    }
//
//    @Override
//    protected Node[] createNodes(Node key) {
//        Node[] nodes = super.createNodes(key);
//        ArrayList<Node> toReturn = new ArrayList<Node>();
//        toReturn.add(new )
//        return nodes;
//    }
//
//}

class TestChildFactory extends ChildFactory<Item> {

    @Override
    protected boolean createKeys(List<Item> toPopulate) {
        Item[] objs = new Item[5];
        for (int i = 0; i < objs.length; i++) {
            objs[i] = new Item();
        }
        toPopulate.addAll(Arrays.asList(objs));
        return true;
    }

    @Override
    protected Node createNodeForKey(Item key) {
        return new FileFilterNode(key);
    }
}

class GeneralizingChildFactory extends ChildFactory<Node> {

    private final Node original;

    public GeneralizingChildFactory(Node node) {
        this.original = node;
    }

    @Override
    protected Node[] createNodesForKey(Node key) {
        return new Node[]{key.cloneNode()};
    }

    @Override
    protected boolean createKeys(List<Node> toPopulate) {
        toPopulate.addAll(Arrays.asList(original.getChildren().getNodes(true)));
        toPopulate.add(new FileFilterNode(new Item()));
        return true;
    }
}

class Item{
}


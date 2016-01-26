/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eremeykin.pete.explorer.nodes;

import java.util.List;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.ChildFactory;
import org.openide.nodes.Children;
import org.openide.nodes.Node;

/**
 *
 * @author Pete
 */
public class RootNode extends AbstractNode {

    public RootNode(Node root) {
        super(Children.create(new RootChildFactory(root), true));
    }

    private static class RootChildFactory extends ChildFactory<Node> {

        private final Node root;

        private RootChildFactory(Node root) {
            this.root = root;
        }

        @Override
        protected boolean createKeys(List<Node> toPopulate) {
            Node[] nodes = root.getChildren().getNodes(true);
            for (Node n : nodes) {
                toPopulate.add(n.cloneNode());
            }
            return true;
        }
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eremeykin.pete.explorer.nodes;

import java.util.ArrayList;
import java.util.List;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.ChildFactory;
import org.openide.nodes.Children;
import org.openide.nodes.Node;

/**
 *
 * @author Pete
 */
public class ReportNode extends AbstractNode {

    private final ReportNodeChildFactory factory;
    
    private ReportNode(Builder builder) {
        super(Children.create(builder.factory, true));
        factory = builder.factory;
    }

    @Override
    public String getDisplayName() {
        return "Report"; //To change body of generated methods, choose Tools | Templates.
    }

    public void refresh(){
        factory.refresh();
    }
    
    private static class ReportNodeChildFactory extends ChildFactory<Node> {

        private final List<? extends Node> toPopulate;

        public ReportNodeChildFactory(List<? extends Node> childrenList) {
            this.toPopulate = childrenList;
        }
        
        @Override
        protected Node createNodeForKey(Node key) {
            return key.cloneNode();
        }

        @Override
        protected boolean createKeys(List<Node> toPopulate) {
            toPopulate.addAll(this.toPopulate);
            return true;
        }
        
        public void refresh(){
            this.refresh(true);
        }

    }

    public static class Builder {

        private ReportNodeChildFactory factory;
        
        public Builder(List<? extends Node> toPopulate) {
            factory = new ReportNodeChildFactory(toPopulate);
        }
        
        public ReportNode build(){
            return new ReportNode(this);
        }

    }
}

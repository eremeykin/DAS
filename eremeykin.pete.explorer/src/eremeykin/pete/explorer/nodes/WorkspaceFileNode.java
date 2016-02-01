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
public class WorkspaceFileNode extends AbstractNode{

    private final WorkspaceFileNodeChildFactory factory;
    private final Node workspaceRootNode;
    private final ReportNode reportNode;
    
    private WorkspaceFileNode(Builder builder) {
        super(Children.create(builder.factory, true));
        factory = builder.factory;
        workspaceRootNode = builder.workspaceRootNode;
        reportNode = builder.reportNode;
    }

    @Override
    public String getDisplayName() {
        return workspaceRootNode.getDisplayName(); //To change body of generated methods, choose Tools | Templates.
    }

    public void refresh() {
        reportNode.refresh();
    }
    
    
    
    private static class WorkspaceFileNodeChildFactory extends ChildFactory<Node>{

        private final Node workspaceRootNode;
        private final Node reportNode;
        
        private WorkspaceFileNodeChildFactory(Node workspaceRootNode, Node reportNode){
            this.workspaceRootNode = workspaceRootNode;
            this.reportNode = reportNode;
        }

        @Override
        protected Node createNodeForKey(Node key) {
            return key.cloneNode();
        }
                
        @Override
        protected boolean createKeys(List toPopulate) {
            Node[] nodes = workspaceRootNode.getChildren().getNodes(true);
            for (Node n : nodes) {
                toPopulate.add(n.cloneNode());
            }
            toPopulate.add(reportNode);
            return true;
        }
                
    }
    
    public static class Builder{

        private final WorkspaceFileNodeChildFactory factory;
        private final Node workspaceRootNode;
        private final ReportNode reportNode;
        
        public Builder(Node workspaceRootNode, List<? extends Node> reportItems) {
            this.workspaceRootNode = workspaceRootNode;
            this.reportNode = new ReportNode.Builder(reportItems).build();//reportNode;
            factory = new WorkspaceFileNodeChildFactory(workspaceRootNode, reportNode);
        }
        
        public WorkspaceFileNode build(){
            return new WorkspaceFileNode(this);
        }
    }
}

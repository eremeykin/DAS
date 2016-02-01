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
public class ReportItemNode extends AbstractNode{

    static int c = 0;
    
    public ReportItemNode(Children children) {
        super(children);
    }

    @Override
    public String getDisplayName() {
        c++;
        return "Node "+c; //To change body of generated methods, choose Tools | Templates.
    }
    
    
}

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

    private final Object obj;

   
    public ReportItemNode(Object obj) {
        super(Children.LEAF);
        this.obj = obj;
    }

    @Override
    public String getDisplayName() {
        return obj.toString(); //To change body of generated methods, choose Tools | Templates.
    }
    
    
}

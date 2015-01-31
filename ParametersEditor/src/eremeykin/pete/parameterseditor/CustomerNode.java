/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eremeykin.pete.parameterseditor;

import java.beans.IntrospectionException;
import org.openide.nodes.BeanNode;

/**
 *
 * @author Pete
 */
public class CustomerNode extends BeanNode {

    public CustomerNode(Customer bean) throws IntrospectionException {
        super(bean);
        setDisplayName(bean.getName());
        setShortDescription(bean.getCity().toString());
    }
    
}
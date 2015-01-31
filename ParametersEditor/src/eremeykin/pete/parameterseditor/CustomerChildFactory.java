/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eremeykin.pete.parameterseditor;

import java.beans.IntrospectionException;
import java.util.List;
import org.openide.nodes.ChildFactory;
import org.openide.nodes.Node;
import org.openide.util.Exceptions;

/**
 *
 * @author Pete
 */
public class CustomerChildFactory extends ChildFactory<Customer> {

    @Override
    protected boolean createKeys(List<Customer> toPopulate) {
        Customer c1 = new Customer("Tom", "London", 23, true);
        Customer c2 = new Customer("Dick", "New York", 27, false);
        Customer c3 = new Customer("Harry", "Amsterdam", 26, true);
        toPopulate.add(c1);
        toPopulate.add(c2);
        toPopulate.add(c3);
        return true;
    }

    @Override
    protected Node createNodeForKey(Customer key) {
        CustomerNode node = null;
        try {
            node = new CustomerNode(key);
        } catch (IntrospectionException ex) {
            Exceptions.printStackTrace(ex);
        }
        return node;
    }

}
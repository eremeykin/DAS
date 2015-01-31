/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eremeykin.pete.parameterseditor;

import eremeykin.pete.parametersapi.Parameter;
import java.util.Arrays;
import java.util.List;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.ChildFactory;
import org.openide.nodes.Children;
import org.openide.nodes.Node;
import org.openide.util.lookup.Lookups;

/**
 *
 * @author Pete
 */
class ParameterChildFactory extends ChildFactory<Parameter> {

    @Override
    protected boolean createKeys(List toPopulate) {
        Parameter[] objs = new Parameter[5];
        for (int i = 0; i < objs.length; i++) {
            objs[i] = new Parameter("testID","testName","testDescriprion","testValue");
        }
        toPopulate.addAll(Arrays.asList(objs));
        return true;
    }

    @Override
    protected Node createNodeForKey(Parameter key) {
        Node result = new AbstractNode(
                Children.create(new ParameterChildFactory(), true),
                Lookups.singleton(key));
        result.setDisplayName(key.getName());
        return result;
    }

}

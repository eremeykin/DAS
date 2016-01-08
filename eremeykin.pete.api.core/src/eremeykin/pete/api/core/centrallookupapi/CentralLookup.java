/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eremeykin.pete.api.core.centrallookupapi;

import org.openide.util.lookup.AbstractLookup;
import org.openide.util.lookup.InstanceContent;

/**
 *
 * @author Pete
 */
public class CentralLookup extends AbstractLookup {

    private static final InstanceContent content = new InstanceContent();
    private static final CentralLookup def = new CentralLookup(content);

    //noninstantiability
    private CentralLookup(InstanceContent content) {
        super(content);
    }


    public void add(Object instance) {
        content.add(instance);
    }


    public void remove(Object instance) {
        content.remove(instance);
    }
   
    public static CentralLookup getDefault() {
        return def;
    }

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eremeykin.pete.api.model;

import java.io.Reader;
import java.net.URL;
import java.util.EventObject;

/**
 *
 * @author Pete
 */
public class ModelFileChangedEvent extends EventObject {

    public ModelFileChangedEvent(Object source) {
        super(source);
    }
}

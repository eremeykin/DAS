/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eremeykin.pete.api.model;

import java.util.EventObject;

/**
 *
 * @author Pete
 */
public class ModelChangedEvent extends ModelEvent {

    ModelParameter parameterSource;

    public ModelChangedEvent(ModelParameter source) {
        super(source);
        this.parameterSource = source;
    }

    public ModelParameter getParameterSource() {
        return parameterSource;
    }

}

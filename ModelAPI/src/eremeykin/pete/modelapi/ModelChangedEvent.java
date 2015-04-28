/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eremeykin.pete.modelapi;

import java.util.EventObject;

/**
 *
 * @author Pete
 */
public class ModelChangedEvent extends EventObject {

    Parameter parameterSource;

    public ModelChangedEvent(Parameter source) {
        super(source);
        this.parameterSource = source;
    }

    public Parameter getParameterSource() {
        return parameterSource;
    }

}

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
public class ModelParameterChangedEvent extends EventObject {
   
    private ModelParameter parameterSource;

    
    public ModelParameterChangedEvent(ModelParameter p){
        super(p);
        this.parameterSource=p;
    }
    
    public ModelParameter getParameterSource() {
        return parameterSource;
    }
    

}

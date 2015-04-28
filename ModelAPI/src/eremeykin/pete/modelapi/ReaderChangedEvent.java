/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eremeykin.pete.modelapi;

import java.io.Reader;
import java.util.EventObject;

/**
 *
 * @author Pete
 */
public class ReaderChangedEvent extends EventObject{

    public ReaderChangedEvent(Reader source) {
        super(source);
    }    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eremeykin.pete.modelloader;

import eremeykin.pete.modelapi.Model;
import java.io.File;

/**
 *
 * @author eremeykin@gmail.com
 */
public class AbstractModelLoader implements ModelLoader {

    File modelFile;
    
    @Override
    public Model load() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}

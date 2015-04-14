/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eremeykin.pete.modelapi;

import java.io.File;
import java.io.Reader;
import java.sql.SQLException;

/**
 *
 * @author Pete
 */
public class Model {

    private Parameter root;
    private Reader objReader;

    public Model(Parameter root, Reader objReader) {
        this.root = root;
        this.objReader = objReader;
    }

    public Parameter getRoot() {
        return root;
    }

    public Reader getObjReader() {
        return objReader;
    }
    

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eremeykin.pete.parameterseditor;

import eremeykin.pete.parameterseditor.ModelLoader.LoadingException;
import java.io.File;
import java.sql.SQLException;

/**
 *
 * @author Pete
 */
public class Model {

    private final Parameter root;

    public Model(File file) throws LoadingException {
        try {
            root = new ModelLoader(file).load();
        } catch (ClassNotFoundException | SQLException ex) {
            ModelLoader.LoadingException lex = new ModelLoader.LoadingException();
            lex.initCause(ex);
            throw lex;
        }
    }

    public Parameter getRoot() {
        return root;
    }

}

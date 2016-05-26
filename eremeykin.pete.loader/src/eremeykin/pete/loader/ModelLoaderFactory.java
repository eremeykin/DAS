/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eremeykin.pete.loader;


import java.io.File;
import java.sql.SQLException;

/**
 *
 * @author Pete
 */
public class ModelLoaderFactory {

    private static ModelLoaderFactory INSTANCE = new ModelLoaderFactory();

    private ModelLoaderFactory() {
    }

    public static ModelLoaderFactory getInstance() {
        return INSTANCE;
    }

    public ModelLoader createModelDAO(File repository) throws ClassNotFoundException, SQLException {
        ModelLoader loader = null;
        if (repository.getName().endsWith("sqlite")) {
            loader = new DBModelLoader(repository);
        } else if (repository.getName().endsWith("xlsx")) {
            loader = new XlsxModelLoader(repository);
        }
        return loader;
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eremeykin.pete.loader;

import eremeykin.pete.api.core.logger.Logger;
import eremeykin.pete.api.core.logger.LoggerManager;
import eremeykin.pete.api.core.workspace.WorkspaceManager;
import eremeykin.pete.api.model.Model;
import eremeykin.pete.loader.dao.DaoException;
import eremeykin.pete.loader.dao.ModelDao;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

/**
 *
 * @author eremeykin@gmail.com
 */
public abstract class AbstractModelLoader implements ModelLoader {

    File modelFile;
    private static final String SCRIPT_FILE_NAME = "script.py";
    private static final String MODEL_FILE_NAME = "model.obj";
    private static final Logger LOGGER = LoggerManager.getLogger(AbstractModelLoader.class);

    protected abstract ModelDao getDao();

    @Override
    public Model load() throws DaoException {
        try {
            Model model = getDao().load();
            model.setModelFile(getObjModelFile());
            model.setScriptFile(getScriptFile());
            LOGGER.info("Model has been successfully loaded.");
            return model;
        } catch (FileNotFoundException ex) {
            throw new DaoException("Can't set model file", ex);
        }
    }

    private void printStringToFile(String str, File file) throws FileNotFoundException {
        PrintWriter out = new PrintWriter(file);
        out.print(str);
        out.close();
    }

    private File getObjModelFile() throws DaoException, FileNotFoundException {
        File resultFile = new File(WorkspaceManager.INSTANCE.getWorkspace().getAbsolutePath() + "\\" + MODEL_FILE_NAME);
        printStringToFile(getDao().getObjModel(), resultFile);
        return resultFile;
    }

    private File getScriptFile() throws DaoException, FileNotFoundException {
        File resultFile = new File(WorkspaceManager.INSTANCE.getWorkspace().getAbsolutePath() + "\\" + SCRIPT_FILE_NAME);
        printStringToFile(getDao().getScript(), resultFile);
        return resultFile;
    }

}

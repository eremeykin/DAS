/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eremeykin.pete.modelloader;

import eremeykin.pete.coreapi.loggerapi.Logger;
import eremeykin.pete.coreapi.loggerapi.LoggerManager;
import eremeykin.pete.coreapi.workspace.WorkspaceManager;
import eremeykin.pete.modelapi.Model;
import eremeykin.pete.modelloader.dao.DaoException;
import eremeykin.pete.modelloader.dao.ModelDao;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

/**
 *
 * @author eremeykin@gmail.com
 */
public abstract class AbstractModelLoader implements ModelLoader {

    File modelFile;
    ModelDao dao = getDao();
    private static final String SCRIPT_FILE_NAME = "script.py";
    private static final String MODEL_FILE_NAME = "model.obj";
    private static final Logger LOGGER = LoggerManager.getLogger(AbstractModelLoader.class);

    protected abstract ModelDao getDao();

    @Override
    public Model load() throws DaoException {
        try {
            Model model = dao.load();
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
        printStringToFile(dao.getObjModel(), resultFile);
        return resultFile;
    }

    private File getScriptFile() throws DaoException, FileNotFoundException {
        File resultFile = new File(WorkspaceManager.INSTANCE.getWorkspace().getAbsolutePath() + "\\" + SCRIPT_FILE_NAME);
        printStringToFile(dao.getScript(), resultFile);
        return resultFile;
    }

}

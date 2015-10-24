package eremeykin.pete.modelloader;

import eremeykin.pete.coreapi.loggerapi.Logger;
import eremeykin.pete.coreapi.loggerapi.LoggerManager;
import eremeykin.pete.coreapi.workspace.WorkspaceManager;
import eremeykin.pete.modelapi.Model;
import eremeykin.pete.modelloader.dao.DaoException;
import eremeykin.pete.modelloader.dbdao.DbModelDao;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.sql.*;
import org.openide.util.Exceptions;

public class DBModelLoader implements ModelLoader {

    private static final Logger LOGGER = LoggerManager.getLogger(DBModelLoader.class);
    private final File mFile;
    private static final String SCRIPT_FILE_NAME = "script.py";
    private static final String MODEL_FILE_NAME = "model.obj";
    private final DbModelDao dao;

    public DBModelLoader(File file) throws ClassNotFoundException, SQLException {
        mFile = file;
        dao = new DbModelDao(mFile);
    }

    public Model load() throws DaoException {
        try {
            Model model = dao.load();
            model.setModelFile(getObjModelFile());
            LOGGER.info("Model " + mFile.getAbsolutePath() + " has been successfully loaded.");
            return model;
        } catch (FileNotFoundException ex) {
            throw  new DaoException("Can't set model file", ex);
        }
    }

    private void printStringToFile(String str, File file) throws FileNotFoundException {
        PrintWriter out = new PrintWriter(file);
        out.print(str);
        out.close();
    }

    public File getObjModelFile() throws DaoException, FileNotFoundException {
        File resultFile = new File(WorkspaceManager.INSTANCE.getWorkspace().getAbsolutePath() + "\\" + MODEL_FILE_NAME);
        printStringToFile(dao.getObjModel(), resultFile);
        return resultFile;
    }

    public File getScriptFile() throws DaoException, FileNotFoundException {
        File resultFile = new File(WorkspaceManager.INSTANCE.getWorkspace().getAbsolutePath() + "\\" + SCRIPT_FILE_NAME);
        printStringToFile(dao.getScript(), resultFile);
        return resultFile;
    }
}

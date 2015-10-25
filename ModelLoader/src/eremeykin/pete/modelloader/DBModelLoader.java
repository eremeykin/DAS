package eremeykin.pete.modelloader;

import eremeykin.pete.modelloader.dao.ModelDao;
import eremeykin.pete.modelloader.dbdao.DbModelDao;
import java.io.File;
import java.sql.*;

public class DBModelLoader extends AbstractModelLoader {

    private final File mFile;


    public DBModelLoader(File file) throws ClassNotFoundException, SQLException {
        mFile = file;
    }

    @Override
    protected ModelDao getDao() {
        return new DbModelDao(mFile);
    }


}

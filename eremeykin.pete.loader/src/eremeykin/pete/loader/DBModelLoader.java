package eremeykin.pete.loader;

import eremeykin.pete.loader.dao.ModelDao;
import eremeykin.pete.loader.dbdao.DbModelDao;
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

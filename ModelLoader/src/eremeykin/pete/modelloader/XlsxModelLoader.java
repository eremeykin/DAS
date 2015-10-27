/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eremeykin.pete.modelloader;

import eremeykin.pete.modelloader.dao.ModelDao;
import eremeykin.pete.modelloader.xlsxdao.XlsxModelDao;
import java.io.File;

/**
 *
 * @author eremeykin@gmail.com
 */
public class XlsxModelLoader extends AbstractModelLoader {

    private final File source;

    public XlsxModelLoader(File source) {
        this.source = source;
    }

    @Override
    protected ModelDao getDao() {
        return new XlsxModelDao(source);
    }

}

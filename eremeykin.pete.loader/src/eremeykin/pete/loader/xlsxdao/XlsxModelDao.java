/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eremeykin.pete.loader.xlsxdao;

import eremeykin.pete.loader.dao.AbstractModelDao;
import eremeykin.pete.loader.dao.DaoException;
import eremeykin.pete.loader.dao.ModelParameterDao;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 *
 * @author eremeykin@gmail.com
 */
public class XlsxModelDao extends AbstractModelDao {

    private final File source;

    public XlsxModelDao(File source) {
        this.source = source;
    }

    private String getTableContent(String table) throws DaoException {
        FileInputStream excelFile = null;
        try {
            excelFile = new FileInputStream(source);
            XSSFWorkbook wb = new XSSFWorkbook(excelFile);
            XSSFSheet sheet = wb.getSheet(table);
            return sheet.getRow(1).getCell(0).getStringCellValue();
        } catch (FileNotFoundException ex) {
            throw new DaoException("Can't find excel file " + source, ex);
        } catch (IOException ex) {
            throw new DaoException("Can't open excel file " + source, ex);
        } finally {
            try {
                if (excelFile != null) {
                    excelFile.close();
                }
            } catch (IOException ex) {
                throw new DaoException("Can't close excel file " + source, ex);
            }
        }
    }

    @Override
    protected ModelParameterDao getDao() {
        return new XlsxModelParameterDao(source);
    }

    @Override
    public String getScript() throws DaoException {
        return getTableContent(XlsxConstants.SCRIPT_SHEET);
    }

    @Override
    public String getObjModel() throws DaoException {
        return getTableContent(XlsxConstants.MODEL_SHEET);
    }

}

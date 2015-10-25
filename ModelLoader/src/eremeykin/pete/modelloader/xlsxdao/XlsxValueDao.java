/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eremeykin.pete.modelloader.xlsxdao;

import eremeykin.pete.modelapi.Value;
import eremeykin.pete.modelloader.dao.DaoException;
import eremeykin.pete.modelloader.dao.ValueDao;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.List;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openide.util.Exceptions;

/**
 *
 * @author eremeykin@gmail.com
 */
public class XlsxValueDao implements ValueDao {

    private final File source;

    public XlsxValueDao(File source) {
        this.source = source;
    }

    @Override
    public List<Value> getAllSuitable(String table, String column, String key) throws DaoException {
        InputStream excelFile = null;
        try {
            excelFile = new FileInputStream(source);
            XSSFWorkbook wb = new XSSFWorkbook(excelFile);
//            XSSFWorkbook test = new XSSFWorkbook();
            XSSFSheet sheet = wb.getSheet(table);
//            XSSFRow row;
//            XSSFCell cell;
            Iterator rows = sheet.rowIterator();
            for (Row row : sheet) {
                XSSFRow xssfRow = (XSSFRow)row;
                xssfRow.getCell(cellnum)
            }

        } catch (FileNotFoundException ex) {
            Exceptions.printStackTrace(ex);
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        } finally {
            try {
                excelFile.close();
            } catch (IOException ex) {
                Exceptions.printStackTrace(ex);
            }
        }
    }

}

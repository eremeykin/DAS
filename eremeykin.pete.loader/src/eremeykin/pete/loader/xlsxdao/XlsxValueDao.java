/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eremeykin.pete.loader.xlsxdao;

import eremeykin.pete.api.model.Value;
import eremeykin.pete.loader.dao.DaoException;
import eremeykin.pete.loader.dao.ValueDao;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import org.apache.poi.xssf.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

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
            XSSFSheet sheet = wb.getSheet(table);
            XlsxResultSet rs = new XlsxResultSet(sheet);
            List<Value> valuesList = new ArrayList<>();
            while (rs.next()) {
                String k = rs.getString(key);//See sql alials
                String v = rs.getString(column);//See sql alials
                Value value = new Value(k, v);
                valuesList.add(value);
            }
            return valuesList;
        } catch (FileNotFoundException ex) {
            throw new DaoException("Can't find excel file", ex);
        } catch (IOException ex) {
            throw new DaoException("Can't open excel file", ex);
        } finally {
            try {
                if (excelFile != null) {
                    excelFile.close();
                }
            } catch (IOException ex) {
                throw new DaoException("Can't close excel file", ex);
            }
        }
    }

}

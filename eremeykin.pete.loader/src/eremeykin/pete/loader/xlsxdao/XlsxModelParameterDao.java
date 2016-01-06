/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eremeykin.pete.loader.xlsxdao;

import eremeykin.pete.api.model.ModelParameter;
import eremeykin.pete.loader.dao.AbstractModelParameterDao;
import eremeykin.pete.loader.dao.DaoException;
import eremeykin.pete.loader.dao.ValueDao;
import eremeykin.pete.loader.dao.entry.ParameterEntry;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openide.util.Exceptions;

/**
 *
 * @author eremeykin@gmail.com
 */
public class XlsxModelParameterDao extends AbstractModelParameterDao {

    private final File source;

    public XlsxModelParameterDao(File source) {
        this.source = source;
    }

    @Override
    protected Map<ParameterEntry, ModelParameter> getORMap() throws DaoException {
        FileInputStream excelFile = null;
        try {
            excelFile = new FileInputStream(source);
            XSSFWorkbook wb = new XSSFWorkbook(excelFile);
            XSSFSheet sheet = wb.getSheet(XlsxConstants.PARAMETERS_SHEET);
            Map<ParameterEntry, ModelParameter> orMap = new HashMap<>();
            XlsxResultSet rs = new XlsxResultSet(sheet);
            while (rs.next()) {
                String id = rs.getString(XlsxConstants.ID_COLUMN);
                String name = rs.getString(XlsxConstants.NAME_COLUMN);
                String parent = rs.getString(XlsxConstants.PARENT_COLUMN);
                String scriptArg = rs.getString(XlsxConstants.SCRIPTARG_COLUMN);
                String value = rs.getString(XlsxConstants.VALUE_COLUMN);
                String comment = rs.getString(XlsxConstants.COMMENT_COLUMN);
                String editorType = rs.getString(XlsxConstants.EDITOR_TYPE_COLUMN);
                String master = rs.getString(XlsxConstants.MASTER_COLUMN);
                String editorTable = rs.getString(XlsxConstants.EDITOR_TABLE_COLUMN);
                String editorColumn = rs.getString(XlsxConstants.EDITOR_COLUMN_COLUMN);
                ParameterEntry pEntry = new ParameterEntry(id, name, parent, scriptArg, value, comment, editorType, master, editorTable, editorColumn);
                ModelParameter parameter = buildModelParameter(pEntry);
                orMap.put(pEntry, parameter);
            }
            return orMap;
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
                throw new DaoException("Can't close file " + source, ex);
            }
        }
    }

    @Override
    protected ValueDao getValueDao() {
        return new XlsxValueDao(source);
    }

}

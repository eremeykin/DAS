/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eremeykin.pete.loader.xlsxdao;

import java.util.HashMap;
import java.util.Map;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;

/**
 *
 * @author eremeykin@gmail.com
 */
public class XlsxResultSet {
    
    final Map<String, Integer> colNameMap = new HashMap<>();
    int pointer = 0;
    XSSFSheet sheet;
    XSSFRow currRow;
    
    public XlsxResultSet(XSSFSheet sheet) {
        this.sheet = sheet;
        currRow = sheet.getRow(0);
        for (Cell cell : currRow) {
            colNameMap.put(cell.getStringCellValue(), cell.getColumnIndex());
        }
    }
    
    boolean next() {
        pointer++;
        currRow = sheet.getRow(pointer);
        return sheet.getRow(pointer) != null;
    }
    
    String getString(String column) {
        int c = colNameMap.get(column);
        XSSFCell cell = currRow.getCell(c);
        if (cell == null) {
            return null;
        }
        String result = cell.toString();
        try {
            Double d = Double.valueOf(result);
            if (result.endsWith(".0")) {
                result = Integer.toString(d.intValue());
            }
        } catch (NumberFormatException ex) {
        }
        return result;
    }
    
}

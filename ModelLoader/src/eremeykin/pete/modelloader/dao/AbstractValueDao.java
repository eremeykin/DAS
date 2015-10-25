/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eremeykin.pete.modelloader.dao;

import eremeykin.pete.modelapi.Value;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.openide.util.Exceptions;

/**
 *
 * @author eremeykin@gmail.com
 */
public abstract class AbstractValueDao implements ValueDao {

    /*The first cell should be kay and second - value*/
    protected List<Value> extractFromResultSet(ResultSet rs) throws DaoException{
        try {
            List<Value> valuesList = new ArrayList<>();
            while (rs.next()) {
                String k = rs.getString(1);//See sql alials
                String v = rs.getString(2);//See sql alials
                Value value = new Value(k, v);
                valuesList.add(value);
            }
            return valuesList;
        } catch (SQLException ex) {
            throw new DaoException("Can't extract values from ResultSet", ex);
        }
    }

}

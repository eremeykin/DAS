/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eremeykin.pete.loader.dao;

import eremeykin.pete.api.model.Value;
import java.util.List;

/**
 *
 * @author eremeykin@gmail.com
 */
public interface ValueDao {

    List<Value> getAllSuitable(String table, String column, String key) throws DaoException;
}

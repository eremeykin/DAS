/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eremeykin.pete.modelloader.dao;

import eremeykin.pete.modelapi.Value;
import java.util.List;

/**
 *
 * @author eremeykin@gmail.com
 */
public interface ValueDao {

    List<Value> getAllSuitable(String table, String column, String key) throws DaoException;
}
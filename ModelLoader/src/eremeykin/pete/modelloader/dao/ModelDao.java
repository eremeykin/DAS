/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eremeykin.pete.modelloader.dao;

import eremeykin.pete.modelapi.Model;
import java.io.File;
import java.sql.Connection;

/**
 *
 * @author eremeykin@gmail.com
 */
public interface ModelDao {

    Model load() throws DaoException;

    String getScript() throws DaoException;
    
    String getObjModel() throws DaoException;
}

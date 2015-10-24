/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eremeykin.pete.modelloader.dao;

import eremeykin.pete.modelapi.ModelParameter;
import java.util.List;

/**
 *
 * @author eremeykin@gmail.com
 */
public interface ModelParameterDao {

    ModelParameter getRoot() throws DaoException;

    List<ModelParameter> getAll() throws DaoException;
}

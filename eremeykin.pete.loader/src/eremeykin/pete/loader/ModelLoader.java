/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eremeykin.pete.loader;

import eremeykin.pete.api.model.Model;
import eremeykin.pete.loader.dao.DaoException;

/**
 *
 * @author eremeykin@gmail.com
 */
public interface ModelLoader {

    public Model load() throws DaoException;
}

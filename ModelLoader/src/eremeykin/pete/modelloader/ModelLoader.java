/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eremeykin.pete.modelloader;

import eremeykin.pete.modelapi.Model;
import eremeykin.pete.modelloader.dao.DaoException;

/**
 *
 * @author eremeykin@gmail.com
 */
public interface ModelLoader {

    public Model load() throws DaoException;
}

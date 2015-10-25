/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eremeykin.pete.modelloader.dao;

import eremeykin.pete.modelapi.Model;
import eremeykin.pete.modelapi.ModelParameter;

/**
 *
 * @author eremeykin@gmail.com
 */
public abstract class AbstractModelDao implements ModelDao {

    protected abstract ModelParameterDao getDao();
    
    @Override
    public Model load() throws DaoException {
        ModelParameterDao modelParameterDao = getDao();//new DbModelParameterDao(source);
        ModelParameter root = modelParameterDao.getRoot();
        Model model = new Model(root);
        // model should know if it's parameter changes
        for (ModelParameter parameter : modelParameterDao.getAll()) {
            parameter.addParameterChangedListener(model);
        }
        return model;
    }

}
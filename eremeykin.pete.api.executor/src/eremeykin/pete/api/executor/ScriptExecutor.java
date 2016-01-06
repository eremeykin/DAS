/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eremeykin.pete.api.executor;

import eremeykin.pete.api.model.Model;

/**
 *
 * @author Pete
 */
public interface ScriptExecutor {

    public void runScript(Model model, boolean refresh);
}

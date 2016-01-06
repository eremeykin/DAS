/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eremeykin.pete.api.core.loggerapi.defaultlog;

import eremeykin.pete.api.core.loggerapi.Logger;

/**
 *
 * @author Pete
 */
public class DefaultLogger implements Logger {

    @Override
    public void trace(Object message) {
        System.out.println(message);
    }

    @Override
    public void debug(Object message) {
        System.out.println(message);
    }

    @Override
    public void info(Object message) {
        System.out.println(message);
    }

    @Override
    public void warn(Object message) {
        System.out.println(message);
    }

    @Override
    public void error(Object message) {
        System.err.println(message);
    }

    @Override
    public void fatal(Object message) {
        System.err.println(message);
    }

}

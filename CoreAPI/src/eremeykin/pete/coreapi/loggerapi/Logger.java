/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eremeykin.pete.coreapi.loggerapi;

/**
 *
 * @author Pete
 */
public interface Logger {

    public void trace(Object message);

    public void debug(Object message);

    public void info(Object message);

    public void warn(Object message);

    public void error(Object message);

    public void fatal(Object message);

}

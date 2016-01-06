/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eremeykin.pete.logger;

import org.apache.logging.log4j.LogManager;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author Pete
 */
@ServiceProvider(service = eremeykin.pete.api.core.loggerapi.Logger.class)
public class Log4jLogger implements eremeykin.pete.api.core.loggerapi.Logger {

    private org.apache.logging.log4j.Logger log;

    public Log4jLogger() {
        this.log = LogManager.getRootLogger();
    }

    public Log4jLogger(org.apache.logging.log4j.Logger log) {
        this.log = log;
    }

    @Override
    public void trace(Object message) {
        log.trace(message);
    }

    @Override
    public void debug(Object message) {
        log.debug(message);
    }

    @Override
    public void info(Object message) {
        log.info(message);
    }

    @Override
    public void warn(Object message) {
        log.warn(message);
    }

    @Override
    public void error(Object message) {
        log.error(message);
    }

    @Override
    public void fatal(Object message) {
        log.fatal(message);
    }

}

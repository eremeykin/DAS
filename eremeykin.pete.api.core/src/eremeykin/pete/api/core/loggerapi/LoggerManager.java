/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eremeykin.pete.api.core.loggerapi;

import eremeykin.pete.api.core.loggerapi.defaultlog.DefaultLogger;
import java.util.Collection;
import org.openide.util.Lookup;

/**
 *
 * @author Pete
 */
public class LoggerManager {

    public static Logger getLogger(String name) {
        Logger log;
        Collection<? extends Logger> allLoggers = Lookup.getDefault().lookupAll(Logger.class);
        if (allLoggers.isEmpty()) {
            log = new DefaultLogger();
        } else {
            log = allLoggers.iterator().next();
        }
        return log;
    }

    public static Logger getLogger(Class c) {
        return LoggerManager.getLogger(c.getName());
    }
}

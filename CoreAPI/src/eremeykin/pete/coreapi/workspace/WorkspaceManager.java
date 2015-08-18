/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eremeykin.pete.coreapi.workspace;

import eremeykin.pete.coreapi.loggerapi.Logger;
import eremeykin.pete.coreapi.loggerapi.LoggerManager;
import java.io.File;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author Pete
 */
public class WorkspaceManager {

    private  File rootDirectory;
    private static final Logger LOGGER = LoggerManager.getLogger(WorkspaceManager.class);

    public static final WorkspaceManager INSTANCE = new WorkspaceManager();

    private WorkspaceManager() {
    }

    public void setWorkspace(File rootDirectory) {
        this.rootDirectory = rootDirectory;
        LOGGER.info("The directory \""+rootDirectory+"\" has been successfully set as a workspace.");
    }

    public File getWorkspace() {
        return rootDirectory;
    }

}

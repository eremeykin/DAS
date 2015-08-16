/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eremeykin.pete.coreapi.workspace;

import java.io.File;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author Pete
 */
public class WorkspaceManager {

    private File rootDirectory;

    public static final WorkspaceManager INSTANCE = new WorkspaceManager();

    private WorkspaceManager() {
    }

    public void setWorkspace(File rootDirectory) {
        this.rootDirectory = rootDirectory;
    }

    public File getWorkspace() {
        return rootDirectory;
    }

}

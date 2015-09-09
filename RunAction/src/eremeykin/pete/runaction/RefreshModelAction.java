/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eremeykin.pete.runaction;

import eremeykin.pete.coreapi.centrallookupapi.CentralLookup;
import eremeykin.pete.coreapi.loggerapi.Logger;
import eremeykin.pete.coreapi.loggerapi.LoggerManager;
import eremeykin.pete.modelapi.Model;
import eremeykin.pete.scriptrunnerapi.ScriptRunner;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import java.util.NoSuchElementException;
import javax.swing.JOptionPane;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionRegistration;
import org.openide.util.*;
import org.openide.util.NbBundle.Messages;
import org.openide.util.actions.CallableSystemAction;

@ActionID(
        category = "File",
        id = "eremeykin.pete.runaction.RunScript"
)
@ActionRegistration(
        iconInMenu = true,
        // as it is lazy loaded module, so it is required to set icon manually
        // iconBase = "res/2images16.png",
        displayName = "#CTL_RefreshScript",
        lazy = false
)
@ActionReferences({
    @ActionReference(path = "Menu/File", position = 1950),
    @ActionReference(path = "Toolbars/File", position = 100),
    @ActionReference(path = "Shortcuts", name = "D-R")
})
@Messages("CTL_RefreshScript=Run model")
public final class RefreshModelAction extends AbstractAction {

    private Lookup.Result modelResult = null;
    private static final Logger LOGGER = LoggerManager.getLogger(RefreshModelAction.class);

    public RefreshModelAction() {
        Lookup.Template template = new Lookup.Template(Model.class);
        CentralLookup cl = CentralLookup.getDefault();
        modelResult = cl.lookup(template);
        modelResult.addLookupListener(this);
        setEnabled(false);
        // as it is lazy loaded module, so it is required to set icon manually
        URL iconURL = getClass().getClassLoader().getResource("res/refresh.png");
        setIcon(new javax.swing.ImageIcon(iconURL));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        setRefresh(true);
        super.actionPerformed(e);
    }

    @Override
    public String getName() {
        return "Refresh model";
    }
}

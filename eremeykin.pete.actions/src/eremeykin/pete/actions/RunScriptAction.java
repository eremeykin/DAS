/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eremeykin.pete.actions;

import eremeykin.pete.api.core.centrallookupapi.CentralLookup;
import eremeykin.pete.api.model.Model;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionRegistration;
import org.openide.util.HelpCtx;
import org.openide.util.Lookup;
import org.openide.util.LookupEvent;
import org.openide.util.LookupListener;
import org.openide.util.NbBundle.Messages;
import org.openide.util.actions.CallableSystemAction;

@ActionID(
        category = "File",
        id = "eremeykin.pete.runaction.RunScript2"
)
@ActionRegistration(
        iconInMenu = true,
        //        iconBase = "res/run.png",
        displayName = "#CTL_RunScriptAction",
        lazy = false
)
@ActionReferences({
    @ActionReference(path = "Menu/File", position = 1875),
    @ActionReference(path = "Toolbars/File", position = 0),
    @ActionReference(path = "Shortcuts", name = "DS-R")
})
@Messages("CTL_RunScriptAction=RunScript")
public final class RunScriptAction extends AbstractAction {

    private Lookup.Result modelResult = null;

    public RunScriptAction() {
        Lookup.Template template = new Lookup.Template(Model.class);
        CentralLookup cl = CentralLookup.getDefault();
        modelResult = cl.lookup(template);
        modelResult.addLookupListener(this);
        setEnabled(false);
        // as it is lazy loaded module, so it is required to set icon manually
        URL iconURL = getClass().getClassLoader().getResource("res/run.png");
        setIcon(new javax.swing.ImageIcon(iconURL));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        setRefresh(false);
        super.actionPerformed(e);
    }

    @Override
    public String getName() {
        return "Run script";
    }
}

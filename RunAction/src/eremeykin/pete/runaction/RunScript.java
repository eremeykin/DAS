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
        //        iconBase = "res/images16.png",
        displayName = "#CTL_RunScript",
        lazy = false
)
@ActionReferences({
    @ActionReference(path = "Menu/File", position = 1950),
    @ActionReference(path = "Toolbars/File", position = 100),
    @ActionReference(path = "Shortcuts", name = "D-R")
})
@Messages("CTL_RunScript=Run model")
public final class RunScript extends CallableSystemAction implements LookupListener {

    private Lookup.Result modelResult = null;
    private static final Logger LOGGER = LoggerManager.getLogger(RunScript.class);

    public RunScript() {
        Lookup.Template template = new Lookup.Template(Model.class);
        CentralLookup cl = CentralLookup.getDefault();
        modelResult = cl.lookup(template);
        modelResult.addLookupListener(this);
        setEnabled(false);
        URL iconURL = getClass().getClassLoader().getResource("res/images16.png");
        setIcon(new javax.swing.ImageIcon(iconURL));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        ScriptRunner runner
                = Lookup.getDefault().lookup(ScriptRunner.class);
        if (runner == null) {
            LOGGER.warn("There is no launcher module!");
            JOptionPane.showMessageDialog(null, "There is no launcher module.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        Lookup.Template templateModel = new Lookup.Template(Model.class);
        CentralLookup cl = CentralLookup.getDefault();
        try {
            Model model = (Model) cl.lookup(templateModel).allInstances().iterator().next();
            runner.runScript(model, false);
        } catch (NoSuchElementException ex) {
            JOptionPane.showMessageDialog(null, "There is no opened model.", "Error", JOptionPane.ERROR_MESSAGE);
        }

    }

    @Override
    public boolean isEnabled() {
        Lookup.Template templateModel = new Lookup.Template(Model.class);
        CentralLookup cl = CentralLookup.getDefault();
        return cl.lookup(templateModel).allInstances().iterator().hasNext(); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void performAction() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getName() {
        return "Refresh model";
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public HelpCtx getHelpCtx() {
        return HelpCtx.DEFAULT_HELP;
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void resultChanged(LookupEvent le) {
        Lookup.Template templateModel = new Lookup.Template(Model.class);
        CentralLookup cl = CentralLookup.getDefault();
        if (cl.lookup(templateModel).allInstances().iterator().hasNext()) {
            setEnabled(true);
        } else {
            setEnabled(false);
        }
    }
}

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
import java.util.NoSuchElementException;
import javax.swing.JOptionPane;
import org.openide.util.HelpCtx;
import org.openide.util.Lookup;
import org.openide.util.LookupEvent;
import org.openide.util.LookupListener;
import org.openide.util.actions.CallableSystemAction;

/**
 *
 * @author eremeykin@gmail.com
 */
public abstract class AbstractAction extends CallableSystemAction implements LookupListener {

    private static final Logger LOGGER = LoggerManager.getLogger(AbstractAction.class);

    private  boolean refresh = true;

    public void setRefresh(boolean refresh) {
        this.refresh = refresh;
    }

    @Override
    public boolean isEnabled() {
        Lookup.Template templateModel = new Lookup.Template(Model.class);
        CentralLookup cl = CentralLookup.getDefault();
        return cl.lookup(templateModel).allInstances().iterator().hasNext(); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void actionPerformed(ActionEvent ev) {
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
            runner.runScript(model, refresh);
        } catch (NoSuchElementException ex) {
            LOGGER.warn("There is no opened model.");
            JOptionPane.showMessageDialog(null, "There is no opened model.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    public void performAction() {
    }

    @Override
    public HelpCtx getHelpCtx() {
        return HelpCtx.DEFAULT_HELP;
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

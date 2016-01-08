/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eremeykin.pete.actions;

import eremeykin.pete.api.core.centrallookupapi.CentralLookup;
import eremeykin.pete.api.core.logger.Logger;
import eremeykin.pete.api.core.logger.LoggerManager;
import eremeykin.pete.api.executor.ScriptExecutor;
import eremeykin.pete.api.model.Model;
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
        ScriptExecutor eExecutor
                = Lookup.getDefault().lookup(ScriptExecutor.class);
        if (eExecutor == null) {
            LOGGER.warn("There is no launcher module!");
            JOptionPane.showMessageDialog(null, "There is no launcher module.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        Lookup.Template templateModel = new Lookup.Template(Model.class);
        CentralLookup cl = CentralLookup.getDefault();
        try {
            Model model = (Model) cl.lookup(templateModel).allInstances().iterator().next();
            eExecutor.runScript(model, refresh);
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

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eremeykin.pete.runaction;

import eremeykin.pete.coreapi.centrallookupapi.CentralLookup;
import eremeykin.pete.modelapi.Model;
import eremeykin.pete.scriptrunnerapi.ScriptRunner;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.NoSuchElementException;
import javax.swing.JOptionPane;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionRegistration;
import org.openide.util.*;
import org.openide.util.NbBundle.Messages;

@ActionID(
        category = "File",
        id = "eremeykin.pete.runaction.RunScript"
)
@ActionRegistration(
        iconBase = "res/images16.png",
        displayName = "#CTL_RunScript"
)
@ActionReferences({
    @ActionReference(path = "Menu/File", position = 1950),
    @ActionReference(path = "Toolbars/File", position = 100),
    @ActionReference(path = "Shortcuts", name = "D-R")
})
@Messages("CTL_RunScript=Run model")
public final class RunScript implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent e) {
        ScriptRunner runner
                = Lookup.getDefault().lookup(ScriptRunner.class);
        Lookup.Template template = new Lookup.Template(Model.class);
        CentralLookup cl = CentralLookup.getDefault();
        try {
            Model model = (Model) cl.lookup(template).allInstances().iterator().next();
            runner.runScript(model,false);
        } catch (NoSuchElementException ex) {
            JOptionPane.showMessageDialog(null, "Нет открытой модели.");
        }

    }
}

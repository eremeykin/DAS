/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eremeykin.pete.modelloader;

import eremeykin.pete.centrallookupapi.CentralLookup;
import eremeykin.pete.modelapi.Model;
import eremeykin.pete.modelapi.Parameter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Iterator;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionRegistration;
import org.openide.filesystems.FileChooserBuilder;
import org.openide.util.Exceptions;
import org.openide.util.Lookup;
import org.openide.util.NbBundle.Messages;
import org.openide.util.lookup.AbstractLookup;
import org.openide.util.lookup.InstanceContent;
import org.openide.util.lookup.ServiceProvider;

@ActionID(
        category = "File",
        id = "eremeykin.pete.modelloader.Load"
)
@ActionRegistration(
        iconBase = "res/open-project-button16.png",
        displayName = "#CTL_Load"
)
@ActionReferences({
    @ActionReference(path = "Menu/File", position = 1450),
    @ActionReference(path = "Toolbars/File", position = 200),
    @ActionReference(path = "Shortcuts", name = "D-O")
})
@Messages("CTL_Load=Загрузить модель")
@ServiceProvider(service = Lookup.Provider.class, position = 20)
public final class Load implements ActionListener, Lookup.Provider {

    private InstanceContent content = new InstanceContent();

    @Override
    public void actionPerformed(ActionEvent e) {
        //The default dir to use if no value is stored
        File home = new File(System.getProperty("user.home"));
        //Now build a file chooser and invoke the dialog in one line of code
        //"user-dir" is our unique key
        File toAdd = new FileChooserBuilder("user-dir").setTitle("Open File").
                setDefaultWorkingDirectory(home).setApproveText("Open").showOpenDialog();
        //Result will be null if the user clicked cancel or closed the dialog w/o OK
        if (toAdd != null) {
            try {
                ModelLoader modelLoader = new ModelLoader(toAdd);
                Model m = modelLoader.load();
                CentralLookup cl = CentralLookup.getDefault();
                Collection parameters = cl.lookupAll(Model.class);
                if (!parameters.isEmpty()) {
                    Iterator<Model> it = parameters.iterator();
                    while (it.hasNext()) {
                        Model model = it.next();
                        cl.remove(model);
                    }
                }
                cl.add(m);
            } catch (ClassNotFoundException ex) {
                Exceptions.printStackTrace(ex);// Нельзя открыть модель т.к. отсутствует JDBC
            } catch (SQLException ex) {
                Exceptions.printStackTrace(ex);
            } catch (ModelLoader.LoadingException ex) {
                Exceptions.printStackTrace(ex);
            }
        }
    }

    @Override
    public Lookup getLookup() {
        return new AbstractLookup(content);
    }
}

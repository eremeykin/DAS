/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eremeykin.pete.loader.action;

import eremeykin.pete.api.core.centrallookupapi.CentralLookup;
import eremeykin.pete.api.core.workspace.WorkspaceManager;
import eremeykin.pete.api.model.Model;
import eremeykin.pete.loader.ModelLoader;
import eremeykin.pete.loader.ModelLoaderFactory;
import eremeykin.pete.loader.XlsxModelLoader;
import eremeykin.pete.loader.dao.DaoException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import javax.swing.JOptionPane;
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
        id = "eremeykin.pete.loader.Load"
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
@Messages("CTL_Load=Load model")
@ServiceProvider(service = Lookup.Provider.class, position = 20)
public final class Load implements ActionListener, Lookup.Provider {

    private InstanceContent content = new InstanceContent();

    @Override
    public void actionPerformed(ActionEvent e) {
        //The default dir to use if no value is stored
//        Collection<? extends PathProvider> allPathProviders = Lookup.getDefault().lookupAll(PathProvider.class);
        try {
//            PathProvider firstProvider = allPathProviders.iterator().next();
//            File home = firstProvider.getPath();
            File home = WorkspaceManager.INSTANCE.getWorkspace();
            //Now build a file chooser and invoke the dialog in one line of code
            //"user-dir" is our unique key
            String windir = System.getenv("WINDIR").substring(0, 1) + ":\\";
            File toAdd = new FileChooserBuilder("user-dir").setTitle("Open File").
                    setDefaultWorkingDirectory(new File(windir)).setApproveText("Open").showOpenDialog();
            //Result will be null if the user clicked cancel or closed the dialog w/o OK
            if (toAdd != null) {
                try {
                    File newModel = new File(home, toAdd.getName());
                    Files.deleteIfExists(newModel.toPath());
                    Files.copy(toAdd.toPath(), newModel.toPath());
                    Model m = null;
                    ModelLoader loader = ModelLoaderFactory.getInstance().createModelDAO(toAdd);
                    if (loader!=null){
                        m = loader.load();
                    }
                    else{throw new DaoException("Error during creating loader");}
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
                } catch (ClassNotFoundException | SQLException | DaoException ex) {
                    Exceptions.printStackTrace(ex);// Нельзя открыть модель т.к. отсутствует JDBC
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(null, "Ошибка при копировании исходного файла модели в рабочий каталог.", "Ошибка", JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (NoSuchElementException ex) {
            JOptionPane.showMessageDialog(null, "Невозможно определить рабочий каталог.\n"
                    + "Вероятно, отсутствует модуль выбора каталога.", "Ошибка", JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    public Lookup getLookup() {
        return new AbstractLookup(content);
    }
}

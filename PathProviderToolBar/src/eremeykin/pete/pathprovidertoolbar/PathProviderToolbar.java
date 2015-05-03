/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eremeykin.pete.pathprovidertoolbar;

import eremeykin.pete.pathprovider.PathProvider;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.io.File;
import javax.swing.AbstractAction;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionRegistration;
import org.openide.util.actions.Presenter;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author Pete
 */
@ActionID(
        category = "File",
        id = "org.myorg.googletoolbar.GoogleActionListener")
@ActionRegistration(
        lazy = false,
        displayName = "NOT-USED")
@ActionReference(
        path = "Toolbars/File",
        position = 0)

@ServiceProvider(service = PathProvider.class)
public class PathProviderToolbar extends AbstractAction implements Presenter.Toolbar, PathProvider {

    static PathProviderForm ppForm = new PathProviderForm();

    @Override
    public void actionPerformed(ActionEvent e) {
//
    }

    @Override
    public Component getToolbarPresenter() {
        return ppForm;
    }

    @Override
    public File getPath() {
        File resultFile = new File(ppForm.getText());
        resultFile.mkdirs();
        return resultFile;
    }
}

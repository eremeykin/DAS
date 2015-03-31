/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eremeykin.pete.modelmanager;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionRegistration;
import org.openide.util.NbBundle.Messages;
import org.openide.util.RequestProcessor;
import org.openide.windows.TopComponent;
import org.openide.windows.WindowManager;

@ActionID(
        category = "File",
        id = "eremeykin.pete.modelmanager.OpenModel"
)
@ActionRegistration(
        iconBase = "res/open-project-button16.png",
        displayName = "#CTL_OpenModel"
)
@ActionReferences({
    @ActionReference(path = "Menu/File", position = 1450),
    @ActionReference(path = "Toolbars/File", position = 200),
    @ActionReference(path = "Shortcuts", name = "D-O")
})
@Messages("CTL_OpenModel=Открыть модель ...")
public final class OpenModel implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent e) {
        // TODO implement action body
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new FileFilter() {

            @Override
            public boolean accept(File f) {
                String fileName = f.getName();
                String extension = "";
                int i = fileName.lastIndexOf('.');
                if (i > 0) {
                    extension = fileName.substring(i + 1);
                }
                return f.isDirectory() || extension.equals("das") || extension.equals("DAS");
            }

            @Override
            public String getDescription() {
                return "DAS files";
            }
        });

        fileChooser.showOpenDialog(null);
        TopComponent editor = WindowManager.getDefault().findTopComponent("EditorTopComponent");
        TopComponent viewport = WindowManager.getDefault().findTopComponent("ViewportTopComponent");
        editor.open();
        viewport.open();

    }
}

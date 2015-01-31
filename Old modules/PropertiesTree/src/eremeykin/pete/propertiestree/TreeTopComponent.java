/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eremeykin.pete.propertiestree;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import org.netbeans.api.settings.ConvertAsProperties;
import org.netbeans.swing.outline.Outline;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.util.Exceptions;
import org.openide.windows.TopComponent;
import org.openide.util.NbBundle.Messages;

/**
 * Top component which displays something.
 */
@ConvertAsProperties(
        dtd = "-//eremeykin.pete.propertiestree//Tree//EN",
        autostore = false
)
@TopComponent.Description(
        preferredID = "TreeTopComponent",
        iconBase = "res/IMG/icon.png",
        persistenceType = TopComponent.PERSISTENCE_ALWAYS
)
@TopComponent.Registration(mode = "properties", openAtStartup = true)
@ActionID(category = "Window", id = "eremeykin.pete.propertiestree.TreeTopComponent")
@ActionReference(path = "Menu/Window" /*, position = 333 */)
@TopComponent.OpenActionRegistration(
        displayName = "#CTL_TreeAction",
        preferredID = "TreeTopComponent"
)
@Messages({
    "CTL_TreeAction=Tree",
    "CTL_TreeTopComponent=Tree Window",
    "HINT_TreeTopComponent=This is a Tree window"
})
public final class TreeTopComponent extends TopComponent {

    public TreeTopComponent() {
        initComponents();
        setName(Bundle.CTL_TreeTopComponent());
        setToolTipText(Bundle.HINT_TreeTopComponent());
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        outline1 = new org.netbeans.swing.outline.Outline();

        jScrollPane1.setViewportView(outline1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 623, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 449, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane jScrollPane1;
    private org.netbeans.swing.outline.Outline outline1;
    // End of variables declaration//GEN-END:variables
    @Override
    public void componentOpened() {
        try {
            Model m = new Model(new File("C:\\Users\\Пётр\\Documents\\NetBeansProjects\\DAS\\Temp\\model1"));
            outline1 = m.getOutline();
            jScrollPane1.setViewportView(outline1);
            JOptionPane.showMessageDialog(null, "Test");
            // TODO add custom code on component opening
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        } catch (SQLException ex) {
            Exceptions.printStackTrace(ex);
        } catch (XMLParser.XMLParsingException ex) {
            Exceptions.printStackTrace(ex);
        } catch (ClassNotFoundException ex) {
            Exceptions.printStackTrace(ex);
        } catch (InstantiationException ex) {
            Exceptions.printStackTrace(ex);
        } catch (IllegalAccessException ex) {
            Exceptions.printStackTrace(ex);
        } catch (IllegalArgumentException ex) {
            Exceptions.printStackTrace(ex);
        } catch (InvocationTargetException ex) {
            Exceptions.printStackTrace(ex);
        }
    }

    @Override
    public void componentClosed() {
        // TODO add custom code on component closing
    }

    void writeProperties(java.util.Properties p) {
        // better to version settings since initial version as advocated at
        // http://wiki.apidesign.org/wiki/PropertyFiles
        p.setProperty("version", "1.0");
        // TODO store your settings
    }

    void readProperties(java.util.Properties p) {
        String version = p.getProperty("version");
        // TODO read your settings according to their version
    }
}

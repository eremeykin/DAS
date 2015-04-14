/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eremeykin.pete.viewport;

import eremeykin.pete.*;
import eremeykin.pete.centrallookupapi.CentralLookup;
import eremeykin.pete.modelapi.Model;
import java.util.Collection;
import java.util.Iterator;
import org.netbeans.api.settings.ConvertAsProperties;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.util.Lookup;
import org.openide.util.LookupEvent;
import org.openide.util.LookupListener;
import org.openide.windows.TopComponent;
import org.openide.util.NbBundle.Messages;

/**
 * Top component which displays something.
 */
@ConvertAsProperties(
        dtd = "-//eremeykin.pete.viewport//Viewport//EN",
        autostore = false
)
@TopComponent.Description(
        preferredID = "ViewportTopComponent",
        //iconBase="SET/PATH/TO/ICON/HERE", 
        persistenceType = TopComponent.PERSISTENCE_ALWAYS
)
@TopComponent.Registration(mode = "explorer", openAtStartup = false)
@ActionID(category = "Window", id = "eremeykin.pete.viewport.ViewportTopComponent")
@ActionReference(path = "Menu/Window" /*, position = 333 */)
@TopComponent.OpenActionRegistration(
        displayName = "#CTL_ViewportAction",
        preferredID = "ViewportTopComponent"
)
@Messages({
    "CTL_ViewportAction=Viewport",
    "CTL_ViewportTopComponent=Viewport Window",
    "HINT_ViewportTopComponent=This is a Viewport window"
})
public final class ViewportTopComponent extends TopComponent implements LookupListener{

    private Lookup.Result userInfoResult = null;

    public ViewportTopComponent() {
        initComponents();
        Lookup.Template template = new Lookup.Template(Model.class);
        CentralLookup cl = CentralLookup.getDefault();
        userInfoResult = cl.lookup(template);
        userInfoResult.addLookupListener(this);
        setName(Bundle.CTL_ViewportTopComponent());
        setToolTipText(Bundle.HINT_ViewportTopComponent());
//        ObjLoader appletObjLoader = new ObjLoader(this);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
    @Override
    public void componentOpened() {
        // TODO add custom code on component opening
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

    @Override
    public void resultChanged(LookupEvent evt) {
        Object o = evt.getSource();
        if (o != null) {
            Lookup.Result r = (Lookup.Result) o;
            Collection infos = r.allInstances();
            if (infos.isEmpty()) {
                System.out.println("");
//                EventQueue.invokeLater(new SetterRunnable(new DefaultUserInformation()));
            } else {
                this.open();
                Iterator<Model> it = infos.iterator();
                while (it.hasNext()) {
                    Model m = it.next();
                    this.removeAll();
                    new ObjLoader(this, m.getObjReader());
                    this.doLayout();
//                    EventQueue.invokeLater(new SetterRunnable(info));
                }

            }
        }
    }
}

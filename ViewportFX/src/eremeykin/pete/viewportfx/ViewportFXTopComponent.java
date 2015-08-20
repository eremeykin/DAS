/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eremeykin.pete.viewportfx;

import eremeykin.pete.coreapi.centrallookupapi.CentralLookup;
import eremeykin.pete.modelapi.Model;
import eremeykin.pete.modelapi.ModelStringChangedEvent;
import eremeykin.pete.modelapi.ReaderChangedListener;
import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.FlowLayout;
import java.util.Collection;
import java.util.Iterator;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Camera;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
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
        dtd = "-//eremeykin.pete.viewportfx//ViewportFX//EN",
        autostore = false
)
@TopComponent.Description(
        preferredID = "ViewportFXTopComponent",
        //iconBase="SET/PATH/TO/ICON/HERE", 
        persistenceType = TopComponent.PERSISTENCE_ALWAYS
)
@TopComponent.Registration(mode = "editor", openAtStartup = true)
@ActionID(category = "Window", id = "eremeykin.pete.viewportfx.ViewportFXTopComponent")
@ActionReference(path = "Menu/Window" /*, position = 333 */)
@TopComponent.OpenActionRegistration(
        displayName = "#CTL_ViewportFXAction",
        preferredID = "ViewportFXTopComponent"
)
@Messages({
    "CTL_ViewportFXAction=ViewportFX",
    "CTL_ViewportFXTopComponent=ViewportFX Window",
    "HINT_ViewportFXTopComponent=This is a ViewportFX window"
})
public final class ViewportFXTopComponent extends TopComponent implements LookupListener {

    final JFXPanel fxPanel = new JFXPanel();
    private Lookup.Result<Model> modelResult = null;
    
    public ViewportFXTopComponent() {
        initComponents();
        setName(Bundle.CTL_ViewportFXTopComponent());
        setToolTipText(Bundle.HINT_ViewportFXTopComponent());

        Lookup.Template template = new Lookup.Template(Model.class);
        CentralLookup cl = CentralLookup.getDefault();
        modelResult = cl.lookup(template);
        modelResult.addLookupListener(this);

        Platform.setImplicitExit(false);
        //setLayout(new BorderLayout());
        jPanel2.setLayout(new BorderLayout());
        jPanel2.add(fxPanel);
        fxPanel.setVisible(true);
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                SceneBuilder builder = new ModelSceneBuilder();
                Director director = new Director();
                director.setSceneBuilder(builder);
                director.buildScene();
                Scene scene = director.getScene();
                fxPanel.setScene(scene);
            }
        });
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel2 = new javax.swing.JPanel();

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jPanel2;
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

    private void setSceneToPanel(JFXPanel panel) {

    }

    @Override
    public void resultChanged(LookupEvent evt) {
        Object o = evt.getSource();
        if (o != null) {
            Lookup.Result r = (Lookup.Result) o;
            Collection infos = r.allInstances();
            if (infos.isEmpty()) {
            } else {
                this.open();
                Iterator<Model> it = infos.iterator();
                if (it.hasNext()) {
                    final Model m = it.next();
                    ReaderChangedListener rcl = new ReaderChangedListener() {

                        @Override
                        public void readerChanged(ModelStringChangedEvent evt) {
                            ViewportFXTopComponent.this.fxPanel.removeAll();
                            Platform.runLater(new Runnable() {
                                @Override
                                public void run() {
                                    SceneBuilder builder = new ModelSceneBuilder();
                                    Director director = new Director();
                                    director.setSceneBuilder(builder);
                                    director.buildScene(m.getModelFile());
                                    Scene scene = director.getScene();
                                    fxPanel.setScene(scene);
                                }
                            });
                        }

                    };
                    rcl.readerChanged(null);
                    m.addReaderChangedListener(rcl);

                }
            }
        }
    }
}
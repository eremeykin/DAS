/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eremeykin.pete.viewport;

import eremeykin.pete.api.core.centrallookupapi.CentralLookup;
import eremeykin.pete.api.model.*;
import eremeykin.pete.api.model.Model;
import eremeykin.pete.reports.api.Report;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Label;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collection;
import java.util.Iterator;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.BorderPane;
import javax.swing.AbstractAction;
import javax.swing.BoxLayout;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.netbeans.api.settings.ConvertAsProperties;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.util.Lookup;
import org.openide.util.LookupEvent;
import org.openide.util.LookupListener;
import org.openide.windows.TopComponent;
import org.openide.util.NbBundle.Messages;
import org.openide.util.Task;
import org.openide.util.TaskListener;

/**
 * Top component which displays something.
 */
@ConvertAsProperties(
        dtd = "-//eremeykin.pete.viewport//ViewportFX//EN",
        autostore = false
)
@TopComponent.Description(
        preferredID = "ViewportFXTopComponent",
        //iconBase="SET/PATH/TO/ICON/HERE", 
        persistenceType = TopComponent.PERSISTENCE_ALWAYS
)
@TopComponent.Registration(mode = "editor", openAtStartup = true)
@ActionID(category = "Window", id = "eremeykin.pete.viewport.ViewportFXTopComponent")
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
public final class ViewportFXTopComponent extends TopComponent implements TaskListener {

    private Lookup.Result<Model> modelResult = null;
    private LookupListener modelLookupListener = this.new ModelLookupListener();
    private Lookup.Result<Task> taskResult = null;
    private LookupListener taskLookupListerner = this.new TaskLookupListener();

    final JFXPanel fxPanel = new JFXPanel();
    private JFXPanel indPanel = new JFXPanel();
    private Model model;

    ModelSceneBuilder builder = new ModelSceneBuilder();

    public ViewportFXTopComponent() {
        initComponents();
        setName(Bundle.CTL_ViewportFXTopComponent());
        setToolTipText(Bundle.HINT_ViewportFXTopComponent());

        Platform.setImplicitExit(false);
        //setLayout(new BorderLayout());
        jPanel2.setLayout(new BorderLayout());
        jPanel2.add(fxPanel);
        fxPanel.setVisible(true);

        jButton1.setBorder(null);
        jButton1.setText(null);
        jButton1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ModelParameter p = model.getParameterByID(model.getRoot(), 33);
                Double value = Double.valueOf(p.getValue());
                value += 0.1;
                p.setValue(Double.toString(value));
            }
        });

        jButton2.setBorder(null);
        jButton2.setText(null);

        jButton2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ModelParameter p = model.getParameterByID(model.getRoot(), 33);
                Double value = Double.valueOf(p.getValue());
                value -= 0.1;
                p.setValue(Double.toString(value));
            }
        });

        jButton3.setBorder(null);
        jButton3.setText(null);
        jButton3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ModelParameter p = model.getParameterByID(model.getRoot(), 30);
                Double angle = Double.valueOf(p.getValue());
                angle -= 15.0;
                p.setValue(Double.toString(angle));
            }
        });
        jButton4.setBorder(null);
        jButton4.setText(null);
        jButton4.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ModelParameter p = model.getParameterByID(model.getRoot(), 30);
                Double angle = Double.valueOf(p.getValue());
                angle += 15.0;
                p.setValue(Double.toString(angle));
            }
        });
        updateView();
//        Platform.runLater(new Runnable() {
//            @Override
//            public void run() {
//                SceneBuilder builder = new ModelSceneBuilder();
//                Director director = new Director();
//                director.setSceneBuilder(builder);
//                director.buildScene();
//                Scene scene = director.getScene();
//                fxPanel.setScene(scene);
//            }
//        });

        ProgressIndicator pin = new ProgressIndicator();
        pin.setProgress(-1.0f);
        Group root = new Group();
        root.getChildren().add(pin);
        Scene s = new Scene(root);
        s.setFill(javafx.scene.paint.Color.TRANSPARENT);
        indPanel.setScene(s);
        indPanel.setMaximumSize(new Dimension(60, 120));

        fxPanel.setLayout(new BoxLayout(fxPanel, BoxLayout.X_AXIS));
        indPanel.setVisible(false);
        fxPanel.add(indPanel);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel2 = new javax.swing.JPanel();
        jToolBar1 = new javax.swing.JToolBar();
        jButton2 = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 420, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 349, Short.MAX_VALUE)
        );

        jToolBar1.setFloatable(false);
        jToolBar1.setRollover(true);

        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/call_point_18l.png"))); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(jButton2, org.openide.util.NbBundle.getMessage(ViewportFXTopComponent.class, "ViewportFXTopComponent.jButton2.text")); // NOI18N
        jButton2.setFocusable(false);
        jButton2.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton2.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar1.add(jButton2);

        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/call_point_18r.png"))); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(jButton1, org.openide.util.NbBundle.getMessage(ViewportFXTopComponent.class, "ViewportFXTopComponent.jButton1.text")); // NOI18N
        jButton1.setFocusable(false);
        jButton1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton1.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar1.add(jButton1);

        jButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/rollback-l.png"))); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(jButton3, org.openide.util.NbBundle.getMessage(ViewportFXTopComponent.class, "ViewportFXTopComponent.jButton3.text")); // NOI18N
        jButton3.setFocusable(false);
        jButton3.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton3.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar1.add(jButton3);

        jButton4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/rollback-r.png"))); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(jButton4, org.openide.util.NbBundle.getMessage(ViewportFXTopComponent.class, "ViewportFXTopComponent.jButton4.text")); // NOI18N
        jButton4.setFocusable(false);
        jButton4.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton4.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar1.add(jButton4);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jToolBar1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JToolBar jToolBar1;
    // End of variables declaration//GEN-END:variables
    @Override
    public void componentOpened() {
        modelResult = CentralLookup.getDefault().lookupResult(Model.class);
        modelResult.addLookupListener(modelLookupListener);

        taskResult = CentralLookup.getDefault().lookupResult(Task.class);
        taskResult.addLookupListener(taskLookupListerner);

        Model m = CentralLookup.getDefault().lookup(Model.class);
        if (m != null && model != null) {
            model.removeModelChangedListener(builder);
            model = m;
            model.addModelChangedListener(builder);
            updateView();
        }
    }

    @Override
    public void componentClosed() {
        modelResult.removeLookupListener(modelLookupListener);
        taskResult.removeLookupListener(taskLookupListerner);
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

    private class ModelLookupListener implements LookupListener {

        @Override
        public void resultChanged(LookupEvent ev) {
            Collection<? extends Model> allModels = modelResult.allInstances();
            if (!allModels.isEmpty()) {
                for (Model model : allModels) {
                    setModel(model);
                }
            }
        }
    }

    private class TaskLookupListener implements LookupListener {

        @Override
        public void resultChanged(LookupEvent ev) {
            Iterator iter = taskResult.allInstances().iterator();
            Task task = (Task) iter.next();
            while (task.isFinished()) {
                if (iter.hasNext()) {
                    task = (Task) iter.next();
                }
            }
            if (task.isFinished()) {
                return;
            }
            task.addTaskListener(ViewportFXTopComponent.this);
            indPanel.setVisible(true);
        }

    }

    private void updateView() {
//        Director director = new Director();
//        builder = new ModelSceneBuilder();
//        director.setSceneBuilder(builder);
//        director.buildScene(model!=null?model.getModelFile():null);
        if (this.model != null) {
            this.model.removeModelChangedListener(builder);
        }
        builder = new ModelSceneBuilder(model != null ? model.getModelFile() : null);
        if (model != null) {
            model.addModelChangedListener(builder);
            model.refresh();
        }
        Scene scene = builder.getScene();
        fxPanel.setScene(scene);
    }

    private void setModel(final Model model) {
        if (this.model != null) {
            this.model.removeModelChangedListener(builder);
        }
        this.model = model;
        this.model.addModelChangedListener(builder);
        ModelFileChangedListener readerChangedListener = new ModelFileChangedListener() {

            @Override
            public void fileChanged(ModelFileChangedEvent evt) {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        updateView();
                    }
                });
            }

        };
        readerChangedListener.fileChanged(null);
        model.addModelFileChangedListener(readerChangedListener);
    }

    @Override
    public void taskFinished(Task task) {
        indPanel.setVisible(false);
    }
}

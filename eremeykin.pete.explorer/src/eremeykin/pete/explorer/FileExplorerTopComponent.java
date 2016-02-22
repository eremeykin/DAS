/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eremeykin.pete.explorer;

import eremeykin.pete.api.core.centrallookupapi.CentralLookup;
import eremeykin.pete.api.core.workspace.WorkspaceManager;
import eremeykin.pete.api.model.Model;
import eremeykin.pete.explorer.nodes.ReportItemNode;
import eremeykin.pete.explorer.nodes.WorkspaceFileNode;
import java.awt.BorderLayout;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.netbeans.api.settings.ConvertAsProperties;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.explorer.ExplorerManager;
import org.openide.explorer.view.BeanTreeView;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;
import org.openide.loaders.DataObject;
import org.openide.loaders.DataObjectNotFoundException;
import org.openide.nodes.Children;
import org.openide.nodes.Node;
import org.openide.util.Lookup;
import org.openide.util.LookupEvent;
import org.openide.util.LookupListener;
import org.openide.windows.TopComponent;
import org.openide.util.NbBundle.Messages;

/**
 * Top component which displays something.
 */
@ConvertAsProperties(
        dtd = "-//eremeykin.pete.explorer//FileExplorer//EN",
        autostore = false
)
@TopComponent.Description(
        preferredID = "FileExplorerTopComponent",
        //iconBase="SET/PATH/TO/ICON/HERE", 
        persistenceType = TopComponent.PERSISTENCE_ALWAYS
)
@TopComponent.Registration(mode = "explorer", openAtStartup = true)
@ActionID(category = "Window", id = "eremeykin.pete.explorer.FileExplorerTopComponent")
@ActionReference(path = "Menu/Window" /*, position = 333 */)
@TopComponent.OpenActionRegistration(
        displayName = "#CTL_FileExplorerAction",
        preferredID = "FileExplorerTopComponent"
)
@Messages({
    "CTL_FileExplorerAction=FileExplorer",
    "CTL_FileExplorerTopComponent=FileExplorer Window",
    "HINT_FileExplorerTopComponent=This is a FileExplorer window"
})
public final class FileExplorerTopComponent extends TopComponent implements ExplorerManager.Provider, LookupListener {

    private Model model;
    private Lookup.Result<Model> modelResult = null;
    private ExplorerManager mgr = new ExplorerManager();
    private List<ReportItemNode> reportItems = new ArrayList<ReportItemNode>();
    private WorkspaceFileNode wfNode = null;

    public FileExplorerTopComponent() throws DataObjectNotFoundException {
        initComponents();
        setName(Bundle.CTL_FileExplorerTopComponent());
        setToolTipText(Bundle.HINT_FileExplorerTopComponent());
//        setLayout(new BorderLayout());
        jPanel1.setLayout(new BorderLayout());
        jPanel1.add(new BeanTreeView(), BorderLayout.CENTER);
        
        mgr.getRootContext().setName("No models");
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jPanel1;
    // End of variables declaration//GEN-END:variables
    @Override
    public void componentOpened() {
        modelResult = CentralLookup.getDefault().lookupResult(Model.class);
        modelResult.addLookupListener(this);
        
        Model m = Lookup.getDefault().lookup(Model.class);
        if (m != null) {
            model = m;
            setModel();
        } else {
            mgr.setRootContext(Node.EMPTY);
        }
    }

    @Override
    public void componentClosed() {
        modelResult.removeLookupListener(this);
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
            Collection results = r.allInstances();
            if (!results.iterator().hasNext()) {
                return;
            }
            Object result = results.iterator().next();
            if (result instanceof Model) {
                setModel();
            }
        }
    }

    private void setModel() {
        try {
            File file = WorkspaceManager.INSTANCE.getWorkspace();
            FileObject fo = FileUtil.toFileObject(FileUtil.normalizeFile(file));
            DataObject dob = DataObject.find(fo);
            Node rootNode = dob.getNodeDelegate();
            this.getActionMap().clear();
            
            for (int i =0; i<5;i++){
                reportItems.add(new ReportItemNode(Children.LEAF));
            }
            
            wfNode = new WorkspaceFileNode.Builder(rootNode,reportItems).build();
            mgr.setRootContext(wfNode);
        } catch (DataObjectNotFoundException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public ExplorerManager getExplorerManager() {
        return mgr;
    }
}
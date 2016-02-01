/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eremeykin.pete.api.core.startup;

/**
 *
 * @author Pete
 */
import eremeykin.pete.api.core.logger.Logger;
import eremeykin.pete.api.core.logger.LoggerManager;
import eremeykin.pete.api.core.workspace.WorkspaceManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import org.openide.modules.OnStart;
import org.openide.util.NbPreferences;
import org.openide.windows.WindowManager;

@OnStart
public class StartupClass implements Runnable {

    private static final Object lock = new Object();
    private static final StartupForm frame = new StartupForm();
    private static final Logger LOGGER = LoggerManager.getLogger(WorkspaceManager.class);

    private static void setUIFont(javax.swing.plaf.FontUIResource f) {
        java.util.Enumeration keys = UIManager.getDefaults().keys();
        while (keys.hasMoreElements()) {
            Object key = keys.nextElement();
            Object value = UIManager.get(key);
            if (value != null && value instanceof javax.swing.plaf.FontUIResource) {
                UIManager.put(key, f);
            }
        }
    }

    private void setWorkspace(ActionEvent e) {
        synchronized (lock) {
            String workspaceString = frame.getText();
            File workspace = new File(workspaceString);
            if (workspace.exists() || workspace.mkdirs()) {
                WorkspaceManager.INSTANCE.setWorkspace(workspace);
                String workspaces = NbPreferences.forModule(StartupClass.class).get("WORKSPACES", "");
                if (!workspaces.equals("") && !workspaces.contains(frame.getText())) {
                    NbPreferences.forModule(StartupClass.class).put("WORKSPACES", workspaces + ";" + frame.getText());
                } else {
                    NbPreferences.forModule(StartupClass.class).put("WORKSPACES", frame.getText());
                }
                WindowManager.getDefault().invokeWhenUIReady(new Runnable() {
                    @Override
                    public void run() {
                        JFrame mainFrame = (JFrame) WindowManager.getDefault().getMainWindow();
                        mainFrame.setTitle("Deformation Analysis System: " + workspace.getAbsolutePath());
                    }
                });
                if (frame.getClearFlag()) {
                    for (File file : workspace.listFiles()) {
                        file.delete();
                    }
                }
                frame.setVisible(false);
                lock.notify();
            } else {
                JOptionPane.showMessageDialog(frame, "Please, correct the workspace path.");
            }
        }
    }

    @Override
    public void run() {
        frame.setVisible(true);
        frame.getOKButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                StartupClass.this.setWorkspace(e);
            }
        });
        synchronized (lock) {
            while (frame.isVisible()) {
                try {
                    lock.wait();
                } catch (InterruptedException e) {
                    LOGGER.error(e);
                    new JOptionPane(e.getMessage()).setVisible(true);
                    System.exit(-1);
                }
            }
        }
    }
}

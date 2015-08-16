/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eremeykin.pete.coreapi.startup;

/**
 *
 * @author Pete
 */
import eremeykin.pete.coreapi.workspace.WorkspaceManager;
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

    private static Object lock = new Object();
    private static StartupForm frame = new StartupForm();

    public static void setUIFont(javax.swing.plaf.FontUIResource f) {
        java.util.Enumeration keys = UIManager.getDefaults().keys();
        while (keys.hasMoreElements()) {
            Object key = keys.nextElement();
            Object value = UIManager.get(key);
            if (value != null && value instanceof javax.swing.plaf.FontUIResource) {
                UIManager.put(key, f);
            }
        }
    }

    public void run() {
//        setUIFont (new javax.swing.plaf.FontUIResource("Serif",Font.ITALIC,12));
        frame.setVisible(true);
        frame.getOKButton().addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                synchronized (lock) {
                    String workspaceString = frame.getText();
                    File workspace;
                    workspace = new File(workspaceString);
                    workspace.mkdirs();
                    if (workspace.exists()) {
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
                        frame.setVisible(false);
                        lock.notify();
                    } else {
                        JOptionPane.showMessageDialog(frame, "Please, correct the workspace path.");
                    }
                }
            }
        });
        synchronized (lock) {
            while (frame.isVisible()) {
                try {
                    lock.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    new JOptionPane(e.getMessage()).setVisible(true);
                    System.exit(-1);
                }
            }
        }
    }
}

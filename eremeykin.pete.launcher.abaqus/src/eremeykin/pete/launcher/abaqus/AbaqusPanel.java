/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eremeykin.pete.launcher.abaqus;

import eremeykin.pete.api.core.logger.Logger;
import eremeykin.pete.api.core.logger.LoggerManager;
import eremeykin.pete.api.core.workspace.WorkspaceManager;
import java.awt.Cursor;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.text.JTextComponent;
import org.openide.util.Exceptions;
import org.openide.util.NbPreferences;
import org.openide.util.RequestProcessor;
import org.openide.windows.IOProvider;
import org.openide.windows.InputOutput;

final class AbaqusPanel extends javax.swing.JPanel {

    private static final String ABAQUS_BAT = "abaqus.bat";
    private static final String[] ABAQUS_CHECK_OPTIONS = new String[]{"whereami"};
    private static final String LMGRD_EXE = "lmgrd.exe";
    private static final String[] LMGRD_CHECK_OPTIONS = new String[]{"-z", "-v"};
    private static final Logger LOGGER = LoggerManager.getLogger(WorkspaceManager.class);
    private static final String[] LMGRD_COMMAND = {"cmd.exe", "/C", "lmgrd -z"};
    private final AbaqusOptionsPanelController controller;

    AbaqusPanel(AbaqusOptionsPanelController controller) {
        this.controller = controller;
        initComponents();
        // TODO listen to changes in form fields and call controller.changed()
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jTextField2 = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(jLabel1, org.openide.util.NbBundle.getMessage(AbaqusPanel.class, "AbaqusPanel.jLabel1.text")); // NOI18N

        jTextField1.setText(org.openide.util.NbBundle.getMessage(AbaqusPanel.class, "AbaqusPanel.jTextField1.text")); // NOI18N

        jLabel2.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(jLabel2, org.openide.util.NbBundle.getMessage(AbaqusPanel.class, "AbaqusPanel.jLabel2.text")); // NOI18N

        jTextField2.setText(org.openide.util.NbBundle.getMessage(AbaqusPanel.class, "AbaqusPanel.jTextField2.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(jButton1, org.openide.util.NbBundle.getMessage(AbaqusPanel.class, "AbaqusPanel.jButton1.text")); // NOI18N
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        org.openide.awt.Mnemonics.setLocalizedText(jButton2, org.openide.util.NbBundle.getMessage(AbaqusPanel.class, "AbaqusPanel.jButton2.text")); // NOI18N
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/res/questionIcon.png"))); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(jButton4, org.openide.util.NbBundle.getMessage(AbaqusPanel.class, "AbaqusPanel.jButton4.text")); // NOI18N
        jButton4.setBorder(null);
        jButton4.setBorderPainted(false);
        jButton4.setContentAreaFilled(false);
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                AbaqusCheckActionPerformed(evt);
            }
        });

        jButton5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/res/questionIcon.png"))); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(jButton5, org.openide.util.NbBundle.getMessage(AbaqusPanel.class, "AbaqusPanel.jButton5.text")); // NOI18N
        jButton5.setBorder(null);
        jButton5.setBorderPainted(false);
        jButton5.setContentAreaFilled(false);
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                LmgrdCheckActionPerformed(evt);
            }
        });

        org.openide.awt.Mnemonics.setLocalizedText(jButton3, org.openide.util.NbBundle.getMessage(AbaqusPanel.class, "AbaqusPanel.jButton3.text")); // NOI18N
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1)
                            .addComponent(jLabel2))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jTextField1, javax.swing.GroupLayout.DEFAULT_SIZE, 164, Short.MAX_VALUE)
                            .addComponent(jTextField2))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jButton5)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton2))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jButton4)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton1))))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jButton3)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel1)
                        .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jButton4))
                    .addComponent(jButton1, javax.swing.GroupLayout.Alignment.TRAILING))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel2)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jButton2, javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jButton5))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton3)
                .addContainerGap(170, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        JFileChooser fileChooser = createFileChooser(JFileChooser.DIRECTORIES_ONLY);
        int result = fileChooser.showDialog(this, "OK");
        if (result == JFileChooser.APPROVE_OPTION && fileChooser.getSelectedFile() != null) {
            File file = fileChooser.getSelectedFile();
            boolean containsAbaqus = false;
            for (File f : file.listFiles()) {
                if (f.getName().equals(ABAQUS_BAT)) {
                    containsAbaqus = true;
                    break;
                }
            }
            if (!containsAbaqus) {
                JOptionPane.showMessageDialog(this, "There is no \"" + ABAQUS_BAT + "\" in the selected directory!\nProbably it is wrong path.", "Wrong path", JOptionPane.ERROR_MESSAGE);
            }
            jTextField1.setText(file.getAbsolutePath());
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        JFileChooser fileChooser = createFileChooser(JFileChooser.FILES_ONLY);
        int result = fileChooser.showDialog(this, "OK");
        if (result == JFileChooser.APPROVE_OPTION && fileChooser.getSelectedFile() != null) {
            File file = fileChooser.getSelectedFile();
            if (!file.getName().equals(LMGRD_EXE)) {
                JOptionPane.showMessageDialog(this, "The selected file name is not \"" + LMGRD_EXE + "\"!\nProbably it is wrong path.", "Wrong path", JOptionPane.ERROR_MESSAGE);
            }
            jTextField2.setText(file.getAbsolutePath());
        }
    }//GEN-LAST:event_jButton2ActionPerformed

    private void AbaqusCheckActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_AbaqusCheckActionPerformed
        checkVersion(jTextField1, jButton4, ABAQUS_BAT, ABAQUS_CHECK_OPTIONS, "Abaqus ");
    }//GEN-LAST:event_AbaqusCheckActionPerformed

    private void LmgrdCheckActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_LmgrdCheckActionPerformed
        checkVersion(jTextField2, jButton5, LMGRD_EXE, LMGRD_CHECK_OPTIONS, "lmgrd v");
    }//GEN-LAST:event_LmgrdCheckActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        RequestProcessor rProcessor = RequestProcessor.getDefault();
        LmgrdThread lmgrdThread = new LmgrdThread();
        rProcessor.post(lmgrdThread);
    }//GEN-LAST:event_jButton3ActionPerformed

    private JFileChooser createFileChooser(int type) {
        String windir = System.getenv("WINDIR").substring(0, 1) + ":\\";
        JFileChooser fileChooser = new JFileChooser(windir);
        fileChooser.setFileSelectionMode(type);
        return fileChooser;
    }

    private void checkVersion(JTextComponent textSource, JButton button, String cmd, String[] args, String keyword) {
        StringBuffer version = new StringBuffer();
        try {
            this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            try {
                String path = textSource.getText();
                VersionChecker vChecker = new VersionChecker(cmd, args, path, keyword);
                version = vChecker.check();
            } catch (IOException ex) {
                String message = "IOException while version checking. ";
                LOGGER.error(message + ex);
                JOptionPane.showMessageDialog(this, message, "IOException", JOptionPane.ERROR_MESSAGE);
            } catch (InterruptedException ex) {
                String message = "Checker thread was interrupted. ";
                LOGGER.error(message + ex);
                JOptionPane.showMessageDialog(this, message, "InterruptedException", JOptionPane.ERROR_MESSAGE);
            }
        } finally {
            this.setCursor(null);
            if (version.length() > 0) {
                button.setIcon(new javax.swing.ImageIcon(getClass().getResource("/res/ok.png"))); // NOI18N
                JOptionPane.showMessageDialog(null, cmd + " detected: " + version.toString(), "Abaqus detected", JOptionPane.INFORMATION_MESSAGE);
            } else {
                button.setIcon(new javax.swing.ImageIcon(getClass().getResource("/res/cross-icon.png"))); // NOI18N
                JOptionPane.showMessageDialog(null, cmd + " not found!", "Abaqus not found", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    void load() {
        String abaqusPath = NbPreferences.forModule(AbaqusPanel.class).get("ABAQUS_PATH", "");
        String lmgrdPath = NbPreferences.forModule(AbaqusPanel.class).get("LMGRD_PATH", "");
        jTextField1.setText(abaqusPath);
        jTextField2.setText(lmgrdPath);
    }

    void store() {
        NbPreferences.forModule(AbaqusPanel.class).put("ABAQUS_PATH", jTextField1.getText());
        NbPreferences.forModule(AbaqusPanel.class).put("LMGRD_PATH", jTextField2.getText());
    }

    boolean valid() {
        return true;
    }

    private class LmgrdThread extends Thread {

        Process process;

        public Process getProcess() {
            return process;
        }

        @Override
        public void run() {
            File home = WorkspaceManager.INSTANCE.getWorkspace();
            ProcessBuilder pBuilder = new ProcessBuilder(LMGRD_COMMAND);
            String lmgrdPath = jTextField2.getText();
            if (lmgrdPath == null) {
                String message = "Can't run lmgrd. Unknown lmgrd path.";
                JOptionPane.showMessageDialog(AbaqusPanel.this, message, "Null lmgrd path", JOptionPane.ERROR_MESSAGE);
                LOGGER.error(message + " lmgrdPath == null");
                return;
            }
            File file = new File(lmgrdPath);
            if (!file.isDirectory() && file.exists()) {
                lmgrdPath = file.getParent();
            } else {
                String message = "Can't get lmgrd file.";
                JOptionPane.showMessageDialog(AbaqusPanel.this, message, "Unknown lmgrd file", JOptionPane.ERROR_MESSAGE);
                LOGGER.error(message + " Lmgrd file is a directory or doesn't exist.");
                return;
            }
            pBuilder.environment().put("Path", lmgrdPath);
            pBuilder.directory(home);
            pBuilder.redirectErrorStream(true);
            try {
                process = pBuilder.start();
            } catch (IOException ex) {
                String message = "IOException while starting lmgrd.";
                JOptionPane.showMessageDialog(AbaqusPanel.this, message, "Null lmgrd path", JOptionPane.ERROR_MESSAGE);
                LOGGER.error(message + ex);
                return;
            }
            try (BufferedReader bri = new BufferedReader(new InputStreamReader(process.getInputStream(), "Cp866"))) {
                InputOutput io = IOProvider.getDefault().getIO("lmgrd", false);
                String line;
                while ((line = bri.readLine()) != null) {
                    LOGGER.info("Lmgrd started.");
                    io.getOut().println(line);
                    io.select();
                    if (line.contains("is already in use")) {
                        process.destroyForcibly();
//                            stopLmgrd();
                        return;
                    }
                }
            } catch (IOException ex) {
                String message = "IOException while reading lmgrd output.";
                JOptionPane.showMessageDialog(AbaqusPanel.this, message, "Null lmgrd path", JOptionPane.ERROR_MESSAGE);
                LOGGER.error(message + ex);
            }
        }
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    // End of variables declaration//GEN-END:variables
}

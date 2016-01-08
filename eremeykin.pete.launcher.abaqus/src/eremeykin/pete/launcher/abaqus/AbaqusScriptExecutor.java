/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eremeykin.pete.launcher.abaqus;

import eremeykin.pete.api.core.centrallookupapi.CentralLookup;
import eremeykin.pete.api.core.logger.Logger;
import eremeykin.pete.api.core.logger.LoggerManager;
import eremeykin.pete.api.core.workspace.WorkspaceManager;
import eremeykin.pete.api.model.Model;
import eremeykin.pete.api.executor.ScriptExecutor;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.util.concurrent.atomic.AtomicBoolean;
import javax.swing.JOptionPane;
import org.openide.util.Cancellable;
import org.openide.util.Exceptions;
import org.openide.util.Lookup;
import org.openide.util.NbPreferences;
import org.openide.util.RequestProcessor;
import org.openide.util.TaskListener;
import org.openide.util.lookup.ServiceProvider;
import org.openide.windows.IOProvider;
import org.openide.windows.InputOutput;

/**
 *
 * @author Pete
 */
@ServiceProvider(service = ScriptExecutor.class)
public class AbaqusScriptExecutor implements ScriptExecutor {

    private static final Logger LOGGER = LoggerManager.getLogger(AbaqusScriptExecutor.class);
    private static final String TMP_MODEL_FILE = "tmp_model.obj";

    @Override
    public void runScript(Model model, boolean refresh) {
        try {
            File scriptFile = model.getScriptFile(); //new File(model.getHome(), "refresh_script.py");
            File home = WorkspaceManager.INSTANCE.getWorkspace();
            File objFile = new File(home, TMP_MODEL_FILE);
            Files.deleteIfExists(objFile.toPath());

            String argString = " param " + model.getArgs();
            String pathEnvVar = NbPreferences.forModule(AbaqusPanel.class).get("ABAQUS_PATH", "");
            AbaqusThread abaqusThread = new AbaqusThread(scriptFile, argString, refresh, pathEnvVar);
            RequestProcessor rProcessor = RequestProcessor.getDefault();
            RequestProcessor.Task abaqus = rProcessor.post(abaqusThread);

            // add Task to Lookup
            Lookup.Template template = new Lookup.Template(TaskListener.class);
            CentralLookup cl = CentralLookup.getDefault();
            cl.add(abaqus);

            Thread modelRefresher = new Thread(new Runnable() {

                @Override
                public void run() {
                    try {
                        boolean finished = abaqus.waitFinished(30 * 60 * 1000);
                        if (abaqusThread.getSuccess().get() && finished) {
                            model.setModelFile(objFile);
                        }
                    } catch (InterruptedException ex) {
                        String message = "Can't refresh model. Thread was interrupted.";
                        LOGGER.error(message + ex);
                        JOptionPane.showMessageDialog(null, message, "InterruptedException", JOptionPane.ERROR_MESSAGE);
                    }
                }
            });
            modelRefresher.start();
//            deleteLockFile(home);
        } catch (IOException ex) {
            LOGGER.error(ex);
        }
    }

    private class AbaqusThread extends Thread implements Cancellable {

        private final static String KILL_COMMAND = "taskkill /F /IM ABQcaeK.exe";
        private final static String COMMAND = "abaqus cae ";
        private final StringBuilder args = new StringBuilder();
        private final File home;
        private final String path;
        private Process p;
        private final boolean refresh;
        private final AtomicBoolean success = new AtomicBoolean(false);

        public AtomicBoolean getSuccess() {
            return success;
        }

        public AbaqusThread(File scriptFile, String argString, boolean refresh, String path) {
            home = WorkspaceManager.INSTANCE.getWorkspace();
            args.append(" noGUI=\"")
                    .append(scriptFile.getAbsolutePath())
                    .append("\" -- ")
                    .append(argString)
                    .append(refresh ? " refresh=true " : " refresh=false ")
                    .append(" dir=\"")
                    .append(home.getAbsolutePath())
                    .append("\"");
            this.path = path;
            this.refresh = refresh;
        }

        @Override
        public void run() {
            String name = refresh ? "Abaqus refresh" : "Abaqus run";
            try {
                if (args.toString().contains("null")) {
                    throw new Error("Parameters contain null value");
                }
                ProcessBuilder pb = new ProcessBuilder("cmd.exe", "/C", COMMAND + args.toString());
                pb.directory(home);
                pb.redirectErrorStream(true);
                pb.environment().put("Path", path);
                p = pb.start();
                String line;
                try (BufferedReader bre = new BufferedReader(new InputStreamReader(p.getInputStream(), "Cp866"));) {
                    InputOutput io = IOProvider.getDefault().getIO("Abaqus", false);
                    while ((line = bre.readLine()) != null) {
                        io.getErr().println(line);
                        io.select();
                        if (line.contains("FLEXnet Licensing error:")) {
                            JOptionPane.showMessageDialog(null, "It is impossible to run Abaqus. License error. ", "Error", JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                    }
                } catch (IOException ex) {
                    String message = "Exception while reading abaqus output.";
                    LOGGER.error(message + ex);
                    JOptionPane.showMessageDialog(null, message, "IOException", JOptionPane.ERROR_MESSAGE);
                }
                p.waitFor();
            } catch (IOException | InterruptedException ex) {
                String message = "Exception while processing abaqus.";
                LOGGER.error(message + ex);
                JOptionPane.showMessageDialog(null, message, "IOException | InterruptedException", JOptionPane.ERROR_MESSAGE);
            }
            success.set(true);
        }

        @Override
        public boolean cancel() {
            try {
                Runtime.getRuntime().exec(KILL_COMMAND);
            } catch (IOException ex) {
                Exceptions.printStackTrace(ex);
            }
            p.destroyForcibly();
            return p.isAlive();
        }

    }

}

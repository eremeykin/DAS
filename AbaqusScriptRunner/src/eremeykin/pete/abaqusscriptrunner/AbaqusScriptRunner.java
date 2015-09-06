/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eremeykin.pete.abaqusscriptrunner;

import eremeykin.pete.*;
import eremeykin.pete.coreapi.centrallookupapi.CentralLookup;
import eremeykin.pete.coreapi.loggerapi.Logger;
import eremeykin.pete.coreapi.loggerapi.LoggerManager;
import eremeykin.pete.coreapi.workspace.WorkspaceManager;
import eremeykin.pete.modelapi.Model;
import eremeykin.pete.scriptrunnerapi.ScriptRunner;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.file.Files;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import javax.swing.JOptionPane;
import org.netbeans.api.progress.ProgressHandle;
import org.netbeans.api.progress.ProgressHandleFactory;
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
@ServiceProvider(service = ScriptRunner.class)
public class AbaqusScriptRunner implements ScriptRunner {

    private static final Logger LOGGER = LoggerManager.getLogger(AbaqusScriptRunner.class);
    private static final String TMP_MODEL_FILE = "tmp_model.obj";

    @Override
    public void runScript(Model model, boolean refresh) {
        try {
            File scriptFile = model.getScriptFile(); //new File(model.getHome(), "refresh_script.py");
            File home = WorkspaceManager.INSTANCE.getWorkspace();
            File objFile = new File(home, TMP_MODEL_FILE);
            Files.deleteIfExists(objFile.toPath());

            String argString =" param "+ model.getArgs();
            // 0 значит refresh=false
            // 1 значит refresh=true
            String pathEnvVar = NbPreferences.forModule(AbaqusPanel.class).get("ABAQUS_PATH", "");
            AbaqusThread abaqusThread = new AbaqusThread(scriptFile, argString, refresh, pathEnvVar);
            RequestProcessor rProcessor = RequestProcessor.getDefault();
            RequestProcessor.Task abaqus = rProcessor.post(abaqusThread);

            // add TaskListener
            Lookup.Template template = new Lookup.Template(TaskListener.class);
            CentralLookup cl = CentralLookup.getDefault();
            cl.add(abaqus);
//            Lookup.Result TLResult = cl.lookup(template);
//            Iterator<TaskListener> it =TLResult.allInstances().iterator();
//            while(it.hasNext()){
//                TaskListener next = it.next();
//                abaqus.addTaskListener(next);
//            }
            
            Thread modelRefresher = new Thread(new Runnable() {

                @Override
                public void run() {
                    try {
                        boolean finished = abaqus.waitFinished(30 * 60 * 1000);
                        if (abaqusThread.getSuccess().get() && finished) {
                            model.setModelFile(objFile);
                        }
                    } catch (InterruptedException ex) {
                        Exceptions.printStackTrace(ex);
                    }
                }
            });
            modelRefresher.start();
//            deleteLockFile(home);
        } catch (IOException ex) {
            LOGGER.error(ex);
        }
    }

    private void updateLookup(Model model) {
        Lookup.Template template = new Lookup.Template(Model.class);
        CentralLookup cl = CentralLookup.getDefault();
        Object modelResult = cl.lookup(template).allInstances().iterator().next();
        cl.remove(modelResult);
        cl.add(model);
    }

    private String join(TreeMap<Integer, String> map) {
        StringBuilder result = new StringBuilder();
        for (Entry<Integer, String> entry : map.entrySet()) {
            Integer key = entry.getKey();
            String value = entry.getValue();
            result.append(" ").append(value);
        }
        return result.toString();
    }

    ;

    private void deleteLockFile(File home) {
        for (File f : home.listFiles(new FilenameFilter() {

            @Override
            public boolean accept(File dir, String name) {
                if (name.contains(".lck")) {
                    return true;
                }
                return false;
            }
        })) {

            f.delete();
        }
    }

    private class AbaqusThread extends Thread implements Cancellable {

        private final static String KILL_COMMAND = "taskkill /F /IM ABQcaeK.exe";
        private final static String COMMAND = "abaqus cae ";
        private final StringBuilder args = new StringBuilder();
        private final File home;
        private final String path;
        private Process p;
        private boolean refresh;
        private AtomicBoolean success = new AtomicBoolean(false);

        public AtomicBoolean getSuccess() {
            return success;
        }

        public AbaqusThread(File scriptFile, String argString, boolean refresh, String path) {
            home = WorkspaceManager.INSTANCE.getWorkspace();
            args.append(" noGUI=\"");
            args.append(scriptFile.getAbsolutePath());
            args.append("\" -- ");
            args.append(argString);
            args.append(refresh ? " refresh=true " : " refresh=false ");
            args.append(" dir=\"");
            args.append(home.getAbsolutePath());
            args.append("\"");
            this.path = path;
            this.refresh = refresh;
        }

        @Override
        public void run() {
            String name = refresh ? "Abaqus refresh" : "Abaqus run";
            final ProgressHandle progr
                    = ProgressHandleFactory.createHandle(name, this);
            progr.start();
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
                    LOGGER.error(ex);
                }
                p.waitFor();
            } catch (IOException | InterruptedException ex) {
                LOGGER.error(ex);
            }
            success.set(true);
            progr.finish();
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

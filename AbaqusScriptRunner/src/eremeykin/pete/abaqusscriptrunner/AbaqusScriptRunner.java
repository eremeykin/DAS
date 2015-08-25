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
import java.util.Map.Entry;
import java.util.TreeMap;
import javax.swing.JOptionPane;
import org.openide.util.Exceptions;
import org.openide.util.Lookup;
import org.openide.util.NbPreferences;
import org.openide.util.RequestProcessor;
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

    @Override
    public void runScript(Model model, boolean refresh) {
        try {
            File scriptFile = model.getScriptFile(); //new File(model.getHome(), "refresh_script.py");
            File home = WorkspaceManager.INSTANCE.getWorkspace();
            File objFile = new File(home, "tmp_model.obj");
            Files.deleteIfExists(objFile.toPath());

            TreeMap map = model.getArgs();
            String argString = join(map);
            // 0 значит refresh=false
            // 1 значит refresh=true
//            String rStr = refresh ? " true" : " false";
//            String command = "abaqus cae noGUI=\"" + scriptFile.getAbsolutePath()
//                    + "\" -- " + argString + rStr + " \"" + home + "\"";
//            if (command.contains("null")) {
//                throw new Error("Parameters contain null value");
//            }
            String pathEnvVar = NbPreferences.forModule(AbaqusPanel.class).get("ABAQUS_PATH", "");
            Thread abaqusThread = new AbaqusThread(scriptFile, argString, refresh, pathEnvVar);
            RequestProcessor rProcessor = RequestProcessor.getDefault();
            rProcessor.post(abaqusThread);
//            abaqusThread.start();
            deleteLockFile(home);

            Reader newReader = new FileReader(objFile);
            String targetString = "";
            int ch;
            while ((ch = newReader.read()) != -1) {
                targetString += (char) ch;
            }
            newReader.close();
            model.setModelFile(objFile);
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

    private class AbaqusThread extends Thread {

        private final String command = "abaqus cae ";
        private final StringBuilder args = new StringBuilder();
        private final File home;
        private final String path;

        public AbaqusThread(File scriptFile, String argString, boolean refresh, String path) {
            home = WorkspaceManager.INSTANCE.getWorkspace();
            args.append(" noGUI=\"");
            args.append(scriptFile.getAbsolutePath());
            args.append("\" -- ");
            args.append(argString);
            args.append(refresh ? " true " : " false ");
            args.append(" \"");
            args.append(home.getAbsolutePath());
            args.append("\"");
            this.path = path;
        }

        @Override
        public void run() {
            try {
                if (args.toString().contains("null")) {
                    throw new Error("Parameters contain null value");
                }
                ProcessBuilder pb = new ProcessBuilder("cmd.exe", "/C", command+args.toString());
                pb.directory(home);
                pb.redirectErrorStream(true);
                pb.environment().put("Path", path);
                Process p = pb.start();
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
                    Exceptions.printStackTrace(ex);
                }
                p.waitFor();

            } catch (IOException ex) {
                Exceptions.printStackTrace(ex);
            } catch (InterruptedException ex) {
                Exceptions.printStackTrace(ex);
            }

        }

    }

}

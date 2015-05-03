/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eremeykin.pete.abaqusscriptrunner;

import eremeykin.pete.centrallookupapi.CentralLookup;
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
import java.nio.file.Files;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import org.openide.util.Lookup;
import org.openide.util.lookup.ServiceProvider;
import org.openide.windows.IOProvider;
import org.openide.windows.InputOutput;

/**
 *
 * @author Pete
 */
@ServiceProvider(service = ScriptRunner.class)
public class AbaqusScriptRunner implements ScriptRunner {

    @Override
    public void runScript(Model model, boolean refresh) {
        try {
            String script = model.getScript();
            File scriptFile = new File(model.getHome(), "refresh_script.py");
            Files.deleteIfExists(scriptFile.toPath());
            File objFile = new File(model.getHome(), "tmp_model.obj");
            Files.deleteIfExists(objFile.toPath());

            try (FileWriter fileWriter = new FileWriter(scriptFile, false)) {
                fileWriter.write(script + "\r\n");
            }
            TreeMap map = model.getArgs();
            String argString = join(map);
            // 0 значит refresh=false
            // 1 значит refresh=true
            String rStr = refresh ? " true" : " false";
            String command = "abaqus cae noGUI=\"" + scriptFile.getAbsolutePath()
                    + "\" -- " + argString + rStr + " \"" + model.getHome() + "\"";
            if (command.contains("null")) {
                throw new Error("Параметры содержат null");
            }
            deleteLockFile(model.getHome());
            ProcessBuilder pb = new ProcessBuilder("cmd.exe", "/C", command);
            pb.directory(model.getHome());
            Process p = pb.start();
            pb.redirectError(ProcessBuilder.Redirect.PIPE);
            pb.redirectOutput(ProcessBuilder.Redirect.PIPE);
            pb.redirectInput(ProcessBuilder.Redirect.PIPE);
            new Runnable() {

                @Override
                public void run() {
                    String line;
                    try (BufferedReader bre = new BufferedReader(new InputStreamReader(p.getErrorStream()));) {
                        InputOutput io = IOProvider.getDefault().getIO("Abaqus", false);
                        while ((line = bre.readLine()) != null) {
                            io.getErr().println(line);
                            io.select();
                            if (line.contains("FLEXnet Licensing error:")) {
                                JOptionPane.showMessageDialog(null, "Не удается запустить Abaqus. Ошибка лицензии. ", "Error", JOptionPane.ERROR_MESSAGE);
                                return;
                            }
                        }
                    } catch (IOException ex) {
                    }
                }
            }.run();
            new Runnable() {

                @Override
                public void run() {
                    String line;
                    try (BufferedReader bri = new BufferedReader(new InputStreamReader(p.getInputStream()));) {
                        InputOutput io = IOProvider.getDefault().getIO("Abaqus", false);
                        while ((line = bri.readLine()) != null) {
                            io.getOut().println(line);
                            io.select();
                            if (line.contains("FLEXnet Licensing error:")) {
                                JOptionPane.showMessageDialog(null, "Не удается запустить Abaqus. Ошибка лицензии. ", "Error", JOptionPane.ERROR_MESSAGE);
                                return;
                            }
                        }
                    } catch (IOException ex) {
                    }
                }
            }.run();
            p.waitFor();
            Reader newReader = new FileReader(objFile);
            model.setObjReader(newReader);
            newReader.close();
        } catch (IOException ex) {
            Logger.getLogger(AbaqusScriptRunner.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InterruptedException ex) {
            Logger.getLogger(AbaqusScriptRunner.class.getName()).log(Level.SEVERE, null, ex);
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

}

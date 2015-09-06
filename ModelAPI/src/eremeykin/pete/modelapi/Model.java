/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eremeykin.pete.modelapi;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

/**
 *
 * @author Pete
 */
public class Model implements ModelParameterChangedListener {

    private ModelParameter root;
    private File modelFile;
    private File scriptFile;
    private String script;
    private ArrayList<ModelFileChangedListener> modelFileListeners = new ArrayList<>();
    private ArrayList<ModelChangedListener> modelListeners = new ArrayList<>();
    private File home;

    public Model(ModelParameter root, File modelFile, File scriptFile) {
        this.root = root;
        this.modelFile = modelFile;
        this.scriptFile = scriptFile;
    }

    public ModelParameter getRoot() {
        return root;
    }

    public void setHome(File home) {
        this.home = home;
    }

//    public File getHome() {
//        return home;
//    }
    public File getModelFile() {
        return modelFile;
    }

    public File getScriptFile() {
        return scriptFile;
    }

//    public String getScript() throws IOException {
//        if (script != null) {
//            return script;
//        }
//        String line, result = "";
//        while ((line = scriptReader.readLine()) != null) {
//            result += line + "\r\n";
//        }
//        script = result;
//        return result;
//    }
    public void setModelFile(File modelFile) {
        this.modelFile = modelFile;
        ModelFileChangedEvent evt = new ModelFileChangedEvent(modelFile);
        for (ModelFileChangedListener listener : modelFileListeners) {
            listener.fileChanged(evt);
        }
    }

    public String getArgs() {
        TreeMap<String, String> args = new TreeMap<>();
        getArgsInner(args, root);
        StringBuilder result = new StringBuilder();
        for (Map.Entry<String, String> entry : args.entrySet()) {
            if (entry.getValue() != null) {
                result.append(" ").append(entry.getKey()).append("=").append(entry.getValue());
            }
        }
        return result.toString();
    }

    private void getArgsInner(Map<String, String> args, ModelParameter param) {
        String argN = param.getScrArg();
        if (argN != null) {
            args.put(param.getScrArg(), param.getValue());
        }
        for (ModelParameter p : param.getChildren()) {
            getArgsInner(args, p);
        }
    }

    public ModelParameter getParameterByID(ModelParameter rootParameter, Integer id) {
        for (ModelParameter p : rootParameter.getChildren()) {
            if (p.getId().equals(id)) {
                return p;
            } else {
                ModelParameter subParam = getParameterByID(p, id);
                if (subParam != null) {
                    return subParam;
                }
            }
        }
        return null;
    }

    public void addModelChangedListener(ModelChangedListener listener) {
        modelListeners.add(listener);
    }

    public void removeModelChangedListener(ModelChangedListener listener) {
        modelListeners.remove(listener);
    }

    public void addModelFileChangedListener(ModelFileChangedListener listener) {
        modelFileListeners.add(listener);
    }

    public void removeModelFileChangedListener(ModelFileChangedListener listener) {
        modelFileListeners.remove(listener);
    }

    @Override
    public void parameterChanged(ModelParameterChangedEvent evt) {
        ModelChangedEvent modelEvt = new ModelChangedEvent(evt.getParameterSource());
        for (ModelChangedListener listener : modelListeners) {
            listener.modelChanged(modelEvt);
        }
    }

}

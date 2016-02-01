/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eremeykin.pete.api.model;

import java.io.File;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
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
    private final ArrayList<ModelFileChangedListener> modelFileListeners = new ArrayList<>();
    private final ArrayList<ModelChangedListener> modelListeners = new ArrayList<>();

    public Model(ModelParameter root) {
        this.root = root;
        listenAllParameters(root);
    }

    private void listenAllParameters(ModelParameter rootParameter) {
        if (rootParameter.getChildren() != null) {
            for (ModelParameter p : rootParameter.getChildren()) {
                p.addParameterChangedListener(this);
                listenAllParameters(p);
            }
        }
    }

    public ModelParameter getRoot() {
        return root;
    }

    public File getModelFile() {
        return modelFile;
    }

    public File getScriptFile() {
        return scriptFile;
    }

    //why synchronized ?
    synchronized public void setModelFile(File modelFile) {
        if (modelFile == null) {
            throw new NullPointerException("modelFile can't be null");
        }
        this.modelFile = modelFile;
        ModelFileChangedEvent evt = new ModelFileChangedEvent(modelFile);
        for (ModelFileChangedListener listener : modelFileListeners) {
            listener.fileChanged(evt);
        }
    }

    public void setScriptFile(File scriptFile) {
        this.scriptFile = scriptFile;
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

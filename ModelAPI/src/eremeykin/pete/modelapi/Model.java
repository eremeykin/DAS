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
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

/**
 *
 * @author Pete
 */
public class Model implements ParameterChangedListener {

    private Parameter root;
    private Reader objReader;
    private BufferedReader scriptReader;
    private String script;
    private ArrayList<ReaderChangedListener> readerListeners = new ArrayList<>();
    private ArrayList<ModelChangedListener> modelListeners = new ArrayList<>();
    private File home;

    public Model(Parameter root, Reader objReader, Reader scrReader) {
        this.root = root;
        this.objReader = objReader;
        this.scriptReader = new BufferedReader(scrReader);
    }

    public Parameter getRoot() {
        return root;
    }

    public void setHome(File home) {
        this.home = home;
    }

    public File getHome() {
        return home;
    }

    public Reader getObjReader() {
        return objReader;
    }

    public String getScript() throws IOException {
        if (script != null) {
            return script;
        }
        String line, result = "";
        while ((line = scriptReader.readLine()) != null) {
            result += line + "\r\n";
        }
        script = result;
        return result;
    }

    public void setObjReader(Reader objReader) {
        this.objReader = objReader;
        ReaderChangedEvent evt = new ReaderChangedEvent(objReader);
        for (ReaderChangedListener listener : readerListeners) {
            listener.readerChanged(evt);
        }
    }

    public TreeMap<Integer, String> getArgs() {
        TreeMap<Integer, String> args = new TreeMap<>();
        getArgsInner(args, root);
        return args;
    }

    private void getArgsInner(Map<Integer, String> args, Parameter param) {
        Integer argN = param.getScrArg();
        if (argN != null) {
            args.put(param.getScrArg(), param.getValue());
        }
        for (Parameter p : param.getChildren()) {
            getArgsInner(args, p);
        }
    }

    public Parameter getParameterByID(Parameter rootParameter, Integer id) {
        for (Parameter p : rootParameter.getChildren()) {
            if (p.getId().equals(id)) {
                return p;
            } else {
                Parameter subParam = getParameterByID(p, id);
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

    public void addReaderChangedListener(ReaderChangedListener listener) {
        readerListeners.add(listener);
    }

    public void removeReaderChangedListener(ReaderChangedListener listener) {
        readerListeners.remove(listener);
    }

    @Override
    public void parameterChanged(ParameterChangedEvent evt) {
        ModelChangedEvent modelEvt = new ModelChangedEvent(evt.getParameterSource());
        for (ModelChangedListener listener : modelListeners) {
            listener.modelChanged(modelEvt);
        }
    }

}

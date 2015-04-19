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
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 *
 * @author Pete
 */
public class Model {

    private Parameter root;
    private Reader objReader;
    private BufferedReader scriptReader;
    private String script;

    public Model(Parameter root, Reader objReader, Reader scrReader) {
        this.root = root;
        this.objReader = objReader;
        this.scriptReader = new BufferedReader(scrReader);
    }

    public Parameter getRoot() {
        return root;
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
            result += line+"\r\n";
        }
        script=result;
        return result;
    }

    public void setObjReader(Reader objReader) {
        this.objReader = objReader;
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
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eremeykin.pete.abaqusscriptrunner;

import eremeykin.pete.coreapi.loggerapi.Logger;
import eremeykin.pete.coreapi.loggerapi.LoggerManager;
import eremeykin.pete.coreapi.workspace.WorkspaceManager;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;

/**
 *
 * @author Pete
 */
public class VersionChecker {

    private final String command;
    private String path;
    private final String args;
    private final StringBuffer version = new StringBuffer();
    private static final Logger LOGGER = LoggerManager.getLogger(WorkspaceManager.class);
    private final String keyword;

    public VersionChecker(String command, String[] args, String path, String keyword) {
        this.command = command;
        String[] aux = new String[args.length];
        for (int i = 0; i < args.length; i++) {
            aux[i] = " " + args[i] + " ";
        }
        this.args = arrToStr(aux);
        this.path = path;
        this.keyword = keyword;
    }

    private String arrToStr(String[] arr) {
        StringBuilder builder = new StringBuilder();
        for (String s : arr) {
            builder.append(s);
        }
        return builder.toString();
    }

    public StringBuffer check() throws IOException, InterruptedException {
        File home = WorkspaceManager.INSTANCE.getWorkspace();
        ProcessBuilder pBuilder = new ProcessBuilder("cmd.exe", "/C", command + args);
        if (path == null) {
            path = "";
        }
        File file = new File(path);
        if (!file.isDirectory() && file.exists()) {
            path = file.getParent();
        }
        pBuilder.environment().put("Path", path);
        pBuilder.directory(home);
        pBuilder.redirectErrorStream(true);
        Process process = pBuilder.start();
        Thread checker = new Checker(process, keyword);
        checker.start();
        checker.join();
        return version;
    }

    private class Checker extends Thread {

        private final Process process;
        private final String keyword;

        public Checker(Process process, String keyword) {
            this.process = process;
            this.keyword = keyword;
        }

        @Override
        public void run() {
            String line;
            try (BufferedReader bri = new BufferedReader(new InputStreamReader(process.getInputStream()));) {
                while ((line = bri.readLine()) != null) {
                    if (line.contains(keyword)) {
                        version.append(line);
                        process.destroyForcibly();
                        return;
                    }
                }
            } catch (IOException ex) {
                LOGGER.debug(ex);
            }
        }
    }
}

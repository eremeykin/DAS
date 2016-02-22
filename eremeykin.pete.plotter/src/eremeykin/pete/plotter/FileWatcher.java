/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eremeykin.pete.plotter;

import eremeykin.pete.api.core.logger.Logger;
import eremeykin.pete.api.core.logger.LoggerManager;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import javax.swing.JOptionPane;
import org.openide.util.Exceptions;

/**
 *
 * @author Pete
 */
class FileWatcher extends Thread {

    AtomicBoolean stop = new AtomicBoolean(false);
    private static final Logger LOGGER = LoggerManager.getLogger(FileWatcher.class);
    private final Updateable callback;
    
    public interface Updateable{
        File home();
        void update();
    }
    
    public FileWatcher(Updateable callback) {
        this.callback = callback;
    }

    public void stopWatch() {
        stop.set(true);
    }

    @Override
    public void run() {
        stop = new AtomicBoolean(false);
        callback.update();
//        updateDataset(home);
        while (!stop.get()) {
            try {
                Path myDir = callback.home().toPath();
                try {
                    WatchService watcher = myDir.getFileSystem().newWatchService();
                    myDir.register(watcher, StandardWatchEventKinds.ENTRY_MODIFY);

                    WatchKey watckKey = watcher.take();

                    List<WatchEvent<?>> events = watckKey.pollEvents();
                    for (WatchEvent event : events) {
                        if (event.kind() == StandardWatchEventKinds.ENTRY_MODIFY) {
                            callback.update();
//                            updateDataset(home);
//                                refershData();
                        }
                    }
                } catch (IOException e) {
                    LOGGER.error("IO Error while watching file");
                    JOptionPane.showMessageDialog(null, "Error while watching the result file.", "Error", JOptionPane.ERROR_MESSAGE);
                } catch (InterruptedException e) {
                    LOGGER.error("Watching file thread was interrupted. ");
                    JOptionPane.showMessageDialog(null, "Watching file thread was interrupted.", "Error", JOptionPane.ERROR_MESSAGE);
                }

            } catch (Exception exc) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ex) {
                    Exceptions.printStackTrace(ex);
                }
            }
        }
    }
}

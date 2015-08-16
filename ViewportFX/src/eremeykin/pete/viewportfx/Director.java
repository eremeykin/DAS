/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eremeykin.pete.viewportfx;

import java.io.File;
import java.net.URL;
import javafx.scene.Scene;

/**
 *
 * @author Pete
 */
public class Director {

    private SceneBuilder sceneBuilder;

    public void setSceneBuilder(SceneBuilder builder) {
        sceneBuilder = builder;
    }

    public void buildScene(File modelFile) {
        sceneBuilder.buildCamera();
        sceneBuilder.buildModel(modelFile);
        sceneBuilder.buildAxes();
        sceneBuilder.buildScene();
    }
    
    public  void buildScene(){
        buildScene(null);
    }

    public Scene getScene() {
        return sceneBuilder.getScene();
    }
}

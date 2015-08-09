/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eremeykin.pete.viewportfx;

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

    public void buildScene() {
        sceneBuilder.buildCamera();
        sceneBuilder.buildModel();
        sceneBuilder.buildAxes();
        sceneBuilder.buildScene();
    }

    public Scene getScene() {
        return sceneBuilder.getScene();
    }
}

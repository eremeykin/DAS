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
public abstract class SceneBuilder {

    protected  Scene scene;

    public abstract void buildCamera();

    public abstract void buildModel();

    public abstract void buildAxes();

    public abstract void buildScene();

    public Scene getScene() {
        return scene;
    }

}

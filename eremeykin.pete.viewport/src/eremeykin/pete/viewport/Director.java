///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//package eremeykin.pete.viewport;
//
//import java.io.File;
//import java.net.URL;
//import javafx.scene.Camera;
//import javafx.scene.Scene;
//import javafx.scene.control.Control;
//
///**
// *
// * @author Pete
// */
//class Director {
//
//    private SceneBuilder sceneBuilder;
//
//    public void setSceneBuilder(SceneBuilder builder) {
//        sceneBuilder = builder;
//    }
//
//    public void buildScene(File modelFile) {
//        sceneBuilder.buildCamera();
//        sceneBuilder.buildModel(modelFile);
//        sceneBuilder.buildAxes();
//        sceneBuilder.buildScene();
//    }
//    
//    public void buildScene(File modelFile, Camera camera) {
//        sceneBuilder.buildCamera();
//        sceneBuilder.buildModel(modelFile);
//        sceneBuilder.buildAxes();
//        sceneBuilder.buildScene();
//    }
//    
//    public  void buildScene(){
//        buildScene(null);
//    }
//    
//    public Scene getScene() {
//        return sceneBuilder.getScene();
//    }
//}

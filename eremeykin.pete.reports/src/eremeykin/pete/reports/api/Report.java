/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eremeykin.pete.reports.api;

import java.util.List;
import java.util.ArrayList;
import javafx.scene.Scene;

/**
 *
 * @author Pete
 */
public class Report {

    private Report() {
    }
    
    private static final Report instance = new Report();

    public static Report getInstance(){
        return instance;
    }
    
    private Scene scene;
}

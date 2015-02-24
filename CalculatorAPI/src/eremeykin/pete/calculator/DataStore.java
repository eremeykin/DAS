/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eremeykin.pete.calculator;

/**
 *
 * @author Pete
 */
public interface DataStore {

    public String getValueById(String string);

    public void setValueById(String string, String string1);
}

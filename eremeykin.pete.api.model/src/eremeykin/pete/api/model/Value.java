/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eremeykin.pete.api.model;

/**
 * used in DAO (ex. XlsxValueDao)
 * @author eremeykin@gmail.com
 */
public class Value {
    
    private final String key;
    private final String value;
    

    public Value(String key,String value) {
        this.value = value;
        this.key = key;
    }    

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return value;
    }
    
    
    
}

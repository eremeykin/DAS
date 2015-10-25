/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eremeykin.pete.modelloader.dao.entry;

import java.util.AbstractMap.SimpleEntry;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author eremeykin@gmail.com
 */
class Entry<T> {

    private Map<T, String> storage = new HashMap<>();

    protected Entry(SimpleEntry<T, String>... pairs) {
        for (SimpleEntry<T, String> pair : pairs) {
            T key = pair.getKey();
            String value = pair.getValue();
            storage.put(key, value);
        }
    }
    
    @Override
    public String toString() {
        StringBuffer result = new StringBuffer();
        result.append("Entry[");
        for (Map.Entry<T, String> entrySet : storage.entrySet()) {
            T key = entrySet.getKey();
            String value = entrySet.getValue();
            result.append("<"+key+","+value+">");
        }            
        result.append("]");
        return result.toString();
    }
    
    public String get(T key) {
        return storage.get(key);
    }
}

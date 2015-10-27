/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eremeykin.pete.modelloader.dao.entry;

import java.util.AbstractMap.SimpleEntry;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author eremeykin@gmail.com
 */
class Entry<T> {

    private final Map<T, String> storage = new HashMap<>();

    protected Entry(SimpleEntry<T, String>... pairs) {
        for (SimpleEntry<T, String> pair : pairs) {
            T key = pair.getKey();
            String value = pair.getValue();
            storage.put(key, value);
        }
    }
    
    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append("Entry[");
        for (Map.Entry<T, String> entrySet : storage.entrySet()) {
            T key = entrySet.getKey();
            String value = entrySet.getValue();
            result.append("<").append(key).append(",").append(value).append(">");
        }            
        result.append("]");
        return result.toString();
    }
    
    public String get(T key) {
        return storage.get(key);
    }
}

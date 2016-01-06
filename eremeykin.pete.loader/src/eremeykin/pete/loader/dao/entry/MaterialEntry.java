/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eremeykin.pete.loader.dao.entry;

import static eremeykin.pete.loader.dao.entry.MaterialEntry.Field.NAME;
import static eremeykin.pete.loader.dao.entry.MaterialEntry.Field.POISSON;
import static eremeykin.pete.loader.dao.entry.MaterialEntry.Field.YOUNG;
import java.util.AbstractMap.SimpleEntry;

/**
 *
 * @author eremeykin@gmail.com
 */
public class MaterialEntry extends Entry<MaterialEntry.Field> {

    public MaterialEntry(String name, String young, String poisson) {
        super(new SimpleEntry<>(NAME, name), new SimpleEntry<>(YOUNG, young), new SimpleEntry<>(POISSON, poisson));
    }

    public enum Field {

        NAME,
        YOUNG,
        POISSON
    }

}

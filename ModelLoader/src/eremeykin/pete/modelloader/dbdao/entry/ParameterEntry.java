/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eremeykin.pete.modelloader.dbdao.entry;

import eremeykin.pete.modelloader.dbdao.entry.ParameterEntry.Field;
import static eremeykin.pete.modelloader.dbdao.entry.ParameterEntry.Field.*;
import java.util.AbstractMap.SimpleEntry;

/**
 *
 * @author eremeykin@gmail.com
 */
public class ParameterEntry extends Entry<Field> {

    public ParameterEntry(String id, String name, String parent,
            String scriptArg, String value, String comment,
            String editorType, String master, String editorTable, String editorColumn) {
        super(new SimpleEntry<>(ID, id),
                new SimpleEntry<>(NAME, name),
                new SimpleEntry<>(PARENT, parent),
                new SimpleEntry<>(SCRIPT_ARG, scriptArg),
                new SimpleEntry<>(VALUE, value),
                new SimpleEntry<>(COMMENT, comment),
                new SimpleEntry<>(EDITOR_TYPE, editorType),
                new SimpleEntry<>(MASTER, master),
                new SimpleEntry<>(EDITOR_TABLE, editorTable),
                new SimpleEntry<>(EDITOR_COLUMN, editorColumn));

    }



    public enum Field {

        ID,
        NAME,
        PARENT,
        SCRIPT_ARG,
        VALUE,
        COMMENT,
        EDITOR_TYPE,
        MASTER,
        EDITOR_TABLE,
        EDITOR_COLUMN
    }

}

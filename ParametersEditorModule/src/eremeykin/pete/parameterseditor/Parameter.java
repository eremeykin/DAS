/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eremeykin.pete.parameterseditor;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.JTextField;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

/**
 *
 * @author eremeykin
 */
public class Parameter {

    private final Integer id;
    private final String name;
    private Parameter parent;
    private final Integer scriptArg;
    private String value;
    private final String comment;
    private final CellProperties<TableCellEditor, TableCellRenderer> cProperties;
    private List<Parameter> children = new ArrayList<>();

    public Parameter(Integer id, String name, Integer scriptArg, String comment, CellProperties<TableCellEditor, TableCellRenderer> cProperties) {
        this.id = id;
        this.name = name;
        this.scriptArg = scriptArg;
        this.comment = comment;
        this.cProperties = cProperties;
    }

    public String getComment() {
        return this.comment;
    }

    public String getValue() {
        return this.value;
    }

    public Integer getId() {
        return id;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public List<Parameter> getChildren() {
        return this.children;
    }

    public void setChildren(List<Parameter> children) {
        children.sort(new Comparator<Parameter>() {

            @Override
            public int compare(Parameter o1, Parameter o2) {
                if (o1.id > o2.id) {
                    return 1;
                } else if (o1.id < o2.id) {
                    return -1;
                } else {
                    return 0;
                }
            }
        });
        this.children = children;
    }

    TableCellEditor getEditor() {
        return cProperties.getEditor();
    }

    TableCellRenderer getRenderer() {
        return cProperties.getRenderer();
    }

    @Override
    public String toString() {
        return name; //To change body of generated methods, choose Tools | Templates.
    }

    public static final class CellProperties<E, R> {

        E editor;
        R renderer;

        CellProperties(E editor, R renderer) {
            this.editor = editor;
            this.renderer = renderer;
        }

        public CellProperties() {
            this.editor = null;
            this.renderer = null;
        }

        private E getEditor() {
            return editor;
        }

        private R getRenderer() {
            return renderer;
        }
    ;
}

}

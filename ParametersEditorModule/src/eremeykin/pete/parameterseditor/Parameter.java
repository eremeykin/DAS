/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eremeykin.pete.parameterseditor;

import java.sql.Array;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
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
    private List<Parameter> slaveParameters = new ArrayList<>();//parameters that will auto edit its content
    private final Integer scriptArg;
    private String value;
    private final String comment;
    private final CellProperties<TableCellEditor, TableCellRenderer> cProperties;
    private List<Parameter> children = new ArrayList<>();
    private Decorator dec;

    public Parameter(Integer id, String name, Integer scriptArg, String comment, CellProperties<TableCellEditor, TableCellRenderer> cProperties) {
        this.id = id;
        this.name = name;
        this.scriptArg = scriptArg;
        this.comment = comment;
        this.cProperties = cProperties;
    }

    public Parameter(Parameter p) {
        this.id = p.id;
        this.name = p.name;
        this.parent = p.parent;
        this.slaveParameters = p.slaveParameters;
        this.scriptArg = p.scriptArg;
        this.value = p.value;
        this.comment = p.comment;
        this.cProperties = p.cProperties;
        this.children = p.children;
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
        for (Parameter p : slaveParameters) {
            p.dec.perform(value);
        }
        this.value = value;
    }

    public List<Parameter> getChildren() {
        return this.children;
    }

    public void addSlaveParameter(Parameter p) {
        this.slaveParameters.add(p);
    }

    public void masterChangedValue(Object newValue) {
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

    void setDecorator(Decorator dec) {
        this.dec = dec;
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

     static class Decorator {

        Parameter p;

        public Decorator(Parameter p) {
            this.p = p;
        }

        void perform(Object newValue) {

        }
    }
}

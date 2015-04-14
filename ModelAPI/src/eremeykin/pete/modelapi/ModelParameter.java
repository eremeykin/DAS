/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eremeykin.pete.modelapi;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

/**
 *
 * @author eremeykin
 */
public class ModelParameter implements Parameter {

    private final Integer id;
    private final String name;
    private ModelParameter parent;
    private List<Parameter> slaveParameters = new ArrayList<>();//parameters that will auto edit its content
    private final Integer scriptArg;
    private String value;
    private final String comment;
    private final CellProperties<TableCellEditor, TableCellRenderer> cProperties;
    private List<Parameter> children = new ArrayList<>();
    private Updater updater;

    public ModelParameter(Integer id, String name, Integer scriptArg, String comment, CellProperties<TableCellEditor, TableCellRenderer> cProperties) {
        this.id = id;
        this.name = name;
        this.scriptArg = scriptArg;
        this.comment = comment;
        this.cProperties = cProperties;
    }

    ModelParameter(ModelParameter p) {
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

    @Override
    public String getComment() {
        return this.comment;
    }

    @Override
    public String getValue() {
        return this.value;
    }

    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public void setValue(String value) {
        for (Parameter p : slaveParameters) {
            p.updateValue(value);
        }
        this.value = value;
    }

    @Override
    public List<Parameter> getChildren() {
        return this.children;
    }

    @Override
    public void addSlaveParameter(Parameter p) {
        this.slaveParameters.add(p);
    }

    @Override
    public void setChildren(List<Parameter> children) {
        children.sort(new Comparator<Parameter>() {

            @Override
            public int compare(Parameter o1, Parameter o2) {
                if (o1.getId() > o2.getId()) {
                    return 1;
                } else if (o1.getId() < o2.getId()) {
                    return -1;
                } else {
                    return 0;
                }
            }
        });
        this.children = children;
    }

    @Override
    public TableCellEditor getEditor() {
        return cProperties.getEditor();
    }

    @Override
    public TableCellRenderer getRenderer() {
        return cProperties.getRenderer();
    }

    @Override
    public String toString() {
        return name; //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setUpdater(Updater updater) {
        this.updater = updater;
    }

    @Override
    public Updater getUpdater() {
        return updater;
    }

    public static final class CellProperties<E, R> {

        E editor;
        R renderer;

        public CellProperties(E editor, R renderer) {
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

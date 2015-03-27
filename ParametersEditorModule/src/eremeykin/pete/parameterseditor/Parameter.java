/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eremeykin.pete.parameterseditor;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.DefaultCellEditor;
import javax.swing.JTextField;
import javax.swing.table.TableCellEditor;
import javax.swing.tree.DefaultTreeCellEditor;

/**
 *
 * @author eremeykin
 */
public class Parameter {

    private Integer id;
    private String name;
    private Parameter parent;
    private Integer scriptArg;
    private String value;
    private String comment;
    private String editor;
    private List<Parameter> children = new ArrayList<>();

    public Parameter(Integer id, String name, Integer scriptArg, String comment, String editor) {
        this.id = id;
        this.name = name;
        this.scriptArg = scriptArg;
        this.comment = comment;
        this.editor = editor;
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

    public DefaultCellEditor getEditor() {
        return new DefaultCellEditor(new JTextField());
    }

    @Override
    public String toString() {
        return name; //To change body of generated methods, choose Tools | Templates.
    }

}

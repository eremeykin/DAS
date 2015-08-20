/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eremeykin.pete.modelapi;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

/**
 *
 * @author eremeykin
 */
public class ModelParameter implements ModelParameterChangedListener {

    private final Integer id;
    private final String name;
    private ModelParameter parent;
    private final Integer scriptArg;
    private String value;
    private final String comment;
    private final CellProperties cProperties;
    private List<ModelParameter> children = new ArrayList<>();
    private ArrayList<ModelParameterChangedListener> listeners = new ArrayList<>();
    private Updater updater;

    public static interface Updater {

        public void update(String value);
    }

    public ModelParameter(Integer id, String name, Integer scriptArg, String comment, CellProperties cProperties) {
        this.id = id;
        this.name = name;
        this.scriptArg = scriptArg;
        this.comment = comment;
        this.cProperties = cProperties;
        if (cProperties != null && cProperties.getEditor() != null && cProperties.getEditor().getType() == CellProperties.Editor.Type.COMBO_BOX) {
            this.value = cProperties.getEditor().getAvailableValues()[0].toString();
        }
    }

    ModelParameter(ModelParameter p) {
        this.id = p.id;
        this.name = p.name;
        this.parent = p.parent;
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
        this.value = value;
        ModelParameterChangedEvent evt = new ModelParameterChangedEvent(this);
        for (ModelParameterChangedListener listener : listeners) {
            listener.parameterChanged(evt);
        }
    }

    public List<ModelParameter> getChildren() {
        return this.children;
    }

    public void setChildren(List<ModelParameter> children) {
        children.sort(new Comparator<ModelParameter>() {

            @Override
            public int compare(ModelParameter o1, ModelParameter o2) {
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

    public CellProperties.Editor getEditor() {
        return cProperties.getEditor();
    }

    public CellProperties.Renderer getRenderer() {
        return cProperties.getRenderer();
    }

    @Override
    public String toString() {
        return name; //To change body of generated methods, choose Tools | Templates.
    }

    public Integer getScrArg() {
        return scriptArg;
    }

    public void addParameterChangedListener(ModelParameterChangedListener listener) {
        listeners.add(listener);
    }

    public void removeParameterChangedListener(ModelParameterChangedListener listener) {
        listeners.remove(listener);
    }

    public void setUpdater(Updater updater) {
        this.updater = updater;
    }

    public void parameterChanged(ModelParameterChangedEvent evt) {
        if (this.updater != null) {
            updater.update(evt.getParameterSource().getValue());
        }
    }

    public static final class CellProperties {

        public static class Editor {

            private final Type type;
            private final List availableValues = new ArrayList();

            public Type getType() {
                return type;

            }

            public Editor(Type type, Object... availableValues) {
                this.type = type;
                this.availableValues.addAll(Arrays.asList(availableValues));
            }

            public Object[] getAvailableValues() {
                return availableValues.toArray();
            }

            public enum Type {

                DEFAULT,
                COMBO_BOX,
                TEXT_BOX
            }
        }

        public static class Renderer {

            private final Type type;

            public Renderer(Type type) {
                this.type = type;
            }

            public Type getType() {
                return type;
            }

            public enum Type {

                DEFAULT,
                COMBO_BOX
            }

        }
        Editor editor;
        Renderer renderer;

        public CellProperties(Editor e, Renderer r) {
            this.editor = e;
            this.renderer = r;
        }

//        public CellProperties() {
//            this.editor = null;
//            this.renderer = null;
//        }
        private Editor getEditor() {
            return editor;
        }

        private Renderer getRenderer() {
            return renderer;
        }
    ;

}
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eremeykin.pete.modelapi;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

/**
 *
 * @author eremeykin
 */
public final class ModelParameter implements ModelParameterChangedListener {

    private final Integer id;
    private final Integer master;
    private final String name;
    private ModelParameter parent;
    private final String scriptArg;
    private String value;
    private final String comment;
    private final CellProperties cProperties;
    private List<ModelParameter> children = new ArrayList<>();
    private ArrayList<ModelParameterChangedListener> slaves = new ArrayList<>();
    private Updater updater;

    public static interface Updater {

        public void update(ModelParameter master);
    }

    public ModelParameter(Integer id, String name, String scriptArg, String comment, Integer master, CellProperties cProperties) {
        this.id = id;
        this.master = master;
        this.name = name;
        this.scriptArg = scriptArg;
        this.comment = comment;
        this.cProperties = cProperties;
        if (cProperties != null && cProperties.getEditor() != null && cProperties.getEditor().getType() == CellProperties.Editor.Type.COMBO_BOX) {
            Value availableValue = cProperties.getEditor().getAvailableValues()[0];
            if (availableValue != null) {
                this.value = availableValue.toString();
            }
        }
        if (cProperties != null && cProperties.getEditor() != null && cProperties.editor.getType().equals(CellProperties.Editor.Type.AUTO)) {
            this.setUpdater(new ModelParameter.Updater() {

                @Override
                public void update(ModelParameter master) {
                    String value = master.getValue();
                    CellProperties.Editor editor = ModelParameter.this.getEditor();
                    Value[] availableValues = editor.getAvailableValues();
                    for (Value v : availableValues) {
                        if (v.getKey().equals(value)) {
                            ModelParameter.this.setValue(v.getValue());
                            return;
                        }
                    }
                }
            });
        }
    }

    public Integer getMaster() {
        return master;
    }

//TODO: what is it?
    ModelParameter(ModelParameter p) {
        this.id = p.id;
        this.name = p.name;
        this.parent = p.parent;
        this.scriptArg = p.scriptArg;
        this.value = p.value;
        this.comment = p.comment;
        this.cProperties = p.cProperties;
        this.children = p.children;
        this.master = p.master;
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
        NumberFormat formatter = new DecimalFormat("#0.0###");
        Double d = null;
        String res = null;
        try {
            d = Double.valueOf(value);
            if (d > 1000) {
                formatter = new DecimalFormat("0.0##E0");
            }
            res = formatter.format(d);
        } catch (IllegalArgumentException ex2) {
        }

        this.value = res == null ? value : res;
        ModelParameterChangedEvent evt = new ModelParameterChangedEvent(this);
        for (ModelParameterChangedListener listener : slaves) {
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

    public void addChild(ModelParameter child) {
        children.add(child);
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
    }

    public CellProperties.Editor getEditor() {
        return cProperties.getEditor();
    }

    @Override
    public String toString() {
        return name; //To change body of generated methods, choose Tools | Templates.
    }

    public String getScrArg() {
        return scriptArg;
    }

    public void addParameterChangedListener(ModelParameterChangedListener listener) {
        slaves.add(listener);
    }

    public void removeParameterChangedListener(ModelParameterChangedListener listener) {
        slaves.remove(listener);
    }

    public void setUpdater(Updater updater) {
        this.updater = updater;
    }

    @Override
    public void parameterChanged(ModelParameterChangedEvent evt) {
        if (this.updater != null) {
            updater.update(evt.getParameterSource());
        }
    }

    public static final class CellProperties {

        public static class Editor {

            private final Type type;
            private final List<Value> availableValues = new ArrayList<>();

            public Type getType() {
                return type;

            }

            public Editor(Type type, Value... availableValues) {
                this.type = type;
                this.availableValues.addAll(Arrays.asList(availableValues));
            }

            public Value[] getAvailableValues() {
                Value[] a = new Value[1];
                a = availableValues.toArray(a);
                return a;
            }

            public enum Type {

                DEFAULT,
                COMBO_BOX,
                TEXT_BOX,
                AUTO
            }
        }

        final Editor editor;

        public CellProperties(Editor e) {
            this.editor = e;
        }

//        public CellProperties() {
//            this.editor = null;
//            this.renderer = null;
//        }
        private Editor getEditor() {
            return editor;
        }

    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eremeykin.pete.modelapi;

import java.util.List;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

/**
 *
 * @author Pete
 */
public interface Parameter {
    
    public static interface Updater {
        public void update(String value);
    }
    
    public String getComment();

    public String getValue();

    public Integer getId();

    public Integer getScrArg();

    public void setValue(String value);

    public List<Parameter> getChildren();

    public void setChildren(List<Parameter> children);

    public TableCellEditor getEditor();

    public TableCellRenderer getRenderer();

    public void addParameterChangedListener(ParameterChangedListener listener);

    public void removeParameterChangedListener(ParameterChangedListener listener);
    
    public void setUpdater(Updater updater);
}

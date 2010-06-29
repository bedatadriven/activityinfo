package org.sigmah.client.page.config.form;

import com.allen_sauer.gwt.log.client.Log;
import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.FieldEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.widget.Component;
import com.extjs.gxt.ui.client.widget.Container;
import com.extjs.gxt.ui.client.widget.form.*;
import org.sigmah.client.page.common.widget.MappingComboBox;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author Alex Bertram (akbertram@gmail.com)
 */
public class ModelFormPanel extends FormPanel {
    
    protected Map<String, Field> fields = new HashMap<String, Field>();

    protected Listener<FieldEvent> keypressListener;
    protected Listener<FieldEvent> changeListener;

    protected Set<String> dirtyProperties = new HashSet<String>();

    public ModelFormPanel() {
        keypressListener = new Listener<FieldEvent>() {
            @Override
            public void handleEvent(FieldEvent be) {
                dirtyProperties.add(be.getField().getName());
                onDirtyFlagChanged(true);
            }
        };

        changeListener = new Listener<FieldEvent>() {
            @Override
            public void handleEvent(FieldEvent be) {
                if(be.getField().isDirty()) {
                    dirtyProperties.add(be.getField().getName());
                } else {
                    dirtyProperties.remove(be.getField().getName());
                }

                onDirtyFlagChanged(dirtyProperties.size() != 0);
            }
        };
    }

    protected void registerField(Field<?> field) {

        Log.debug("ModelForm: registering " + field.getName());

        assert field.getName() != null : "field name cannot be null!";
        
        fields.put(field.getName(), field);

        if(field instanceof TextField) {
            field.addListener(Events.KeyPress, keypressListener);
        } else if(field instanceof ComboBox) {
           field.addListener(Events.Select, changeListener);
        }
        field.addListener(Events.Change, changeListener);

    }

    protected void registerField(MultiField<?> multiField) {

        for(Field<?> field : multiField.getAll()) {
            if(field.getName() != null) {
                registerField(field);
            }
        }
    }

    protected void registerFieldSet(Container<?> container) {
        registerFieldSet(container, false);
    }

    protected void registerFieldSet(Container<?> container, boolean addChildren) {
        for(Component c : container.getItems()) {
            if(addChildren && c instanceof MultiField) {
                registerField((MultiField)c);    
            } else if(c instanceof Field) {
                Field f = (Field)c;
                if( f.getName() != null) {
                    registerField(f);
                }
            } else if(addChildren && c instanceof Container) {
                registerFieldSet((Container<?>)c, addChildren);
            }
        }
    }

    protected void registerAll() {
        registerFieldSet(this, true);
    }
    
    protected void updateForm(ModelData model) {

        // update the field widgets
        for(Map.Entry<String, Field> entry : fields.entrySet()) {
            Field field = entry.getValue();
            Object value =  model.get(entry.getKey());

            setFieldValue(field, value);
        }
    }

    private void setFieldValue(Field field, Object value) {
        if(field instanceof MappingComboBox) {
            ((MappingComboBox)field).setMappedValue(value);
            ((MappingComboBox)field).setOriginalMappedValue(value);
        } else {
            field.setOriginalValue(value);
            field.setValue(value);
        }
        field.clearInvalid();
    }

    protected Object getFieldValue(Field f) {
        if(f instanceof MappingComboBox) {
            return ((MappingComboBox) f).getMappedValue();
        } else {
            return f.getValue();
        }
    }

    protected void onDirtyFlagChanged(boolean isDirty) {
    

    }

    @Override
    public boolean isDirty() {
        return dirtyProperties.size() != 0;
    }

    protected Map<String, Object> getAllValues() {

        Map<String, Object> map = new HashMap<String, Object>();
        for(Map.Entry<String,Field> entry :  fields.entrySet()) {
            map.put(entry.getKey(), getFieldValue(entry.getValue()));
        }
        return map;
    }

    protected Map<String, Object> getModified() {
        Map<String, Object> changes = new HashMap<String, Object>();

        for(String property : dirtyProperties) {
            Field field = fields.get(property);
            changes.put(property, getFieldValue(field));
        }

        Log.debug(changeLog(changes));

        return changes;
    }

    private String changeLog(Map<String, Object> changes) {
        StringBuilder sb = new StringBuilder();
        sb.append("ModelFormPanel: getModified has 3 ").append(changes.size()).append(" dirty properties");

        for(Map.Entry<String, Object> entry : changes.entrySet()) {
            sb.append("\n").append(entry.getKey()).append(" = ").append(
                    entry.getValue() == null ? "null" : entry.getValue().toString());
        }

        return sb.toString();
    }

    @Override
    public boolean isValid(boolean preventMark) {
        for(Field f : fields.values()) {
            if(!f.isValid(preventMark)) {
                return false;
            }
        }
        return true;
    }

    public boolean highlightInvalid(FieldSet fieldSet) {
		for(Component component : fieldSet.getItems()) {
			if(component instanceof Field) {
				if(!((Field)component).validate()) {

					this.scrollIntoView(component);
					component.focus();
					return false;
				}
			}
		}
		return true;
	}

    public void testingSetValue(String name, Object value) {
        setFieldValue(fields.get(name),value);
        changeListener.handleEvent(new FieldEvent(fields.get(name)));
    }
}

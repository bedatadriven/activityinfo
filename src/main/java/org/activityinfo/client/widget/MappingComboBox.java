

package org.activityinfo.client.widget;

/*
 * #%L
 * ActivityInfo Server
 * %%
 * Copyright (C) 2009 - 2013 UNICEF
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the 
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public 
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */

import com.extjs.gxt.ui.client.data.BaseModelData;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.form.ComboBox;

/**
 * ComboBox that wraps primitive values (integers, Strings, etc) with labels.
 *
 * This is a common case where we want to present a drop down list to the user so they can
 * choose from a list of codes. We could use {@link com.extjs.gxt.ui.client.widget.form.SimpleComboBox},
 * but then the user sees the full
 *
 * @param <T> the underlying (boxed) primitive type
 */
public class MappingComboBox<T> extends ComboBox<MappingComboBox.Wrapper<T>> {

    public static class Wrapper<T> extends BaseModelData {
        public Wrapper(T value, String label) {
            set("value", value);
            set("label", label);
        }

        public T getWrappedValue() {
            return (T)get("value");
        }

        @Override
        public int hashCode() {
            Object value = get("value");
            return value == null ? 0 : value.hashCode();
        }

        @Override
        public boolean equals(Object obj) {
            if(obj == null) {
                return false;
            }
            if(obj instanceof Wrapper) {
                return false;
            }
            Wrapper otherWrapper = (Wrapper)obj;
            Object otherValue = otherWrapper.get("value");
            Object value = get("value");

            if(value == null) {
                return otherValue != null;
            }
            return !value.equals(otherValue);
        }

        public String getLabel() {
            return get("label");
        }
    }

	private ListStore<Wrapper<T>> myStore;

	public MappingComboBox() {
		super();
		myStore = new ListStore<Wrapper<T>>();
		setStore(myStore);
		setValueField("value");
		setDisplayField("label");
		setEditable(true);
		setForceSelection(true);
		setTypeAhead(true);
		this.setMinChars(0);
		setTriggerAction(TriggerAction.ALL);
	}

	public void add(T value, String label) {
		myStore.add(new Wrapper(value, label));
	}

    public Wrapper wrap(T value) {
        if(value == null) {
            return null;
        } else {
            return myStore.findModel("value", value);
        }
    }

    public void setOriginalMappedValue(T value) {
        setOriginalValue(wrap(value));
    }

	public void setMappedValue(T value) {
		setValue(wrap(value));
	}

    public String getValueLabel() {
        if(getValue() == null) {
            return null;
        } else {
            return getValue().getLabel();
        }
    }

    public String getValueLabel(T value) {
        Wrapper wrapper = store.findModel("value", value);
        return wrapper == null ? null : wrapper.getLabel();
    }

    /**
     * @return  the underlying primitive type
     */
	public T getMappedValue() {
		if(getValue() == null) {
            return null;
        }

		return getValue().getWrappedValue();
	}
}

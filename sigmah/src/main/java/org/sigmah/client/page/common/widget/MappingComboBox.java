/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.client.page.common.widget;

import com.extjs.gxt.ui.client.data.BaseModelData;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.form.ComboBox;

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



	public T getMappedValue() {
		if(getValue() == null) {
            return null;
        }

		return getValue().getWrappedValue();
	}

}

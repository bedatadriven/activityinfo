/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.shared.command.result;

import java.io.Serializable;
import java.util.List;

//import org.sigmah.server.domain.element.FlexibleElement;

/**
 * Value result containing the inner value object or the inner values list object of 
 * a {@link FlexibleElement}.
 * 
 * @author Denis Colliot (dcolliot@ideia.fr)
 */
public class ValueResult implements CommandResult {
	
	private static final long serialVersionUID = -2164809792512897349L;
	
	private Serializable valueObject; // Inner value object
	private List<Serializable> valuesObject; // Inner values list object
	private boolean isListResult;

    public ValueResult() {
    }

    public ValueResult(Serializable valueObject) {
        this.valueObject = valueObject;
    }
    
    public ValueResult(List<Serializable> valuesObject) {
        this.valuesObject = valuesObject;
    }

    /**
     * Indicates if the current object contains an inner value.
     * 
     * @return {@code true} if the current object contains an inner value, {@code false} otherwise.
     */
    public boolean isValueDefined() {
    	return (valueObject != null) || (valuesObject != null && valuesObject.size() > 0);
    }
    
	public Serializable getValueObject() {
		return valueObject;
	}
	
	public void setValueObject(Serializable valueObject) {
		this.valueObject = valueObject;
	}

	public List<Serializable> getValuesObject() {
		return valuesObject;
	}
	
	public void setValuesObject(List<Serializable> valuesObject) {
		this.valuesObject = valuesObject;
	}

	public boolean isListResult() {
		return isListResult;
	}
	
	public void setListResult(boolean isListResult) {
		this.isListResult = isListResult;
	}

	@Override
	public String toString() {
		String toString = null;
		if (isValueDefined()) {
			if (isListResult) {
				toString = valuesObject.toString();
			}
			else {
				toString = valueObject.toString();
			}
		}
		return toString;
	}
}

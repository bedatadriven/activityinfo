/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.shared.command.result;

import java.util.ArrayList;
import java.util.List;

import org.sigmah.shared.domain.element.FlexibleElement;
import org.sigmah.shared.dto.value.ListableValue;

/**
 * Value result containing the inner value object or the inner values list
 * object of a {@link FlexibleElement}.
 * 
 * @author Denis Colliot (dcolliot@ideia.fr)
 */
public class ValueResult implements CommandResult {

    private static final long serialVersionUID = -2164809792512897349L;

    /**
     * Inner values list object.
     */
    private List<ListableValue> valuesObject;

    /**
     * Single value.
     */
    private String valueObject;

    public ValueResult() {
        // Serialization.
    }

    /**
     * Instantiates the local if it isn't done yet.
     */
    private void ensureListIsNotNull() {
        if (valuesObject == null) {
            valuesObject = new ArrayList<ListableValue>();
        }
    }

    /**
     * Indicates if the current object contains a valid value.
     * 
     * @return {@code true} if the current object contains a valid value,
     *         {@code false} otherwise.
     */
    public boolean isValueDefined() {
        return (valueObject != null) || (valuesObject != null && valuesObject.size() > 0);
    }

    /**
     * Returns the unique value.
     * 
     * @return the unique value.
     */
    public String getValueObject() {
        return valueObject;
    }

    /**
     * Sets a unique value.
     * 
     * @param valueObject
     *            the value.
     */
    public void setValueObject(String valueObject) {
        this.valueObject = valueObject;
    }

    /**
     * Gets the list of values.
     * 
     * @return The list of values.
     */
    public List<ListableValue> getValuesObject() {
        return valuesObject;
    }

    /**
     * Sets the list of values.
     * 
     * @param valuesObject
     *            the new list.
     */
    public void setValuesObject(List<ListableValue> valuesObject) {
        this.valuesObject = valuesObject;
    }

    /**
     * Adds a value to the list of values.
     * 
     * @param valueObject
     *            the new value.
     */
    public void addValueObject(ListableValue valueObject) {
        ensureListIsNotNull();
        valuesObject.add(valueObject);
    }

    @Override
    public String toString() {
        return "ValueResult\n" + (valuesObject != null ? valuesObject.toString() : "") + " "
                + (valueObject != null ? valueObject.toString() : "");
    }
}

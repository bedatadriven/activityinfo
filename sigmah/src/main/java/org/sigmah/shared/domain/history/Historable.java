package org.sigmah.shared.domain.history;

import org.sigmah.shared.domain.value.ListEntity;

/**
 * Determines if an element can history of its values.
 * 
 * @author tmi
 * 
 */
public interface Historable {

    /**
     * If the element manages an history.
     * 
     * @return If the element manages an history.
     */
    public boolean isHistorable();

    /**
     * Transforms a input value as an historable value.
     * 
     * @param value
     *            The actual value.
     * @return The historable value.
     */
    public String asHistoryToken(String value);

    /**
     * Transforms a input value as an historable value.
     * 
     * @param value
     *            The actual value.
     * @return The historable value.
     */
    public String asHistoryToken(ListEntity value);
}

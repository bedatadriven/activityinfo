package org.sigmah.shared.domain.history;

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
}

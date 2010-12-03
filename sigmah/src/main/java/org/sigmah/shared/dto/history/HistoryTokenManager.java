package org.sigmah.shared.dto.history;

import com.google.gwt.user.client.ui.Widget;

/**
 * Represents a element which can render history tokens.
 * 
 * @author tmi
 * 
 */
public interface HistoryTokenManager {

    /**
     * Gets the history manager name.
     * 
     * @return The history manager name.
     */
    public String getElementLabel();

    /**
     * Renders a history token.
     * 
     * @param token
     *            The token
     * @return The rendered token (can be a {@link Widget}).
     */
    public Object renderHistoryToken(HistoryTokenDTO token);
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.sigmah.client.page;

import org.sigmah.client.ui.Tab;

/**
 * A class that implements this interface can store a reference to a tab.
 * @author Raphaël Calabro (rcalabro@ideia.fr)
 */
public interface HasTab {
    /**
     * Returns the tab hosted by this class.
     * @return The tab hosted by this class.
     */
    Tab getTab();
    /**
     * Defines or updates the tab hosted by this class.
     * @param tab The new tab.
     */
    void setTab(Tab tab);
}

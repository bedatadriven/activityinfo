/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.sigmah.client.page;

/**
 * A Page/PageState that implements this interface can be displayed in a tab.
 * @author Raphaël Calabro (rcalabro@ideia.fr)
 */
public interface TabPage {
    /**
     * Returns the title to display in the tab bar.
     * @return the title to display in the tab bar.
     */
    String getTabTitle();
}

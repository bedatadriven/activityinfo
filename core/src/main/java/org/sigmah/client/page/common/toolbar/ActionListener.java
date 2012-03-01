/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.client.page.common.toolbar;


/**
 *
 * Listens for user actions, e.g. originating from
 * {@link org.sigmah.client.page.common.toolbar.ActionToolBar}
 *
 * See {@link org.sigmah.client.page.common.toolbar.UIActions} for the list of
 * String ids.
 *
 * @author Alex Bertram
 */
public interface ActionListener {

    public void onUIAction(String actionId);
    
}

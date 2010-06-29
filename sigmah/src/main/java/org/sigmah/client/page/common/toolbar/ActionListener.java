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

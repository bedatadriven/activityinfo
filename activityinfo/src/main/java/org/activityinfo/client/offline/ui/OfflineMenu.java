package org.activityinfo.client.offline.ui;

import com.extjs.gxt.ui.client.event.MenuEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.menu.Menu;
import com.extjs.gxt.ui.client.widget.menu.MenuItem;
import com.google.inject.Inject;
import org.activityinfo.client.AppEvents;
import org.activityinfo.client.Application;
import org.activityinfo.client.EventBus;
import org.activityinfo.client.dispatch.remote.Authentication;

/**
 * @author Alex Bertram
 */
public class OfflineMenu extends Button {

    protected EventBus eventBus;
    protected Authentication auth;

    public OfflineMenu() {

    }

    @Inject
    public void setEventBus(EventBus eventBus) {
        this.eventBus = eventBus;
    }

    @Inject
    public void setAuth(Authentication auth) {
        this.auth = auth;
    }

    @Override
    protected void beforeRender() {
        super.beforeRender();
        init();
    }

    protected void init() {
        //this.setText(Application.offlineMode());
        this.setText(Application.CONSTANTS.offlineMode());

        Menu menu = new Menu();
        MenuItem statusItem = new MenuItem(Application.CONSTANTS.status(), new SelectionListener<MenuEvent>() {
            @Override
            public void componentSelected(MenuEvent ce) {
                eventBus.fireEvent(AppEvents.ShowOfflineStatus);

            }
        });
        menu.add(statusItem);

        MenuItem disableItem = new MenuItem(Application.CONSTANTS.disableOffline(), new
                SelectionListener<MenuEvent>() {
                    @Override
                    public void componentSelected(MenuEvent ce) {
                        eventBus.fireEvent(AppEvents.DisableOfflineMode);
                    }
                });
        menu.add(disableItem);

        this.setMenu(menu);

    }
}

package org.activityinfo.client.offline.ui;

import com.extjs.gxt.ui.client.event.Observable;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.menu.Menu;
import com.extjs.gxt.ui.client.widget.menu.MenuItem;
import com.google.inject.Singleton;
import org.activityinfo.client.Application;
import org.activityinfo.client.offline.OfflineManager;

/**
 * @author Alex Bertram
 */
@Singleton
public class OfflineMenu extends Button implements OfflineManager.View {

    private MenuItem enableOfflineModeMenuItem;

    public OfflineMenu() {
        enableOfflineModeMenuItem = new MenuItem(Application.CONSTANTS.enableOffline());

        Menu menu = new Menu();
        menu.add(enableOfflineModeMenuItem);

        this.setMenu(menu);
        this.setText(Application.CONSTANTS.offlineMode());
    }

    @Override
    public Observable getEnableOfflineModeMenuItem() {
        return enableOfflineModeMenuItem;
    }
}

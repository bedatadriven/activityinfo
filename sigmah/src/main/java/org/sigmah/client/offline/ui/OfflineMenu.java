/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.client.offline.ui;

import com.extjs.gxt.ui.client.event.Observable;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.menu.Menu;
import com.extjs.gxt.ui.client.widget.menu.MenuItem;
import com.google.inject.Singleton;
import org.sigmah.client.i18n.I18N;
import org.sigmah.client.offline.OfflineManager;

/**
 * @author Alex Bertram
 */
@Singleton
public class OfflineMenu extends Button implements OfflineManager.View {

    private MenuItem enableOfflineModeMenuItem;

    public OfflineMenu() {
        enableOfflineModeMenuItem = new MenuItem(I18N.CONSTANTS.enableOffline());

        Menu menu = new Menu();
        menu.add(enableOfflineModeMenuItem);

        this.setMenu(menu);
        this.setText(I18N.CONSTANTS.offlineMode());
    }

    @Override
    public Observable getEnableOfflineModeMenuItem() {
        return enableOfflineModeMenuItem;
    }
}

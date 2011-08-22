/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.client.offline.ui;

import org.sigmah.client.i18n.I18N;

import com.bedatadriven.rebar.appcache.client.AppCache;
import com.bedatadriven.rebar.appcache.client.AppCacheFactory;
import com.extjs.gxt.ui.client.event.MenuEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.menu.Menu;
import com.extjs.gxt.ui.client.widget.menu.MenuItem;
import com.google.gwt.i18n.client.Dictionary;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class AppCacheMenu extends Button {

    private AppCache appCache;
    private AppCache.Status status;
    private MenuItem statusItem;

    public AppCacheMenu() {
        super( I18N.MESSAGES.versionedActivityInfoTitle(getRevision()));
        appCache = AppCacheFactory.get();

        Menu menu = new Menu();

        statusItem = new MenuItem(I18N.CONSTANTS.status(), new SelectionListener<MenuEvent>() {
            @Override
            public void componentSelected(MenuEvent menuEvent) {
                onStatusClicked();
            }
        });
        menu.add(statusItem);

        setMenu(menu);

//        new Timer() {
//            @Override
//            public void run() {
//                AppCache.Status currentStatus = appCache.getStatus();
//                if(currentStatus != status) {
//                    status = currentStatus;
//                    onStatusChanged();
//                }
//            }
//        }.scheduleRepeating(2000);
    }

    private static String getRevision() {
        Dictionary versionInfo = Dictionary.getDictionary("VersionInfo");
        return versionInfo.get("revision");
    }

    private void onStatusChanged() {
        switch (status) {
            case UNSUPPORTED:
                statusItem.setText("Download the Google Chrome browser");
                break;
            case UNCACHED:
                statusItem.setText(I18N.CONSTANTS.appcacheUncached());
                break;
            case OBSOLETE:
            case IDLE:
                statusItem.setText(I18N.CONSTANTS.appcacheUptodate());
                break;
            case CHECKING:
                statusItem.setText(I18N.CONSTANTS.appcacheChecking());
                break;
            case DOWNLOADING:
                statusItem.setText(I18N.CONSTANTS.appcacheDownloading());
                break;
            case UPDATE_READY:
                statusItem.setText(I18N.CONSTANTS.appcacheDownloading());
                notifyNewVersion();
                break;
        }
    }

    private void notifyNewVersion() {
    	if(Window.confirm(I18N.CONSTANTS.newVersionPrompt())) {
    		Window.Location.reload();
    	}
	}

	private void onStatusClicked() {
      switch (status) {
            case UNSUPPORTED:
                Window.open("http://www.google.com/chrome", "_blank", null);
                break;
            case UNCACHED:
                cache();
                break;
            case OBSOLETE:
            case CHECKING:
            case DOWNLOADING:
            case IDLE:
                // noop
                break;
            case UPDATE_READY:
                restart();
                break;
        }
    }

    private void cache() {
        appCache.ensureCached(new AsyncCallback<Void>() {
            @Override
            public void onFailure(Throwable caught) {
                Window.alert(I18N.CONSTANTS.appcacheError() + caught.getMessage());
            }

            @Override
            public void onSuccess(Void result) {
                status = appCache.getStatus();
                onStatusChanged();
            }
        });
    }

    private void restart() {
        if(Window.confirm(I18N.CONSTANTS.newVersionPrompt())) {
            Window.Location.reload();
        }
    }

}

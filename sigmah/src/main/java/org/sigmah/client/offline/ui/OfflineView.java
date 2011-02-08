/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.client.offline.ui;

import com.extjs.gxt.ui.client.event.Observable;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.menu.Menu;
import com.extjs.gxt.ui.client.widget.menu.MenuItem;
import com.extjs.gxt.ui.client.widget.menu.SeparatorMenuItem;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.inject.Singleton;
import org.sigmah.client.i18n.I18N;
import org.sigmah.client.icon.IconImageBundle;

import java.util.Date;

/**
 * @author Alex Bertram
 */
@Singleton                                               
public class OfflineView extends Button implements OfflinePresenter.View {

    private StatusWindow window;
    private ProgressDialog progressDialog;

    private Menu menu;
    private MenuItem syncNowButton;
    private MenuItem toggleModeButton;
    private MenuItem reinstallOffline;

    public OfflineView() {
        window = new StatusWindow();

        syncNowButton = new MenuItem(I18N.CONSTANTS.syncNow(), IconImageBundle.ICONS.onlineSyncing());
        toggleModeButton = new MenuItem(I18N.CONSTANTS.switchToOnline());
        reinstallOffline = new MenuItem(I18N.CONSTANTS.reinstallOfflineMode());

        menu = new Menu();
        menu.add(syncNowButton);
        menu.add(toggleModeButton);
        menu.add(new SeparatorMenuItem());
        menu.add(reinstallOffline);
    }

    @Override
    public Observable getButton() {
        return this;
    }

    @Override
    public Observable getSyncNowItem() {
        return syncNowButton;
    }

    @Override
    public Observable getToggleItem() {
        return toggleModeButton;
    }

    @Override
    public Observable getReinstallItem() {
        return reinstallOffline;
    }

    @Override
    public void setButtonTextToInstall() {
        this.setIcon(null);
        this.setText(I18N.CONSTANTS.installOffline());
    }

    @Override
    public void setButtonTextToInstalling() {
        this.setIcon(null);
        this.setText(I18N.CONSTANTS.installingOffline());
    }

    @Override
    public void setButtonTextToLastSync(Date lastSyncTime) {
        this.setIcon(IconImageBundle.ICONS.offline());
        this.setText(I18N.MESSAGES.lastSynced(DateTimeFormat.getShortDateTimeFormat().format(lastSyncTime)));
    }

    @Override
    public void setButtonTextToSyncing() {
        this.setIcon(IconImageBundle.ICONS.onlineSyncing());
        this.setText(I18N.CONSTANTS.synchronizing());
    }

    @Override
    public void setButtonTextToOnline() {
        this.setIcon(IconImageBundle.ICONS.onlineSynced());
        this.setText(I18N.CONSTANTS.online());
    }

    @Override
    public void setButtonTextToLoading() {
        this.setText(I18N.CONSTANTS.loading());
    }

    @Override
    public void showProgressDialog() {
        if(progressDialog == null) {
            progressDialog = new ProgressDialog();
        }
        progressDialog.show();
    }

    @Override
    public void hideProgressDialog() {
        if(progressDialog != null) {
            progressDialog.hide();
        }
    }

    @Override
    public void updateProgress(String taskDescription, double percentComplete) {
        assert progressDialog != null;
        progressDialog.getProgressBar().updateProgress(percentComplete / 100, taskDescription);
    }

    public void enableMenu() {
        setMenu(menu);
    }

    public void disableMenu() {
        setMenu(null);
    }

	@Override
	public void showError(String message) {
		MessageBox.alert("Offline installation failed", "An error occured while installing " +
				"offline mode: " + message, null);
		
	}
}

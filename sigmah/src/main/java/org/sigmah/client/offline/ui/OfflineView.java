/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.client.offline.ui;

import java.util.Date;

import org.sigmah.client.i18n.I18N;
import org.sigmah.client.icon.IconImageBundle;
import org.sigmah.client.offline.ui.OfflinePresenter.EnableCallback;
import org.sigmah.client.offline.ui.OfflinePresenter.PromptConnectCallback;
import org.sigmah.client.util.state.CrossSessionStateProvider;

import com.extjs.gxt.ui.client.event.Observable;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.menu.Menu;
import com.extjs.gxt.ui.client.widget.menu.MenuItem;
import com.extjs.gxt.ui.client.widget.menu.SeparatorMenuItem;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.inject.Inject;
import com.google.inject.Singleton;

/**
 * @author Alex Bertram
 */
@Singleton                                               
public class OfflineView extends Button implements OfflinePresenter.View {

	private CrossSessionStateProvider stateProvider;
    private ProgressDialog progressDialog;
    private ConnectionDialog connectionDialog;

    private Menu menu;
    private MenuItem syncNowButton;
    private MenuItem toggleModeButton;
    private MenuItem reinstallOffline;

    @Inject
    public OfflineView(CrossSessionStateProvider stateProvider) {
    	this.stateProvider = stateProvider;
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
        toggleModeButton.setText(I18N.CONSTANTS.switchToOffline());
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
        toggleModeButton.setText(I18N.CONSTANTS.switchToOnline());
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
        toggleModeButton.setText(I18N.CONSTANTS.switchToOffline());
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
    
    

    @Override
	public void showConnectionProblem(int attempt, int retryDelay) {
    	assert progressDialog != null;
    	progressDialog.getProgressBar().updateText(I18N.CONSTANTS.connectionProblem());
	}

	public void enableMenu() {
        setMenu(menu);
    }

    public void disableMenu() {
        setMenu(null);
    }

	@Override
	public void showError(String message) {
		MessageBox.alert(I18N.CONSTANTS.offlineInstallationFailed(), I18N.CONSTANTS.offlineInstallError() + message, null);

	}

	@Override
	public void promptToGoOnline(final PromptConnectCallback callback) {
		if(connectionDialog == null) {
			connectionDialog = new ConnectionDialog(); 
		}
		connectionDialog.setCallback(callback);
		connectionDialog.clearStatus();
		connectionDialog.show();
	}

	@Override
	public void setConnectionDialogToFailure() {
		connectionDialog.setFailed();
	}

	@Override
	public void setConnectionDialogToBusy() {
		connectionDialog.setBusy();	
	}

	@Override
	public void hideConnectionDialog() {
		connectionDialog.hide();
		
	}

	@Override
	public void promptEnable(EnableCallback callback) {
		if(PromptOfflineDialog.shouldAskAgain(stateProvider)) {
			PromptOfflineDialog dialog = new PromptOfflineDialog(stateProvider,callback);
			dialog.show();
		}
	}

	@Override
	public void confirmEnable(EnableCallback callback) {
		// TODO Auto-generated method stub
		
	}

}

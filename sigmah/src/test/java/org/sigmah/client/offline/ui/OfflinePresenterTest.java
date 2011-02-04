/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.client.offline.ui;

import com.extjs.gxt.ui.client.event.BaseObservable;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.MenuEvent;
import com.extjs.gxt.ui.client.event.Observable;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Provider;
import org.junit.Before;
import org.junit.Test;
import org.sigmah.client.mock.MockEventBus;
import org.sigmah.client.mock.MockStateManager;
import org.sigmah.client.offline.OfflineGateway;
import org.sigmah.client.offline.sync.SyncStatusEvent;

import java.util.Date;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

public class OfflinePresenterTest {
    private ViewStub view;
    private MockEventBus eventBus;
    private Provider<OfflineGateway> gatewayProvider;
    private OfflineImplStub offlineImpl;
    private MockStateManager stateManager;

    @Before
    public void setUp() {
        view = new ViewStub();
        eventBus = new MockEventBus();
        offlineImpl = new OfflineImplStub();
        gatewayProvider = new Provider<OfflineGateway>() {
            @Override
            public OfflineGateway get() {
                return offlineImpl;
            }
        };
        stateManager = new MockStateManager();
    }


    @Test
    public void installationSequence() {

        // No state is set, so the presenter should assume that offline is not yet installed

        OfflinePresenter presenter = new OfflinePresenter(view, eventBus, gatewayProvider, stateManager);

        assertThat(view.defaultButtonText, equalTo("Install"));

        //  click the default button
        view.defaultButton.fireEvent(Events.Select);

        // verify that installation has been launched
        assertThat(offlineImpl.lastCall, equalTo("install"));

        // verify the UI is showing the status
        assertThat(view.defaultButtonText, equalTo("Installing..."));
        assertThat(view.progressDialogVisible, equalTo(true));
        assertThat(view.menuEnabled, equalTo(false));

        // status event is fired
        eventBus.fireEvent(new SyncStatusEvent("sync-task", 50));

        // verify that progress bar is updated
        assertThat(view.percentComplete, equalTo(50d));

        // signal that installation is complete
        offlineImpl.lastCallback.onSuccess(null);

        // verify that the UI has been updated
        assertThat(view.progressDialogVisible, equalTo(false));
        assertThat(view.defaultButtonText, equalTo("Last Sync'd: <date>"));
        assertThat(view.menuEnabled, equalTo(true));
    }

    @Test
    public void loadWhenOfflineIsEnabled() {

        // No state is set, so the presenter should assume that offline is not yet installed

        stateManager.set(OfflinePresenter.OFFLINE_MODE_KEY, OfflinePresenter.OfflineMode.OFFLINE.toString());

        OfflinePresenter presenter = new OfflinePresenter(view, eventBus, gatewayProvider, stateManager);

        // offline async fragment finishes loading
        assertThat(offlineImpl.lastCall, equalTo("goOffline"));
        offlineImpl.lastCallback.onSuccess(null);

        assertThat(view.defaultButtonText, equalTo("Last Sync'd: <date>"));
        assertThat(offlineImpl.lastCall, equalTo("goOffline"));
    }

    @Test
    public void reinstallWhileOffline() {

        stateManager.set(OfflinePresenter.OFFLINE_MODE_KEY, OfflinePresenter.OfflineMode.OFFLINE.toString());

        new OfflinePresenter(view, eventBus, gatewayProvider, stateManager);

        // offline async fragment finishes loading
        offlineImpl.lastCallback.onSuccess(null);

        // now click the reinstall menu item
        view.reinstallItem.fireEvent(Events.Select, new MenuEvent(null));

        // we should be in install mode
        assertThat(offlineImpl.lastCall, equalTo("install"));
        assertThat(view.defaultButtonText, equalTo("Installing..."));
    }


    private static class ViewStub implements OfflinePresenter.View {

        public BaseObservable defaultButton = new BaseObservable();
        public String defaultButtonText;
        public boolean progressDialogVisible = false;
        public String taskDescription;
        public double percentComplete;
        public boolean menuEnabled = false;

        private BaseObservable reinstallItem = new BaseObservable();
        private BaseObservable toggleItem = new BaseObservable();
        private BaseObservable syncNowItem = new BaseObservable();

        @Override
        public Observable getButton() {
            return defaultButton;
        }

        @Override
        public Observable getReinstallItem() {
            return reinstallItem;
        }

        @Override
        public Observable getToggleItem() {
            return toggleItem;
        }

        @Override
        public Observable getSyncNowItem() {
            return syncNowItem;
        }

        @Override
        public void setButtonTextToInstall() {
            this.defaultButtonText = "Install";
        }

        @Override
        public void setButtonTextToInstalling() {
            this.defaultButtonText = "Installing...";
        }

        @Override
        public void setButtonTextToLoading() {
            this.defaultButtonText = "Loading...";
        }

        @Override
        public void setButtonTextToSyncing() {
            this.defaultButtonText = "Syncing...";

        }

        @Override
        public void setButtonTextToLastSync(Date lastSyncTime) {
            this.defaultButtonText = "Last Sync'd: <date>";
        }

        @Override
        public void setButtonTextToOnline() {
            this.defaultButtonText = "Online";
        }

        @Override
        public void updateProgress(String taskDescription, double percentComplete) {
            this.taskDescription = taskDescription;
            this.percentComplete = percentComplete;
        }

        @Override
        public void showProgressDialog() {
            progressDialogVisible = true;
        }

        @Override
        public void hideProgressDialog() {
            progressDialogVisible = false;
        }

        @Override
        public void enableMenu() {
            menuEnabled = true;
        }

        @Override
        public void disableMenu() {
            menuEnabled = false;
        }

    }

    private static class OfflineImplStub implements OfflineGateway {
        AsyncCallback<Void> lastCallback;
        String lastCall = "not yet been called";

        @Override
        public void install(AsyncCallback<Void> callback) {
            onCalled("install", callback);
        }

        @Override
        public void goOffline(AsyncCallback<Void> callback) {
           onCalled("goOffline", callback);
        }

        @Override
        public void goOnline(AsyncCallback<Void> callback) {
            onCalled("goOnline", callback);
        }

        @Override
        public void synchronize(AsyncCallback<Void> callback) {
            onCalled("synchronize", callback);

        }

        @Override
        public Date getLastSyncTime() {
            return null;
        }

        private void onCalled(String method, AsyncCallback<Void> callback) {
            lastCall = method;
            lastCallback = callback;
        }

        @Override
        public boolean validateOfflineInstalled() {
            return true;
        }
    }
}

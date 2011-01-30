/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.client.offline.install;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.gears.client.localserver.ManagedResourceStore;
import com.google.gwt.gears.client.localserver.ManagedResourceStoreProgressHandler;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import org.sigmah.client.EventBus;
import org.sigmah.client.offline.sync.SyncStatusEvent;

public class CacheScript implements Step {
    private final EventBus eventBus;
    protected ManagedResourceStore store;
    private String status;

    public CacheScript(EventBus eventBus, ManagedResourceStore store) {
        this.eventBus = eventBus;
        this.store = store;
    }

    @Override
    public boolean isComplete() {
        Log.debug("CacheScript: " + store.getName() + " current version = " + store.getCurrentVersion());
        return false; // always force a version check
    }

    @Override
    public String getDescription() {
        return "AppCache" + store.getName();
    }

    @Override
    public void execute(final AsyncCallback<Void> callback) {
        store.setEnabled(true);
        store.checkForUpdate();
        new Timer() {
            @Override
            public void run() {
                switch(store.getUpdateStatus()) {
                    case ManagedResourceStore.UPDATE_CHECKING:
                        Log.trace("CacheScript: " + store.getName() + "; " + "checking...");
                        break;
                    case ManagedResourceStore.UPDATE_DOWNLOADING:
                        Log.trace("CacheScript: " + store.getName() + "; " + "downloading...");
                        break;
                    case ManagedResourceStore.UPDATE_FAILED:
                        Log.trace("CacheScript: " + store.getName() + "; " + "update failed.");
                        callback.onFailure(new Exception(store.getLastErrorMessage()));
                        this.cancel();
                        break;
                    case ManagedResourceStore.UPDATE_OK:
                        Log.trace("CacheScript: " + store.getName() + "; " + "update ok, new version = " + store.getCurrentVersion());
                        callback.onSuccess(null);
                        this.cancel();
                        break;
                }
            }
        }.scheduleRepeating(500);

        store.setOnProgressHandler(new ManagedResourceStoreProgressHandler() {
            @Override
            public void onProgress(ManagedResourceStoreProgressEvent event) {
                double percentComplete = event.getFilesComplete() / event.getFilesTotal() * 100;
                Log.trace("CacheScript: " + store.getName() + "; progressEvent = " + percentComplete);
                eventBus.fireEvent(SyncStatusEvent.TYPE, new SyncStatusEvent("AppCache", percentComplete));
            }
        });
    }
}

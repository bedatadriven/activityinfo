/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.client.offline.sync;

import com.google.gwt.gears.client.localserver.ManagedResourceStore;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class CacheScript implements Step {
    protected ManagedResourceStore store;
    private String status;

    public CacheScript(ManagedResourceStore store) {
        this.store = store;
    }

    @Override
    public boolean isComplete() {
        // managed resources stores cause no end of problems in hosted mode,
        // so only invoke here if we are actually running in scripted mode
        return store.getCurrentVersion() != null;
    }

    @Override
    public String getDescription() {
        return "AppCache" + store.getName();
    }

    @Override
    public void execute(final AsyncCallback<Void> callback) {
        store.checkForUpdate();
        new Timer() {
            @Override
            public void run() {
                switch(store.getUpdateStatus()) {
                    case ManagedResourceStore.UPDATE_CHECKING:
                        status = "Checking";
                    case ManagedResourceStore.UPDATE_DOWNLOADING:
                        status = "Downloading";
                        break;
                    case ManagedResourceStore.UPDATE_FAILED:
                        callback.onFailure(new Exception(store.getLastErrorMessage()));
                        break;
                    case ManagedResourceStore.UPDATE_OK:
                        callback.onSuccess(null);
                        break;
                }
            }
        }.scheduleRepeating(100);
    }
}

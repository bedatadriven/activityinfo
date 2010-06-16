/*
 * This file is part of ActivityInfo.
 *
 * ActivityInfo is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * ActivityInfo is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with ActivityInfo.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Copyright 2010 Alex Bertram and contributors.
 */

package org.activityinfo.client.offline.sync;

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

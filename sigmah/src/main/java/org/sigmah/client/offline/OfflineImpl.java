/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.client.offline;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import com.google.inject.Provider;
import org.sigmah.client.dispatch.SwitchingDispatcher;
import org.sigmah.client.offline.command.LocalDispatcher;
import org.sigmah.client.offline.install.InstallSteps;
import org.sigmah.client.offline.sync.Synchronizer;

import java.util.Date;

public class OfflineImpl implements OfflineGateway {
    private static final int SYNC_INTERVAL = 30000;

    private final SwitchingDispatcher switchingDispatcher;
    private final Provider<InstallSteps> installSteps;
    private final Provider<LocalDispatcher> localDispatcher;
    private final Synchronizer synchronizer;


    @Inject
    public OfflineImpl(SwitchingDispatcher switchingDispatcher,
                       Provider<InstallSteps> installSteps,
                       Provider<LocalDispatcher> localDispatcher,
                       Synchronizer synchronizer) {

        this.switchingDispatcher = switchingDispatcher;
        this.installSteps = installSteps;
        this.localDispatcher = localDispatcher;
        this.synchronizer = synchronizer;
    }

    @Override
    public void install(final AsyncCallback<Void> callback) {

        // just in case we are re-installing 
        switchingDispatcher.clearLocalDispatcher();

        installSteps.get().run(new AsyncCallback() {
            @Override
            public void onFailure(Throwable throwable) {
                callback.onFailure(throwable);
            }

            @Override
            public void onSuccess(Object o) {
                goOffline(callback);
            }
        });
    }

    @Override
    public void goOffline(AsyncCallback<Void> callback) {
        switchingDispatcher.setLocalDispatcher(localDispatcher.get());
        callback.onSuccess(null);
    }

    @Override
    public Date getLastSyncTime() {
        return synchronizer.getLastUpdateTime();
    }

    @Override
    public boolean validateOfflineInstalled() {
        return synchronizer.getLastUpdateTime() != null;
    }

    @Override
    public void synchronize(AsyncCallback<Void> callback) {
        synchronizer.start(callback); 

    }

    @Override
    public void goOnline(AsyncCallback callback) {
        // TODO: transmit queued changes
        switchingDispatcher.clearLocalDispatcher();

        callback.onSuccess(null);
    }
}

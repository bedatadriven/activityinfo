/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.client.offline;

import java.util.Date;

import org.sigmah.client.dispatch.AsyncMonitor;
import org.sigmah.client.dispatch.Dispatcher;
import org.sigmah.client.dispatch.remote.DirectDispatcher;
import org.sigmah.client.offline.command.LocalDispatcher;
import org.sigmah.client.offline.install.InstallSteps;
import org.sigmah.client.offline.sync.Synchronizer;
import org.sigmah.shared.command.Command;
import org.sigmah.shared.command.Ping;
import org.sigmah.shared.command.result.VoidResult;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import com.google.inject.Provider;

public class OfflineImpl implements OfflineGateway {
    private static final int SYNC_INTERVAL = 30000;

    private final Provider<InstallSteps> installSteps;
    private final LocalDispatcher localDispatcher;
    private final Dispatcher remoteDispatcher;
    private final Synchronizer synchronizer;
   


    @Inject
    public OfflineImpl(Provider<InstallSteps> installSteps,
                       LocalDispatcher localDispatcher,
                       DirectDispatcher remoteDispatcher,
                       Synchronizer synchronizer) {

        this.installSteps = installSteps;
        this.localDispatcher = localDispatcher;
        this.remoteDispatcher = remoteDispatcher;
        this.synchronizer = synchronizer;
    }

    @Override
    public void install(final AsyncCallback<Void> callback) {

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
    public void goOnline(final AsyncCallback<Void> callback) {
        // TODO: transmit queued changes
    	
    	// confirm that we can connect to the server
    	remoteDispatcher.execute(new Ping(), null, new AsyncCallback<VoidResult>() {

			@Override
			public void onFailure(Throwable caught) {
				callback.onFailure(caught);
				
			}

			@Override
			public void onSuccess(VoidResult result) {
				callback.onSuccess(null);
			}
		});
    }

	@Override
	public boolean canHandle(Command command) {
		return localDispatcher.canExecute(command);
	}

	@Override
	public void execute(Command command, AsyncMonitor monitor,
			AsyncCallback callback) {
		
		localDispatcher.execute(command, monitor, callback);
		
	}
}

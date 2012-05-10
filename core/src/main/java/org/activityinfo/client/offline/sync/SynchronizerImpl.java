/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.activityinfo.client.offline.sync;

import java.util.Date;

import org.activityinfo.client.dispatch.AsyncMonitor;
import org.activityinfo.client.dispatch.Dispatcher;
import org.activityinfo.client.dispatch.remote.DirectDispatcher;
import org.activityinfo.client.offline.AuthTokenUtil;
import org.activityinfo.client.offline.command.LocalDispatcher;
import org.activityinfo.shared.auth.AuthenticatedUser;
import org.activityinfo.shared.command.Command;
import org.activityinfo.shared.command.Ping;
import org.activityinfo.shared.command.result.VoidResult;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;

public class SynchronizerImpl implements Synchronizer {

    private final LocalDispatcher localDispatcher;
    private final Dispatcher remoteDispatcher;
    private final AppCacheSynchronizer appCacheSynchronizer;
    private final DownSynchronizer downSychronizer;
    private final UpdateSynchronizer updateSynchronizer;
    private final AuthenticatedUser auth;
    private final SchemaMigration migrator;


    @Inject
    public SynchronizerImpl(
                       LocalDispatcher localDispatcher,
                       DirectDispatcher remoteDispatcher,
                       AppCacheSynchronizer appCache,
                       DownSynchronizer synchronizer,
                       UpdateSynchronizer updateSynchronizer,
                       AuthenticatedUser auth,
                       SchemaMigration migrator) {
    	this.appCacheSynchronizer = appCache;
    	this.localDispatcher = localDispatcher;
        this.remoteDispatcher = remoteDispatcher;
        this.downSychronizer = synchronizer;
        this.updateSynchronizer = updateSynchronizer;
        this.auth = auth;
        this.migrator = migrator;
    }

    @Override
    public void install(final AsyncCallback<Void> callback) {
    	Log.trace("SynchronizerImpl.install() starting...");
    	appCacheSynchronizer.ensureUpToDate(new AsyncCallback<Void>() {
			
			@Override
			public void onSuccess(Void result) {
		    	Log.trace("Calling downSynchronizer.startFresh()");
				downSychronizer.startFresh(new AsyncCallback<Void>() {
					
					@Override
					public void onSuccess(Void result) {
				    	Log.trace("downSynchronizer.startFresh() completed");
						AuthTokenUtil.ensurePersistentCookie(auth);
						callback.onSuccess(result);
					}
					
					@Override
					public void onFailure(Throwable caught) {
						callback.onFailure(caught);
					}
				});
			}
			
			@Override
			public void onFailure(Throwable caught) {
				callback.onFailure(caught);
			}
		});
    }

    @Override
    public void getLastSyncTime(AsyncCallback<java.util.Date> callback) {
        downSychronizer.getLastUpdateTime(callback);
    }

    @Override
    public void validateOfflineInstalled(final AsyncCallback<Void> callback) {
    	downSychronizer.getLastUpdateTime(new AsyncCallback<Date>() {
			
			@Override
			public void onSuccess(Date result) {
				if(result == null) {
					callback.onFailure(new RuntimeException("Never synchronized"));
				} else {
					// apply any changes made to the schema
					migrator.migrate(callback);
				}
				
			}
			
			@Override
			public void onFailure(Throwable caught) {
				callback.onFailure(caught);
			}
		});
    }

    @Override
    public void synchronize(final AsyncCallback<Void> callback) {
    	appCacheSynchronizer.ensureUpToDate(new AsyncCallback<Void>() {
			
			@Override
			public void onSuccess(Void result) {
				// send updates to server, then on success request 
		    	// updates from server
		    	updateSynchronizer.sync(new AsyncCallback<Void>() {
					
					@Override
					public void onSuccess(Void arg0) {
				    	downSychronizer.start(callback); 
						
					}
					
					@Override
					public void onFailure(Throwable caught) {
						callback.onFailure(caught);
					}
				});
			}
			
			@Override
			public void onFailure(Throwable caught) {
				callback.onFailure(caught);
			}
		});
    	
    }

    @Override
    public void goOnline(final AsyncCallback<Void> callback) {
    
    	updateSynchronizer.sync(new AsyncCallback<Void>() {

			@Override
			public void onFailure(Throwable caught) {
				callback.onFailure(caught);
			}

			@Override
			public void onSuccess(Void result) {
		    	verifyConnectivity(callback);
			}
		});    	
    }

	private void verifyConnectivity(final AsyncCallback<Void> callback) {
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

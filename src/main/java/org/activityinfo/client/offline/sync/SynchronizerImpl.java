/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.activityinfo.client.offline.sync;

import java.util.Date;

import org.activityinfo.client.dispatch.AsyncMonitor;
import org.activityinfo.client.offline.command.LocalDispatcher;
import org.activityinfo.client.offline.sync.pipeline.InstallPipeline;
import org.activityinfo.client.offline.sync.pipeline.SyncPipeline;
import org.activityinfo.shared.command.Command;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;

public class SynchronizerImpl implements Synchronizer {

    private final LocalDispatcher localDispatcher;
    private final SchemaMigration migrator;
    private final InstallPipeline installPipeline;
    private final SyncPipeline syncPipeline;
	private SyncHistoryTable historyTable;

    @Inject
    public SynchronizerImpl(
                       LocalDispatcher localDispatcher,
                       InstallPipeline installPipeline,
                       SyncPipeline syncPipeline,
                       SyncHistoryTable historyTable,
                       SchemaMigration migrator) {
    	this.localDispatcher = localDispatcher;
    	this.migrator = migrator;
    	this.installPipeline = installPipeline;
    	this.syncPipeline = syncPipeline;
    	this.historyTable = historyTable;
    	
    }

    @Override
    public void install(final AsyncCallback<Void> callback) {
    	Log.trace("SynchronizerImpl.install() starting...");
    	installPipeline.start(callback);
    }

    @Override
    public void getLastSyncTime(AsyncCallback<java.util.Date> callback) {
        historyTable.get(callback);
    }

    @Override
    public void validateOfflineInstalled(final AsyncCallback<Void> callback) {
    	historyTable.get(new AsyncCallback<Date>() {
			
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

    public void synchronize() {
       	syncPipeline.start();
    }

	@Override
	public void execute(Command command, AsyncCallback callback) {
		localDispatcher.execute(command, callback);
	}
}

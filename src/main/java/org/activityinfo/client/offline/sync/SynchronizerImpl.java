/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.activityinfo.client.offline.sync;

import java.util.Date;

import org.activityinfo.client.EventBus;
import org.activityinfo.client.offline.command.LocalDispatcher;
import org.activityinfo.client.offline.sync.pipeline.InstallPipeline;
import org.activityinfo.client.offline.sync.pipeline.SyncPipeline;
import org.activityinfo.shared.command.Command;

import org.activityinfo.client.Log;
import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.RepeatingCommand;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;

public class SynchronizerImpl implements Synchronizer {

    private static final int AUTO_SYNC_INTERVAL_MS = 5 * 60 * 1000;
    
	private final LocalDispatcher localDispatcher;
    private final SchemaMigration migrator;
    private final InstallPipeline installPipeline;
    private final SyncPipeline syncPipeline;
	private SyncHistoryTable historyTable;

    @Inject
    public SynchronizerImpl(
    				   EventBus eventBus,
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
    	
    	eventBus.addListener(SyncRequestEvent.TYPE, new Listener<BaseEvent>() {

			@Override
			public void handleEvent(BaseEvent be) {
				SynchronizerImpl.this.syncPipeline.start();
			}
		});
    	
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
       	syncPipeline.start(new AsyncCallback<Void>() {
			
			@Override
			public void onSuccess(Void result) {
				scheduleNext();	
			}
			
			@Override
			public void onFailure(Throwable caught) {
				scheduleNext();
			}
		});
    }

	private void scheduleNext() {
		Scheduler.get().scheduleFixedDelay(new RepeatingCommand() {
			@Override
			public boolean execute() {
				synchronize();
				return false;
			}
		}, AUTO_SYNC_INTERVAL_MS);
	}

	@Override
	public void execute(Command command, AsyncCallback callback) {
		localDispatcher.execute(command, callback);
	}
}

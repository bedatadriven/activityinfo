/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.client.offline.sync;

import java.sql.Date;
import java.util.Collection;
import java.util.Iterator;

import org.sigmah.client.EventBus;
import org.sigmah.client.dispatch.Dispatcher;
import org.sigmah.client.dispatch.remote.Authentication;
import org.sigmah.client.dispatch.remote.Direct;
import org.sigmah.client.i18n.UIConstants;
import org.sigmah.client.i18n.UIMessages;
import org.sigmah.shared.command.GetSyncRegionUpdates;
import org.sigmah.shared.command.GetSyncRegions;
import org.sigmah.shared.command.result.SyncRegion;
import org.sigmah.shared.command.result.SyncRegionUpdate;
import org.sigmah.shared.command.result.SyncRegions;

import com.allen_sauer.gwt.log.client.Log;
import com.bedatadriven.rebar.sql.client.SqlDatabase;
import com.bedatadriven.rebar.sql.client.SqlException;
import com.bedatadriven.rebar.sql.client.SqlTransaction;
import com.bedatadriven.rebar.sql.client.SqlTransactionCallback;
import com.bedatadriven.rebar.sync.client.BulkUpdaterAsync;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import com.google.inject.Singleton;

/**
 * 
 * Synchronizes the local database by retriving updates from
 * the remote server. 
 * 
 * 
 * @author alex
 *
 */
@Singleton
public class Synchronizer {

    private final Dispatcher dispatch;
    private final EventBus eventBus;
    private final SqlDatabase conn;
    private final UIConstants uiConstants;
    private final UIMessages uiMessages;

    private ProgressTrackingIterator<SyncRegion> regionIt;

    private int rowsUpdated;

    private AsyncCallback<Void> callback;

    private boolean running = false;
    
    private SyncRegionTable localVerisonTable;
    private SyncHistoryTable historyTable;
    
    private SynchronizerStats stats = new SynchronizerStats();

    @Inject
    public Synchronizer(EventBus eventBus,
                        @Direct Dispatcher dispatch,
                        SqlDatabase conn,
                        UIConstants uiConstants, UIMessages uiMessages) {
        this.eventBus = eventBus;
        this.conn = conn;
        this.dispatch = dispatch;
        this.uiConstants = uiConstants;
        this.uiMessages = uiMessages;
        
        this.localVerisonTable = new SyncRegionTable(conn);
        this.historyTable = new SyncHistoryTable(conn);
   }

    /**
     * Drops all tables in the user's database and starts synchronization fresh
     */
    public void startFresh(final AsyncCallback<Void> callback) { 	
    	conn.dropAllTables(new AsyncCallback<Void>() {
			
			@Override
			public void onSuccess(Void result) {
				start(callback);
			}
			
			@Override
			public void onFailure(Throwable caught) {
				callback.onFailure(caught);
			}
		});
    }

    public void start(AsyncCallback<Void> callback) {
    	this.callback = callback;
        fireStatusEvent(uiConstants.requestingSyncRegions(), 0);
        running = true;
        rowsUpdated = 0;
        stats.onStart();
        createSyncMetaTables();
    }


    
    private void createSyncMetaTables() {
    	conn.transaction(new DefaultTxCallback() {
			
			@Override
			public void begin(SqlTransaction tx) {
				tx.executeSql("create table if not exists sync_regions (id TEXT PRIMARY KEY, localVersion TEXT)");
				tx.executeSql("create table if not exists sync_history (lastUpdate INTEGER)");
			}

			@Override
			public void onSuccess() {
				retrieveSyncRegions();
			}
		});
    }
    
	private void retrieveSyncRegions() {
		dispatch.execute(new GetSyncRegions(), null, new AsyncCallback<SyncRegions>() {
            @Override
            public void onFailure(Throwable throwable) {
                handleException("Error getting sync regions", throwable);
            }

            @Override
            public void onSuccess(SyncRegions syncRegions) {
                Synchronizer.this.regionIt = new ProgressTrackingIterator(syncRegions.getList());
                fireStatusEvent("Received sync regions...", 0);
                doNextUpdate();
            }
        });
	}

    private void fireStatusEvent(String task, double percentComplete) {
        Log.info("Synchronizer: " + task + " (" + percentComplete + "%)");
        eventBus.fireEvent(SyncStatusEvent.TYPE, new SyncStatusEvent(task, percentComplete));
    }

    private void doNextUpdate() {
        if(!running) {
            return;
        }
        if(regionIt.hasNext()) {
            final SyncRegion region = regionIt.next();
            checkLocalVersion(region);
        } else {
            onSynchronizationComplete();
        }
    }

    private void checkLocalVersion(final SyncRegion region) {
    	localVerisonTable.get(region.getId(), new DefaultCallback<String>() {

			@Override
			public void onSuccess(String localVersion) {
				doUpdate(region, localVersion);
			}
		});
    }

    
    private void onSynchronizationComplete() {
    	stats.onFinished();
        setLastUpdateTime();
        fireStatusEvent(uiConstants.synchronizationComplete(), 100);
        if(callback != null) {
            callback.onSuccess(null);
        }
    }

    private void doUpdate(final SyncRegion region, String localVersion) {
    	    	
        fireStatusEvent(uiMessages.synchronizerProgress(region.getId(), Integer.toString(rowsUpdated)),
                regionIt.percentComplete());
        Log.info("Synchronizer: Region " + region.getId() + ": localVersion=" + localVersion);

    	stats.onRemoteCallStarted();
        dispatch.execute(new GetSyncRegionUpdates(region.getId(), localVersion), null, new AsyncCallback<SyncRegionUpdate>() {
            @Override
            public void onFailure(Throwable throwable) {
                handleException("GetSyncRegionUpdates for region id " + region.getId() + " failed.", throwable);
            }

            @Override
            public void onSuccess(SyncRegionUpdate update) {            	
            	stats.onRemoteCallFinished();
                persistUpdates(region, update);
            }
        });
    }

    private void persistUpdates(final SyncRegion region, final SyncRegionUpdate update) {
        if(update.getSql() == null) {
            Log.debug("Synchronizer: Region " + region.getId() + " is up to date");
            doNextUpdate();
    
        } else {
		    Log.debug("Synchronizer: persisting updates for region " + region.getId());
		    stats.onDbUpdateStarted();
	        conn.executeUpdates(update.getSql(), new AsyncCallback<Integer>() {
	            @Override
	            public void onFailure(Throwable throwable) {
	                handleException("Synchronizer: Async execution of region updates failed. \nSQL=" + update.getSql() +
	                        "\nMessage: " + throwable.getMessage(), throwable);
	            }
	
	            @Override
	            public void onSuccess(Integer rows) {
	                Log.debug("Synchronizer: updates to region " + region.getId() + " succeeded, " + rows + " row(s) affected");
	                rowsUpdated += rows;
	                stats.onDbUpdateFinished();
	                updateLocalVersion(region, update);
	            }
	        });
        }
    }

    private void updateLocalVersion(final SyncRegion region, final SyncRegionUpdate update) {
    	localVerisonTable.put(region.getId(), update.getVersion(), new AsyncCallback<Void>() {
			
			@Override
			public void onSuccess(Void result) {
				if(!update.isComplete()) {
	                doUpdate(region, update.getVersion());
	            } else {
	                doNextUpdate();
	            }
			}
			
			@Override
			public void onFailure(Throwable caught) {
				callback.onFailure(caught);
			}
		});
    }

    private void handleException(String message, Throwable throwable) {
        Log.error("Synchronizer: " + message, throwable);
        if(callback != null) {
            callback.onFailure(throwable);
        }
    }

    private void setLastUpdateTime() {
        historyTable.update();
    }

    public void getLastUpdateTime(final AsyncCallback<java.util.Date> callback) {
        historyTable.get(callback);
    }
    
    private abstract class DefaultTxCallback extends SqlTransactionCallback {
		@Override
		public final void onError(SqlException e) {
			callback.onFailure(e);
		}
    }
  
    private abstract class DefaultCallback<T> implements AsyncCallback<T> {
		@Override
		public void onFailure(Throwable caught) {
			callback.onFailure(caught);
		}
    }

    private static class ProgressTrackingIterator<T> implements Iterator<T> {
        private double total;
        private double completed;
        private Iterator<T> inner;

        private ProgressTrackingIterator(Collection<T> collection) {
            total = collection.size();
            completed = 0;
            inner = collection.iterator();
        }

        @Override
        public boolean hasNext() {
            completed++;
            return inner.hasNext();
        }

        @Override
        public T next() {
            return inner.next();
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }

        public double percentComplete() {
            return completed / total * 100d;
        }
    }


}

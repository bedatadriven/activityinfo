package org.activityinfo.client.offline.command;

import org.activityinfo.client.EventBus;
import org.activityinfo.client.offline.sync.ServerStateChangeEvent;
import org.activityinfo.client.offline.sync.SyncCompleteEvent;
import org.activityinfo.client.offline.sync.SyncRequestEvent;
import org.activityinfo.client.offline.sync.SyncStatusEvent;

import com.extjs.gxt.ui.client.event.Listener;
import com.google.inject.ImplementedBy;
import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class OutOfSyncMonitor {

	private final EventBus eventBus;
	private boolean syncing = false;
	
	private int time = 0;
	private int timeLastSyncBegan = -1;
	private int timeLastServerMutation = -2;
	
	private boolean outOfSync = false;
	
	@ImplementedBy(OutOfSyncStatus.class)
	public interface View {
		void hide();
		void show();
	}
	
	private View view;
	
	@Inject
	public OutOfSyncMonitor(EventBus eventBus, 
			View view) {
		super();
		this.eventBus = eventBus;
		this.view = view;
		
		this.eventBus.addListener(SyncCompleteEvent.TYPE, new Listener<SyncCompleteEvent>() {

			@Override
			public void handleEvent(SyncCompleteEvent be) {
				onSyncComplete();
			}
		});
		this.eventBus.addListener(SyncStatusEvent.TYPE, new Listener<SyncStatusEvent>() {

			@Override
			public void handleEvent(SyncStatusEvent be) {
				if(!syncing) {
					onSyncStarted();
				}
			}
		});
		this.eventBus.addListener(ServerStateChangeEvent.TYPE, new Listener<ServerStateChangeEvent>() {

			@Override
			public void handleEvent(ServerStateChangeEvent be) {
				onServerStateMutated();
			}
		});
	}

	public void onServerStateMutated() {
		outOfSync = true;
		timeLastServerMutation = time++;
		
		if(!syncing) {
			eventBus.fireEvent(SyncRequestEvent.INSTANCE);
		}
		view.show();
	}
	
	private void onSyncStarted() {
		syncing = true;
		timeLastSyncBegan = time++;
	}

	private void onSyncComplete() {
		if(timeLastSyncBegan > timeLastServerMutation) {
			// sync started after mutation,  so we 
			// should have the latest results
			outOfSync = false;
			view.hide();
		} else {
			eventBus.fireEvent(SyncRequestEvent.INSTANCE);
		}
		syncing = false;
	}
	
	public boolean isOutOfSync() {
		return outOfSync;
	}
}

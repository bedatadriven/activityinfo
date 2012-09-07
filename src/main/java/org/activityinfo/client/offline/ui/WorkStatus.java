package org.activityinfo.client.offline.ui;

import org.activityinfo.client.EventBus;
import org.activityinfo.client.offline.OfflineStateChangeEvent;
import org.activityinfo.client.offline.OfflineStateChangeEvent.State;
import org.activityinfo.client.offline.sync.SyncCompleteEvent;
import org.activityinfo.client.offline.sync.SyncStatusEvent;

import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.widget.Status;
import com.google.inject.Inject;

public class WorkStatus extends Status {

	private boolean syncing;
	private State state = State.UNINSTALLED;

	@Inject
	public WorkStatus(EventBus eventBus) {

		eventBus.addListener(SyncStatusEvent.TYPE, new Listener<SyncStatusEvent>() {

			@Override
			public void handleEvent(SyncStatusEvent be) {
				setBusy(be.getTask() + " " + ((int)(be.getPercentComplete())) + "%");
				syncing = true;
			}
		});	
		eventBus.addListener(SyncCompleteEvent.TYPE, new Listener<SyncCompleteEvent>() {

			@Override
			public void handleEvent(SyncCompleteEvent event) {
				clearBusy();
			}
		});
		
		eventBus.addListener(OfflineStateChangeEvent.TYPE, new Listener<OfflineStateChangeEvent>() {

			@Override
			public void handleEvent(OfflineStateChangeEvent be) {
				onOfflineStatusChange(be.getState());
			}
		});
	}

	private void onOfflineStatusChange(State state) {
		this.state = state;
		if(!syncing) {
			clearBusy();
		}
	}

	private void clearBusy() {
		switch(state) {
		case UNINSTALLED:
		case INSTALLING:
			this.clearStatus("Working online");
			break;
		case CHECKING:
			this.clearStatus("Loading...");
			break;
		case INSTALLED:
			this.clearStatus("Working offline");
			break;
		}
	}
	
}

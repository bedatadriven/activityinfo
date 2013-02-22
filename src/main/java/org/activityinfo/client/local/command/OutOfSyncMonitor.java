package org.activityinfo.client.local.command;

/*
 * #%L
 * ActivityInfo Server
 * %%
 * Copyright (C) 2009 - 2013 UNICEF
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the 
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public 
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */

import org.activityinfo.client.EventBus;
import org.activityinfo.client.local.sync.ServerStateChangeEvent;
import org.activityinfo.client.local.sync.SyncCompleteEvent;
import org.activityinfo.client.local.sync.SyncRequestEvent;
import org.activityinfo.client.local.sync.SyncStatusEvent;

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

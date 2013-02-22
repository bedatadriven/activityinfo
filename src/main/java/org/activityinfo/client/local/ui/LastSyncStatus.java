package org.activityinfo.client.local.ui;

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

import java.util.Date;

import org.activityinfo.client.EventBus;
import org.activityinfo.client.i18n.I18N;
import org.activityinfo.client.local.sync.SyncCompleteEvent;
import org.activityinfo.client.local.sync.SyncRequestEvent;

import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.MenuEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.Status;
import com.extjs.gxt.ui.client.widget.menu.Menu;
import com.extjs.gxt.ui.client.widget.menu.MenuItem;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.RepeatingCommand;
import com.google.gwt.user.client.Event;
import com.google.inject.Inject;

public class LastSyncStatus extends Status {

	private static final int UPDATE_DELAY = 30000;

	private static final int WARNING_THRESHOLD_SECONDS = 30 * 60;

	private final EventBus eventBus;
	
	private Date lastSyncTime;
	
	private Menu contextMenu;

	
	@Inject
	public LastSyncStatus(EventBus eventBus) {
		this.eventBus = eventBus;
		setStyleAttribute("cursor", "pointer");
		eventBus.addListener(SyncCompleteEvent.TYPE, new Listener<SyncCompleteEvent>() {
			@Override
			public void handleEvent(SyncCompleteEvent event) {
				lastSyncTime = event.getTime();
				setBox(true);
				updateLastSyncLabel();
			}
		});
		
		Scheduler.get().scheduleFixedPeriod(new RepeatingCommand() {
			
			@Override
			public boolean execute() {
				updateLastSyncLabel();
				return true;
			}
		}, UPDATE_DELAY);
	
		sinkEvents(Event.ONCLICK);
		
	}
	
	public void createMenu() {
		
		MenuItem syncNow = new MenuItem(I18N.CONSTANTS.syncNow(),new SelectionListener<MenuEvent>() {

			@Override
			public void componentSelected(MenuEvent ce) {
				eventBus.fireEvent(SyncRequestEvent.INSTANCE);
			}
		});
		
		this.contextMenu = new Menu();
		contextMenu.add(syncNow);
	}
	
	
	@Override
	public void onBrowserEvent(Event event) {
		super.onBrowserEvent(event);
		if(event.getTypeInt() == Event.ONCLICK) {
			if(contextMenu == null) {
				createMenu();
			}
			contextMenu.show(getElement(), "bl-tl");
		}
	}

	private void updateLastSyncLabel() {
		if(lastSyncTime != null) {
			setText(I18N.MESSAGES.lastSynced(formatLastSyncTime()));
			if(secondsSinceSync() > WARNING_THRESHOLD_SECONDS) {
				setIconStyle(SyncStatusResources.INSTANCE.style().warningIcon());
			} else {
				setIconStyle(null);
			}
		} 
	}
	
	private String formatLastSyncTime() {
		long secondsAgo = secondsSinceSync();
		if(secondsAgo <= 60) {
			return I18N.CONSTANTS.relativeTimeMinAgo();
		}
		long minutesAgo = secondsAgo / 60;
		if(minutesAgo <= 60) {
			return I18N.MESSAGES.minutesAgo((int)minutesAgo);
		}
		long hoursAgo = minutesAgo / 60;
		if(hoursAgo <= 48) {
			return I18N.MESSAGES.hoursAgo((int)hoursAgo);
		}
		long daysAgo = hoursAgo / 24;
		return I18N.MESSAGES.daysAgo((int)daysAgo);
	}


	private long secondsSinceSync() {
		long now = new Date().getTime();
		long last = lastSyncTime.getTime();
		
		long secondsAgo = (now-last)/1000;
		return secondsAgo;
	}
	
}

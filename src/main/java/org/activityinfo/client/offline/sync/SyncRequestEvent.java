package org.activityinfo.client.offline.sync;

import org.activityinfo.client.EventBus;

import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.EventType;

public class SyncRequestEvent extends BaseEvent {

	public static final EventType TYPE = new EventBus.NamedEventType("SyncRequest");
	
	public static final SyncRequestEvent INSTANCE = new SyncRequestEvent();
	
	private SyncRequestEvent() {
		super(TYPE);
	}

	
}

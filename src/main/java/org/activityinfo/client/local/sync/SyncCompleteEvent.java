package org.activityinfo.client.local.sync;

import java.util.Date;

import org.activityinfo.client.EventBus;

import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.EventType;

public class SyncCompleteEvent extends BaseEvent {
	
    public static final EventType TYPE = new EventBus.NamedEventType("SyncCompleteEvent");

    private Date time;

	public SyncCompleteEvent(Date time) {
		super(TYPE);
		this.time = time;
	}

	public Date getTime() {
		return time;
	}

	@Override
	public String toString() {
		return "SyncCompleteEvent [time=" + time + "]";
	}
	
	
}

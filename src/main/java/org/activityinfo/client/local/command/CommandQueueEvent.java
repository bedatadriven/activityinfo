package org.activityinfo.client.local.command;

import org.activityinfo.client.EventBus;

import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.EventType;

public class CommandQueueEvent extends BaseEvent {

	public static final EventType TYPE = new EventBus.NamedEventType("CommandQueueEvent");

	private int enqueuedItemCount;
	
	public CommandQueueEvent(int enqueuedItemCount) {
		super(TYPE);
		this.enqueuedItemCount = enqueuedItemCount;
	}

	public int getEnqueuedItemCount() {
		return enqueuedItemCount;
	}
}

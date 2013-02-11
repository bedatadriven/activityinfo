package org.activityinfo.client.local.sync;

import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.EventType;

/**
 * Fired when we change the server-side state via a remote command
 */
public class ServerStateChangeEvent extends BaseEvent {

	public static final EventType TYPE = new EventType();

	public ServerStateChangeEvent() {
		super(TYPE);
	}
}

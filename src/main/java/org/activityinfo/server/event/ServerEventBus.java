package org.activityinfo.server.event;

import com.google.common.eventbus.EventBus;

/**
 * Empty extension of the standard Guava EventBus, to make an explicit distinction with the client-side EventBus 
 */
public class ServerEventBus extends EventBus {
	public ServerEventBus(){
		super("ServerEventBus");
	}
}

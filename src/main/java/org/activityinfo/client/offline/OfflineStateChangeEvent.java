package org.activityinfo.client.offline;

import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.EventType;

public class OfflineStateChangeEvent extends BaseEvent {

	public static final EventType TYPE = new EventType();
	
	public enum State { CHECKING, UNINSTALLED, INSTALLED, INSTALLING };
	
	private State state;
	
	public OfflineStateChangeEvent(State state) {
		super(TYPE);
		this.state = state;
	}

	public State getState() {
		return state;
	}
	
	public String toString() {
		return "OfflineStateChangeEvent: " + state.name();
	}
}

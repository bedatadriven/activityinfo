package org.activityinfo.client.offline;

import org.activityinfo.client.EventBus.NamedEventType;

import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.EventType;

public class OfflineStateChangeEvent extends BaseEvent {

	public static final EventType TYPE = new NamedEventType("OfflineStateChange");
	
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

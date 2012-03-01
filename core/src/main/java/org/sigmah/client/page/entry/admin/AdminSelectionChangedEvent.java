package org.sigmah.client.page.entry.admin;

import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.EventType;

/**
 * Fired when the combos are fully updated, after the bounds
 * have been update (and that event is fired).
 * 
 * Fires only once per user click, even if it affects
 * multiple levels
 */
public class AdminSelectionChangedEvent extends BaseEvent {
	
	public static final EventType TYPE = new EventType();
	
	
	public AdminSelectionChangedEvent() {
		super(TYPE);
	}
}

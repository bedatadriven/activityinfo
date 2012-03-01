/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.client;

import com.allen_sauer.gwt.log.client.Log;
import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.BaseObservable;
import com.extjs.gxt.ui.client.event.EventType;
import com.extjs.gxt.ui.client.event.Listener;
import com.google.inject.Singleton;

/**
 * An EventBus implementation that logs all published events.
 * 
 * @author Alex Bertram
 */
@Singleton
public class LoggingEventBus extends BaseObservable implements EventBus {

    public LoggingEventBus() {
        super();
        Log.trace("LoggingEventBus: initialized");
    }

    public boolean fireEvent(BaseEvent event) {
		return fireEvent(event.getType(), event);
	}
	
    @Override
    public boolean fireEvent(EventType eventType, BaseEvent be) {
        Log.debug("EventBus: " + eventType.toString() + ": " + be.toString());
        return super.fireEvent(eventType, be);
    }

    @Override
    protected void callListener(Listener<BaseEvent> listener, BaseEvent be) {
        try {
            super.callListener(listener, be);
        } catch (Exception e) {
            // don't allow one misbehaving component to throw
            // everything off
            Log.error("EventBus: " + listener.getClass().toString() + " threw an exception while receiving the event " +
                    be.toString(), e);
        }
    }
}

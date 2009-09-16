package org.activityinfo.client;

import com.extjs.gxt.ui.client.event.EventType;
import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.BaseObservable;
import com.extjs.gxt.ui.client.event.Listener;
import com.google.gwt.core.client.GWT;

/**
 * @author Alex Bertram (akbertram@gmail.com)
 */
public class LoggingEventBus extends BaseObservable implements EventBus {

	public boolean fireEvent(BaseEvent event) {
		return fireEvent(event.getType(), event);
	}
	
    @Override
    public boolean fireEvent(EventType eventType, BaseEvent be) {

        GWT.log("EventBus: " + eventType.toString() + ": " + be.toString(), null);

        boolean doEvent = super.fireEvent(eventType, be);

        return doEvent;
    }

    @Override
    protected void callListener(Listener<BaseEvent> listener, BaseEvent be) {

        try {
            super.callListener(listener, be);
        } catch (Exception e) {
             
            // don't allow one misbehaving component to throw
            // everything off
            GWT.log("EventBus: " + listener.getClass().toString() + " threw an exception while receiving the event " +
                    be.toString(), e);
        }
    }
}

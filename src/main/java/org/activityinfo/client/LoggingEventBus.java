

package org.activityinfo.client;

/*
 * #%L
 * ActivityInfo Server
 * %%
 * Copyright (C) 2009 - 2013 UNICEF
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the 
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public 
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */

import org.activityinfo.client.Log;
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

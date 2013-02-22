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

import java.util.ArrayList;
import java.util.List;

import org.activityinfo.client.event.NavigationEvent;

import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.BaseObservable;
import com.extjs.gxt.ui.client.event.EventType;

/**
 * @author Alex Bertram (akbertram@gmail.com)
 */
public class MockEventBus extends BaseObservable implements EventBus {

    public List<BaseEvent> eventLog = new ArrayList<BaseEvent>();

    @Override
    public boolean fireEvent(EventType eventType, BaseEvent be) {
        eventLog.add(be);
        return super.fireEvent(eventType, be);
    }

    @Override
    public boolean fireEvent(BaseEvent event) {
        return fireEvent(event.getType(), event);
    }

    public void assertEventFired(BaseEvent event) {
        if (!eventLog.contains(event)) {
            throw new AssertionError();
        }
    }

    public int getEventCount(EventType type) {
        int count = 0;
        for (BaseEvent event : eventLog) {

            if (event.getType() == type) {
                count++;
            }
        }
        return count;
    }

    public <T> T getLastNavigationEvent(Class<T> placeClass) {
        for (int i = eventLog.size() - 1; i >= 0; i--) {
            BaseEvent event = eventLog.get(i);
            if (event instanceof NavigationEvent) {
                NavigationEvent nevent = (NavigationEvent) event;
                if (placeClass.isAssignableFrom(nevent.getPlace().getClass())) {
                    return (T) nevent.getPlace();
                }
            }
        }
        return null;
    }

    public void assertNotFired(EventType eventType) {
        if (getEventCount(eventType) != 0) {
            throw new AssertionError("eventType" + eventType.toString()
                + " has not been fired");
        }
    }
}

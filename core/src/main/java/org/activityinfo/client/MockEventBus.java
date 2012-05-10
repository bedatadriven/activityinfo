/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.activityinfo.client;


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
    	if(!eventLog.contains(event)) {
    		throw new AssertionError();
    	}
    }

    public int getEventCount(EventType type) {
        int count = 0;
        for(BaseEvent event : eventLog) {

            if(event.getType() == type) {
                count++;
            }
       }
        return count;
    }




    public <T> T getLastNavigationEvent(Class<T> placeClass) {
        for(int i=eventLog.size()-1; i>=0;i--) {
            BaseEvent event = eventLog.get(i);
            if(event instanceof NavigationEvent) {
                NavigationEvent nevent = (NavigationEvent)event;
                if(placeClass.isAssignableFrom(nevent.getPlace().getClass())) {
                    return (T)nevent.getPlace();
                }
            }
        }
        return null;
    }

    public void assertNotFired(EventType eventType) {
    	if(getEventCount(eventType)==0) {
    		throw new AssertionError("eventType" + eventType.toString() + " has not been fired");
    	}
    }
}

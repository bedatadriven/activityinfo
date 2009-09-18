package org.activityinfo.client.event;

import com.extjs.gxt.ui.client.event.BaseEvent;

import org.activityinfo.client.AppEvents;

public class ConnectionEvent extends BaseEvent {

    private boolean connected;

    public ConnectionEvent(boolean connected) {
        super(AppEvents.ConnectionStatusChange);
        this.connected = connected;
    }

    @Override
    public String toString() {
        if(connected) {
            return "Connection regained";
        } else {
            return "Connection lost";
        }
        
    }
}

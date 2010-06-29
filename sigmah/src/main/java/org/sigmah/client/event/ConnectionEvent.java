package org.sigmah.client.event;

import com.extjs.gxt.ui.client.event.BaseEvent;
import org.sigmah.client.AppEvents;

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

package org.activityinfo.client.event;

import com.extjs.gxt.ui.client.event.BaseEvent;

import org.activityinfo.client.AppEvents;

public class AuthenticationEvent extends BaseEvent {

    private String authToken;
    private String email;

    public AuthenticationEvent(String authToken, String email) {
        super(AppEvents.Authenticated);
        this.authToken = authToken;
        this.email = email;
    }

    public String getAuthToken() {
        return authToken;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public String toString() {
        return authToken;
    }

}

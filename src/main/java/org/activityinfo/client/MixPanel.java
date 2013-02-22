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

import javax.inject.Inject;

import org.activityinfo.client.event.NavigationEvent;
import org.activityinfo.client.page.NavigationHandler;
import org.activityinfo.shared.auth.AuthenticatedUser;

import com.extjs.gxt.ui.client.event.Listener;

public class MixPanel {

    @Inject
    public MixPanel(AuthenticatedUser user, EventBus eventBus) {
        identify(user);
        eventBus.addListener(NavigationHandler.NAVIGATION_REQUESTED,
            new Listener<NavigationEvent>() {

                @Override
                public void handleEvent(NavigationEvent be) {
                    track("navigate:" + be.getPlace().getPageId());
                }
            });
    }

    private static native void identify(String id) /*-{
                                                   $wnd.mixpanel.identify(id);
                                                   }-*/;

    private static native void doTrack(String eventName) /*-{
                                                         $wnd.mixpanel.track(eventName);
                                                         }-*/;

    public static void track(String eventName) {
        try {
            doTrack(eventName);
        } catch (Exception e) {
            Log.error("Mixpanel.track threw exception", e);
        }

    }

    public static void identify(AuthenticatedUser user) {
        try {
            identify("u" + user.getId());
        } catch (Exception e) {
            Log.error("Mixpanel.identify threw exception", e);
        }
    }

}

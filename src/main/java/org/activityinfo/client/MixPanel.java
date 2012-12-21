package org.activityinfo.client;

import javax.inject.Inject;

import org.activityinfo.client.event.NavigationEvent;
import org.activityinfo.client.page.NavigationHandler;
import org.activityinfo.login.shared.AuthenticatedUser;

import com.extjs.gxt.ui.client.event.Listener;

public class MixPanel {
	
	@Inject
	public MixPanel(AuthenticatedUser user, EventBus eventBus) {
		identify(user);
		eventBus.addListener(NavigationHandler.NavigationRequested, new Listener<NavigationEvent>() {

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
		} catch(Exception e) {
			Log.error("Mixpanel.track threw exception", e);
		}
		
	}
	
	public static void identify(AuthenticatedUser user)  {
		try {
			identify("u" + user.getId());
		} catch(Exception e) {
			Log.error("Mixpanel.identify threw exception", e);
		}
	}
	
	
}

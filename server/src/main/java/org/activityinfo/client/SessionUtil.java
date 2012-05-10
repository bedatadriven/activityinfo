package org.activityinfo.client;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.Window.Location;

public class SessionUtil {

	public static void forceLogin() {
		Window.Location.assign("/login");
	}
	
	public static void logout() {
		Window.Location.assign("/logout");
	}

}

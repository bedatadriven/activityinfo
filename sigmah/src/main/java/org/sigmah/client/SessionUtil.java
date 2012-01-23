package org.sigmah.client;

import com.google.gwt.user.client.Window;

public final class SessionUtil {
	
	private SessionUtil() {}

	public static void forceLogin() {
		Window.Location.assign("/content/");
	}

	public static void logout() {
		Window.Location.assign("/logout");
	}

}

package org.sigmah.client;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.Window.Location;

public class SessionUtil {

	public static void forceLogin() {
		Window.Location.assign("/login");
	}

}

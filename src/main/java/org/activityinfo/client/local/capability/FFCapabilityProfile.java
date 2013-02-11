package org.activityinfo.client.local.capability;


import org.activityinfo.client.Log;
import com.google.gwt.user.client.Window;

/**
 * FireFox offline capability profile.
 * 
 * For the most part, firefox does not support ActivityInfo's offline
 * mode because they have chosen to go with indexedb over websql. So
 * we encourage users to download chrome instead.
 * 
 * However, we do handle the exceptional case where the user has an old
 * version of FireFox (<= 3.6) that supports the Gears plugin. 
 */
public class FFCapabilityProfile extends GearsCapabilityProfile {

	private static final double FIREFOX_4 = 2.0;
	
	private double revision;
	
	public FFCapabilityProfile() {
		Log.debug("FireFox version: " + Window.Navigator.getUserAgent());
		String rv = fireFoxVersion();
		revision = Double.parseDouble(rv);
	}

	@Override
	public String getInstallInstructions() {
		if(isOfflineModeSupported()) {
			return ProfileResources.INSTANCE.startupMessage().getText();
		} else if(revision < FIREFOX_4) {
			return ProfileResources.INSTANCE.startupMessageFirefox().getText() +
				   ProfileResources.INSTANCE.startupMessageFirefox36().getText();

		} else {
			return ProfileResources.INSTANCE.startupMessageFirefox().getText();
		}
	}
	
	private static native String fireFoxVersion() /*-{
		var pattern = /rv:(\d+\.\d+)/;
		$wnd.navigator.userAgent.match(pattern);
		return RegExp.$1;
	}-*/;
}

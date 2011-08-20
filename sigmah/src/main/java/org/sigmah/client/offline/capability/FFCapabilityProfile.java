package org.sigmah.client.offline.capability;


import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.gears.client.Factory;
import com.google.gwt.user.client.Window;

/**
 * 
 */
public class FFCapabilityProfile extends OfflineCapabilityProfile {

	private static final double FIREFOX_4 = 2.0;
	
	private double revision;
	
	public FFCapabilityProfile() {
		Log.debug("FireFox version: " + Window.Navigator.getUserAgent());
		String rv = fireFoxVersion();
		revision = Double.parseDouble(rv);
	}
	
	@Override
	public boolean isOfflineModeSupported() {
		return gearsIsInstalled() || revision < FIREFOX_4;
	}

	@Override
	public String getStartupMessageHtml() {
		if(gearsIsInstalled()) {
			return ProfileResources.INSTANCE.startupMessage().getText();
		} else if(revision < FIREFOX_4) {
			return ProfileResources.INSTANCE.startupMessageFirefox().getText() +
				   ProfileResources.INSTANCE.startupMessageFirefox36().getText();

		} else {
			return ProfileResources.INSTANCE.startupMessageFirefox().getText();
		}
	}
	
	private boolean gearsIsInstalled() {
		return Factory.getInstance() != null;
	}

	
	private static native String fireFoxVersion() /*-{
		var pattern = /rv:(\d+\.\d+)/;
		$wnd.navigator.userAgent.match(pattern);
		return RegExp.$1;
	}-*/;
}

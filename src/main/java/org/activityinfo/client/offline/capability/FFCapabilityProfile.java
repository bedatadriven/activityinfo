package org.activityinfo.client.offline.capability;


import org.activityinfo.client.Log;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;

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
public class FFCapabilityProfile extends OfflineCapabilityProfile {

	
	private boolean hasPlugin;
	
	public FFCapabilityProfile() {
		Log.debug("FireFox version: " + Window.Navigator.getUserAgent());
		hasPlugin = hasPlugin();
	}

	@Override
	public boolean isOfflineModeSupported() {
		return hasPlugin;
	}


	@Override
	public void acquirePermission(AsyncCallback<Void> callback) {
		callback.onSuccess(null);
	}

	@Override
	public String getInstallInstructions() {
		if(isOfflineModeSupported()) {
			return ProfileResources.INSTANCE.startupMessage().getText();
		} else {
			return ProfileResources.INSTANCE.startupMessageFirefox().getText();
		}
	}
	
	private static native boolean hasPlugin() /*-{
		return !!$wnd.openDatabase;
	}-*/;
}

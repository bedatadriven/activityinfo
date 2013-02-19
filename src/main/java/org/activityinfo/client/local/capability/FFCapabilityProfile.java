package org.activityinfo.client.local.capability;


import org.activityinfo.client.Log;
import org.activityinfo.client.authentication.ClientSideAuthProvider;
import org.activityinfo.client.offline.capability.FFPermissionsDialog;
import org.activityinfo.shared.auth.AuthenticatedUser;

import com.bedatadriven.rebar.appcache.client.AppCache;
import com.bedatadriven.rebar.appcache.client.AppCache.Status;
import com.bedatadriven.rebar.appcache.client.AppCacheFactory;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * FireFox offline capability profile.
 * 
 * <p>Though Mozilla has decided not to implement WebSQL, we use
 * a add-on to expose the sqlite database service Mozilla provides
 * for extensions.
 */
public class FFCapabilityProfile extends LocalCapabilityProfile {

	
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
	public void acquirePermission(final AsyncCallback<Void> callback) {
		
		final AppCache appCache = AppCacheFactory.get();
		if(appCache.getStatus() != Status.UNCACHED) {
			callback.onSuccess(null);
		} else {
			FFPermissionsDialog dialog = new FFPermissionsDialog(callback);
			dialog.show();
		}
	}

	private String createReceipt() {
		AuthenticatedUser user = new ClientSideAuthProvider().get();
		StringBuilder receipt = new StringBuilder();
		receipt.append(user.getUserId()).append("|")
			.append(user.getAuthToken()).append("|")
			.append(user.getEmail());
		return receipt.toString();
	}

	@Override
	public String getInstallInstructions() {
		return ProfileResources.INSTANCE.startupMessageFirefox().getText();
	}
	
	private static native boolean hasPlugin() /*-{
		return !!$wnd.openDatabase;
	}-*/;
	
	

}

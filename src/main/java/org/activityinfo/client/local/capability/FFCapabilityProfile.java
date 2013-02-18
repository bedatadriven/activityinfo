package org.activityinfo.client.local.capability;


import org.activityinfo.client.Log;
import org.activityinfo.client.authentication.ClientSideAuthProvider;
import org.activityinfo.client.util.mozApp.MozApp;
import org.activityinfo.client.util.mozApp.MozAppsApi;
import org.activityinfo.shared.auth.AuthenticatedUser;

import com.bedatadriven.rebar.appcache.client.AppCache;
import com.bedatadriven.rebar.appcache.client.AppCache.Status;
import com.bedatadriven.rebar.appcache.client.AppCacheFactory;
import com.bedatadriven.rebar.async.NullCallback;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * FireFox offline capability profile.
 * 
 * <p>Though Mozilla has decided not to implement WebSQL, we use
 * a add-on to expose the sqlite database service Mozilla provides
 * for extensions.
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
	public void acquirePermission(final AsyncCallback<Void> callback) {
		doInstallViaAppsApi(NullCallback.forVoid());
		
		final AppCache appCache = AppCacheFactory.get();
		if(appCache.getStatus() != Status.UNCACHED) {
			callback.onSuccess(null);
		} else {
			FFPermissionsDialog dialog = new FFPermissionsDialog(callback);
			dialog.show();
		}
	}

	private void doInstallViaAppsApi(final AsyncCallback<Void> callback) {
		MozAppsApi.install(GWT.getHostPageBaseURL() + "ActivityInfo.webapp",
				createReceipt(),
				new AsyncCallback<MozApp>() {
			
			@Override
			public void onSuccess(MozApp result) {
				callback.onSuccess(null);
			}
			
			@Override
			public void onFailure(Throwable caught) {
				callback.onFailure(caught);
			}
		});
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

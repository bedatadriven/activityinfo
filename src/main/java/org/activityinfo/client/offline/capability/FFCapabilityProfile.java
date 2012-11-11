package org.activityinfo.client.offline.capability;


import org.activityinfo.client.Log;
import org.activityinfo.client.authentication.ClientSideAuthProvider;
import org.activityinfo.shared.auth.AuthenticatedUser;

import com.bedatadriven.rebar.appcache.client.AppCache;
import com.bedatadriven.rebar.appcache.client.AppCache.Status;
import com.bedatadriven.rebar.appcache.client.AppCacheFactory;
import com.bedatadriven.rebar.async.NullCallback;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.MessageBoxEvent;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JavaScriptObject;
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
			MessageBox.alert("Offline Mode", "FireFox requires your permission before enabling offline mode." +
					"Please click the 'Allow' button at the top of this window." +
					"If you do not see an 'Allow' button, you may need to reload the page" +
					"before continuing.", new Listener<MessageBoxEvent>() {

				@Override
				public void handleEvent(MessageBoxEvent be) {
					if(appCache.getStatus() == Status.UNCACHED) {
						callback.onFailure(new PermissionRefusedException());
					} else {
						callback.onSuccess(null);
					}
				}				
			});
		}
//		
//		try {
//			if(installSupported()) {
//				doInstallViaAppsApi(callback);
//			} else {
//				callback.onSuccess(null);
//			}
//		} catch(Exception e) {
//			callback.onFailure(e);
//		}
	}

	private void doInstallViaAppsApi(final AsyncCallback<Void> callback) {
		install(GWT.getModuleBaseURL() + "ActivityInfo.webapp",
				createReceipt(),
				new AsyncCallback<FFCapabilityProfile.MozApp>() {
			
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
	
	private static native boolean installSupported() /*-{
		return !!$wnd.navigator.mozApps;
	}-*/;
	
	private static native void install(String url, String receipt, AsyncCallback<MozApp> callback) /*-{
		var request = $wnd.navigator.mozApps.install(url, [receipt]);
		request.onsuccess = function() {
			callback.@com.google.gwt.user.client.rpc.AsyncCallback::onSuccess(Ljava/lang/Object;)(this.result);
		};
		request.onerror = function() {
			@org.activityinfo.client.offline.capability.FFCapabilityProfile::throwAppError(Ljava/lang/String;)(this.error.name);
		};
	}-*/;
	
	
	private static void throwAppError(String name) {
		throw new RuntimeException(name);
	}
	
	private static final class MozApp extends JavaScriptObject {
		protected MozApp() { }
	}
	
}

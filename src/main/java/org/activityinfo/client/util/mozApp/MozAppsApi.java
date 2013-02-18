package org.activityinfo.client.util.mozApp;

import com.google.gwt.user.client.rpc.AsyncCallback;

public class MozAppsApi {
	
	public static native boolean isSupported() /*-{
		return !!$wnd.navigator.mozApps;
	}-*/;

	public static native void install(String url, String receipt, AsyncCallback<MozApp> callback) /*-{
		var request = $wnd.navigator.mozApps.install(url, [receipt]);
		request.onsuccess = function() {
			callback.@com.google.gwt.user.client.rpc.AsyncCallback::onSuccess(Ljava/lang/Object;)(this.result);
		};
		request.onerror = function() {
			@org.activityinfo.client.util.mozApp.MozAppsApi::throwAppError(Ljava/lang/String;)(this.error.name);
		};
	}-*/;
	
	public static native void getSelf(AsyncCallback<MozApp> callback) /*-{
		var request = $wnd.navigator.mozApps.getSelf();
		request.onsuccess = function() {
			callback.@com.google.gwt.user.client.rpc.AsyncCallback::onSuccess(Ljava/lang/Object;)(this.result);
		};
		request.onerror = function() {
			@org.activityinfo.client.util.mozApp.MozAppsApi::throwAppError(Ljava/lang/String;)(this.error.name);
		};
	}-*/;
	
	private static void throwAppError(String name) {
		throw new RuntimeException(name);
	}
	
}

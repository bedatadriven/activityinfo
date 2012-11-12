package org.activityinfo.client.util.mozApp;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArrayString;

public final class MozApp extends JavaScriptObject {

	protected MozApp() {
		
	}
	
	public native String getManifestURL() /*-{
		return this.manifestURL;
	}-*/;
	
	public native MozAppManifest getManifest() /*-{
		return this.manifest;
	}-*/;
	
	public native String getName() /*-{
		return this.manifestURL;
	}-*/;
	
	public native JsArrayString getReceipts() /*-{
		return this.receipts || [];
	}-*/;
}

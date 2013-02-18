package org.activityinfo.client.util.mozApp;

import com.google.gwt.core.client.JavaScriptObject;

public final class MozAppManifest extends JavaScriptObject {

	protected MozAppManifest() {
		
	}
	
	public native String getName() /*-{
		return this.name;
	}-*/;
}

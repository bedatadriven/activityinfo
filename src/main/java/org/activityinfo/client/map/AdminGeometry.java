package org.activityinfo.client.map;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArray;

public final class AdminGeometry extends JavaScriptObject {

	protected AdminGeometry() { }
	
	public native int getZoomFactor() /*-{
		return this.zoomFactor;
	}-*/;
	
	public native int getNumLevels() /*-{
		return this.numLevels;
	}-*/;
	
	public native JsArray<AdminEntityGeometry> getEntities() /*-{
		return this.entities;
	}-*/;

	public static final native AdminGeometry fromJson(String input) /*-{ 
	    return eval('(' + input + ')');
	}-*/;  
	
}

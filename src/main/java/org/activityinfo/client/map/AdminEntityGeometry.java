package org.activityinfo.client.map;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArray;

public final class AdminEntityGeometry extends JavaScriptObject {

	protected AdminEntityGeometry() {
		
	}
	

	public native int getAdminEntityId() /*-{
		return this.id;
	}-*/;
	
	public native JsArray<AdminPolygon> getPolygons() /*-{
		return this.polygons;
	}-*/;
	
	
}

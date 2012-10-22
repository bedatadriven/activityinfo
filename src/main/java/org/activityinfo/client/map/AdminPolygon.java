package org.activityinfo.client.map;

import com.google.gwt.core.client.JavaScriptObject;

public final class AdminPolygon extends JavaScriptObject {
	
	protected AdminPolygon() {
		
	}
	
	public native String getPoints() /*-{
		return this.points;
	}-*/;
	
	public native String getLevels() /*-{
		return this.levels;
	}-*/;
}

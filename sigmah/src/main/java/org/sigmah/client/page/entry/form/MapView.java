package org.sigmah.client.page.entry.form;

import org.sigmah.client.dispatch.AsyncMonitor;
import org.sigmah.shared.report.content.AiLatLng;
import org.sigmah.shared.util.mapping.BoundingBoxDTO;

import com.google.gwt.user.client.ui.Widget;

public interface MapView  {
	public Widget asWidget();
	public void initialize();

	// The map area the user can see
	public void setViewBounds(BoundingBoxDTO bounds);
	public BoundingBoxDTO getViewBounds();
	
	/*
	 * The UI displaying loading, network status (retry/error/complete)
	 * Usually some standard Async monitor UI view, such as NullAsyncMonitor,
	 * MaskingAsyncMonitor etc
	 * 
	 */
	public AsyncMonitor getAsyncMonitor();
	
	// A marker has a lat/lng coordinate
	void setValue(AiLatLng value);

	AiLatLng getValue();
}

package org.sigmah.client.page.entry.editor;

import org.sigmah.client.dispatch.AsyncMonitor;
import org.sigmah.shared.report.content.AiLatLng;
import org.sigmah.shared.util.mapping.BoundingBoxDTO;

import com.google.gwt.user.client.ui.Widget;

public interface MapView  {
    public void setMapView(BoundingBoxDTO bounds);
	public Widget asWidget();
	public void initialize();
	public BoundingBoxDTO getBoundingBox();
	
	/*
	 * The UI displaying loading, network status (retry/error/complete)
	 * Usually some standard Async monitor UI view, such as NullAsyncMonitor,
	 * MaskingAsyncMonitor etc
	 * 
	 */
	public AsyncMonitor getAsyncMonitor();
	  void setValue(AiLatLng value);

	  /**
	   * Returns the current value.
	   * 
	   * @return the value as an object of type V
	   * @see #setValue
	   */
	  AiLatLng getValue();
}

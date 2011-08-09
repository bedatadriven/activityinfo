/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.client.page.entry.editor.mock;

import org.sigmah.client.dispatch.AsyncMonitor;
import org.sigmah.client.page.entry.editor.MapEditView;
import org.sigmah.shared.report.content.AiLatLng;
import org.sigmah.shared.util.mapping.BoundingBoxDTO;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author Alex Bertram (akbertram@gmail.com)
 */
public class MockMapView implements MapEditView {

    public String boundsName;
    public BoundingBoxDTO bounds;
    public Double lat;
    public Double lng;
    public BoundingBoxDTO mapView;
    private AiLatLng latLng;
    private EventBus eventBus = new SimpleEventBus(); 

    public Double markerX;
    public Double markerY;

    @Override
    public void setBounds(String name, BoundingBoxDTO bounds) {
        this.boundsName = name;
        this.bounds = bounds;
    }

    @Override
    public Double getX() {
        return lng;
    }

    @Override
    public Double getY() {
        return lat;
    }

    @Override
    public void setMapView(BoundingBoxDTO bounds) {
        this.mapView = bounds;
    }

	@Override
	public Widget asWidget() {
		return null;
	}

	@Override
	public void initialize() {
		
	}

	@Override
	public AsyncMonitor getAsyncMonitor() {
		return null;
	}

	@Override
	public void setValue(AiLatLng value) {
		latLng = value;
	}

	@Override
	public AiLatLng getValue() {
		return latLng;
	}

	@Override
	public void setMarkerPosition(AiLatLng latLng) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public BoundingBoxDTO getBounds() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void panTo(AiLatLng latLng) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public HandlerRegistration addMarkerMovedHandler(MarkerMovedHandler handler) {
		return eventBus.addHandler(MarkerMovedEvent.TYPE, handler);
	}

	@Override
	public HandlerRegistration addCoordinatesChangedHandler(
			CoordinatesChangedHandler handler) {
		return eventBus.addHandler(CoordinatesChangedEvent.TYPE, handler);
	}

	@Override
	public HandlerRegistration addMapViewChangedHandler(
			MapViewChangedHandler handler) {
		return eventBus.addHandler(MapViewChangedEvent.TYPE, handler);
	}

	@Override
	public BoundingBoxDTO getBoundingBox() {
		// TODO Auto-generated method stub
		return null;
	}
}

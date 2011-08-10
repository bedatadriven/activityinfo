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

	private String boundsName;
	private BoundingBoxDTO editBounds;
	private BoundingBoxDTO viewBounds;
    private AiLatLng latLng = new AiLatLng(-1000.0, -1000.0);
    private AiLatLng marker = new AiLatLng(-1000.0, -1000.0);
    private EventBus eventBus = new SimpleEventBus(); 

    public Double markerX;
    public Double markerY;

    @Override
    public void setEditBounds(String name, BoundingBoxDTO bounds) {
        this.boundsName = name;
        this.editBounds = bounds;
    }

    @Override
    public Double getX() {
        return latLng.getLng();
    }

    @Override
    public Double getY() {
        return latLng.getLat();
    }

    @Override
    public void setViewBounds(BoundingBoxDTO bounds) {
        this.viewBounds = bounds;
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
		this.latLng=latLng;
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
	public BoundingBoxDTO getViewBounds() {
		return viewBounds;
	}
}

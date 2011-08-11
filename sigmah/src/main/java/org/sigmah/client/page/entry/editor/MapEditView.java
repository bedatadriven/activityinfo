package org.sigmah.client.page.entry.editor;

import org.sigmah.shared.report.content.AiLatLng;
import org.sigmah.shared.util.mapping.BoundingBoxDTO;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;

public interface MapEditView extends MapView {
    public Double getX();
    public Double getY();
    public void setMarkerPosition(AiLatLng latLng);
    
    // Area where the user is forced to keep the marker within the specified bounds when
    // changing the position of the marker
    public void setEditBounds(String name, BoundingBoxDTO bounds);
    public void panTo(AiLatLng latLng);
    
    public HandlerRegistration addMarkerMovedHandler(MarkerMovedHandler handler);
    public HandlerRegistration addCoordinatesChangedHandler(CoordinatesChangedHandler handler);
    public HandlerRegistration addMapViewChangedHandler(MapViewChangedHandler handler);
    
    public interface MarkerMovedHandler extends EventHandler {
    	public void onMarkedMoved(MarkerMovedEvent event);
    }
    public interface CoordinatesChangedHandler extends EventHandler {
    	public void onCoordinatesChanged(CoordinatesChangedEvent event);
    }
    public interface MapViewChangedHandler extends EventHandler {
    	public void onMapViewChanged(MapViewChangedEvent event);
    }
    
    public class MarkerMovedEvent extends GwtEvent<MarkerMovedHandler> {
		public static Type<MarkerMovedHandler> TYPE = new Type<MarkerMovedHandler>();
    	double my;
    	double mx;
		
    	public MarkerMovedEvent(double my, double mx) {
			this.my = my;
			this.mx = mx;
		}

		public double getMy() {
			return my;
		}

		public double getMx() {
			return mx;
		}

		@Override
		public Type<MarkerMovedHandler> getAssociatedType() {
			return TYPE;
		}

		@Override
		protected void dispatch(MarkerMovedHandler handler) {
			handler.onMarkedMoved(this);
		}
    }
    
    public class CoordinatesChangedEvent extends GwtEvent<CoordinatesChangedHandler> {
    	public static Type<CoordinatesChangedHandler> TYPE = new Type<CoordinatesChangedHandler>();
    	AiLatLng coordinates;
    	
    	
		public CoordinatesChangedEvent(AiLatLng coordinates) {
			super();
			this.coordinates = coordinates;
		}

		public AiLatLng getCoordinates() {
			return coordinates;
		}

		public void setCoordinates(AiLatLng coordinates) {
			this.coordinates = coordinates;
		}

		@Override
		public Type<CoordinatesChangedHandler> getAssociatedType() {
			return TYPE;
		}

		@Override
		protected void dispatch(CoordinatesChangedHandler handler) {
			handler.onCoordinatesChanged(this);
		}
    }
    
    public class MapViewChangedEvent extends GwtEvent<MapViewChangedHandler> {
    	public static Type<MapViewChangedHandler> TYPE = new Type<MapViewChangedHandler>();
    	private BoundingBoxDTO boundingBox;
    	
    	public MapViewChangedEvent(BoundingBoxDTO boundingBox) {
			this.boundingBox = boundingBox;
		}

		public BoundingBoxDTO getBoundingBox() {
			return boundingBox;
		}

		@Override
		public Type<MapViewChangedHandler> getAssociatedType() {
			return TYPE;
		}

		@Override
		protected void dispatch(MapViewChangedHandler handler) {
			handler.onMapViewChanged(this);
		}
    }
}

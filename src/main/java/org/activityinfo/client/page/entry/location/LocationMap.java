package org.activityinfo.client.page.entry.location;

import org.activityinfo.client.i18n.I18N;
import org.activityinfo.client.map.GoogleChartsIconBuilder;
import org.activityinfo.client.map.MapTypeFactory;
import org.activityinfo.client.widget.CoordinateEditor;
import org.activityinfo.client.widget.GoogleMapsPanel;
import org.activityinfo.client.widget.CoordinateField.Axis;
import org.activityinfo.shared.dto.LocationDTO;
import org.activityinfo.shared.report.content.AiLatLng;
import org.activityinfo.shared.util.mapping.Extents;

import org.activityinfo.client.Log;
import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.store.StoreEvent;
import com.extjs.gxt.ui.client.store.StoreListener;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.Lists;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.maps.client.InfoWindowContent;
import com.google.gwt.maps.client.MapType;
import com.google.gwt.maps.client.MapWidget;
import com.google.gwt.maps.client.control.SmallMapControl;
import com.google.gwt.maps.client.event.MapClickHandler;
import com.google.gwt.maps.client.event.MapZoomEndHandler;
import com.google.gwt.maps.client.event.MarkerDragEndHandler;
import com.google.gwt.maps.client.geom.LatLng;
import com.google.gwt.maps.client.geom.LatLngBounds;
import com.google.gwt.maps.client.overlay.Icon;
import com.google.gwt.maps.client.overlay.Marker;
import com.google.gwt.maps.client.overlay.MarkerOptions;

public class LocationMap extends GoogleMapsPanel {

	private final LocationSearchPresenter searchPresenter;
	private final NewLocationPresenter newLocationPresenter;

	private final BiMap<LocationDTO, Marker> searchMarkers = HashBiMap.create();
	private Marker newLocationMarker;
	
	public LocationMap(LocationSearchPresenter presenter, NewLocationPresenter newLocationPresenter) {
		super();
		this.searchPresenter = presenter;
		this.newLocationPresenter = newLocationPresenter;
		
        setHeaderVisible(false);
	}

	@Override
	protected void configureMap(MapWidget map) {
		Extents countryBounds = searchPresenter.getCountry().getBounds();
		map.addControl(new SmallMapControl());
        map.setCenter(LatLng.newInstance(
                countryBounds.getCenterY(),
                countryBounds.getCenterX()));
        map.setZoomLevel(6);

        MapType adminMap = MapTypeFactory.createLocalisationMapType(searchPresenter.getCountry());
        map.addMapType(adminMap);
        map.setCurrentMapType(adminMap);
	}

	@Override
	protected void onMapInitialized() {
		
		searchPresenter.getStore().addStoreListener(new StoreListener<LocationDTO>() {

			@Override
			public void storeDataChanged(StoreEvent<LocationDTO> event) {
				updateSearchMarkers();
			}
		});
		searchPresenter.addListener(Events.Select, new Listener<LocationEvent>() {

			@Override
			public void handleEvent(LocationEvent event) {
				if(event.getSource() != LocationMap.this) {
					onLocationSelected(event.getLocation());
				}
			}
		});
		
		newLocationPresenter.addListener(NewLocationPresenter.ACTIVE_STATE_CHANGED, new Listener<BaseEvent>() {
			@Override
			public void handleEvent(BaseEvent be) {
				onModeChanged();
			}
		});
		
		newLocationPresenter.addListener(NewLocationPresenter.POSITION_CHANGED, new Listener<BaseEvent>() {

			@Override
			public void handleEvent(BaseEvent be) {
				onNewLocationPosChanged();
			}
		});
		
		newLocationPresenter.addListener(NewLocationPresenter.BOUNDS_CHANGED, new Listener<BaseEvent>() {

			@Override
			public void handleEvent(BaseEvent be) {
				if(newLocationPresenter.isActive()) {	
					LatLngBounds newBounds = newLatLngBounds(newLocationPresenter.getBounds());
					zoomToBounds(newBounds);
				}
			}
		});
		
		getMapWidget().addMapClickHandler(new MapClickHandler() {

			@Override
			public void onClick(MapClickEvent event) {
				if(event.getOverlay() instanceof Marker) {
					onMarkerClicked((Marker)event.getOverlay());
				}
			}
		});
		
		getMapWidget().addMapZoomEndHandler(new MapZoomEndHandler() {

			@Override
			public void onZoomEnd(MapZoomEndEvent event) {
				if(newLocationPresenter.isActive()) {
					ensureNewLocationMarkerIsVisible();
				}
			}
		});
	}

	private void updateSearchMarkers() {
		clearSearchMarkers();
		
		GoogleChartsIconBuilder iconBuilder = new GoogleChartsIconBuilder();
		
		Extents bounds = Extents.empty();
		
		for(LocationDTO location : Lists.reverse(searchPresenter.getStore().getModels())) {
			if(location.hasCoordinates()) {
				iconBuilder.setLabel(location.getMarker());
				Icon icon = iconBuilder.createPinUrl();
				
				MarkerOptions options = MarkerOptions.newInstance(icon);
				Marker marker = new Marker(LatLng.newInstance(location.getLatitude(), location.getLongitude()), 
						options);
				getMapWidget().addOverlay(marker);
				
				bounds.grow(location.getLongitude(), location.getLatitude());
				
				searchMarkers.put(location, marker);
			}
		}
		
		if(searchMarkers.isEmpty()) {
			if(searchPresenter.getBounds() == null) {
				Log.debug("searchPresenter.getBounds() is null");
			} else {
				zoomToBounds(newLatLngBounds(searchPresenter.getBounds()));
			}
		} else {
			zoomToBounds(newLatLngBounds(bounds));
		}
	}

	private void clearSearchMarkers() {
		for(Marker marker : searchMarkers.values()) {
			getMapWidget().removeOverlay(marker);
		}
		searchMarkers.clear();
	}	

	private void onLocationSelected(LocationDTO location) {
		if(location.hasCoordinates()) {
			getMapWidget().panTo(LatLng.newInstance(location.getLatitude(), location.getLongitude()));
		}
	}

	private void onMarkerClicked(Marker overlay) {
		LocationDTO location = searchMarkers.inverse().get(overlay);
		if(location != null) {
			searchPresenter.select(this, location);
			getMapWidget().getInfoWindow().open(overlay, 
					createContent(location));
		
		}
	}
	
	private void onModeChanged() {
		if(newLocationPresenter.isActive()) {
			if(newLocationMarker == null) {
				createNewLocationMarker();
			}
			newLocationMarker.setVisible(true);
			ensureNewLocationMarkerIsVisible();
		} else if(newLocationMarker != null) {
			newLocationMarker.setVisible(false);
		}
	}
	
	private void createNewLocationMarker() {
		GoogleChartsIconBuilder iconBuilder = new GoogleChartsIconBuilder();
		iconBuilder.setPrimaryColor("#0000FF");
		iconBuilder.setLabel("+");
		Icon icon = iconBuilder.createPinUrl();
		
		MarkerOptions options = MarkerOptions.newInstance(icon);
		options.setDraggable(true);
		
		newLocationMarker = new Marker(newLatLng(newLocationPresenter.getLatLng()), 
				options);
		newLocationMarker.addMarkerDragEndHandler(new MarkerDragEndHandler() {

			@Override
			public void onDragEnd(MarkerDragEndEvent event) {
				newLocationPresenter.setLatLng(new AiLatLng(
						newLocationMarker.getLatLng().getLatitude(), 
						newLocationMarker.getLatLng().getLongitude()));
			}
		});
		
		getMapWidget().addOverlay(newLocationMarker);
	}
	
	private void onNewLocationPosChanged() {
		if(newLocationMarker != null) {
			newLocationMarker.setLatLng(newLatLng(newLocationPresenter.getLatLng()));
		}
	}

	private void ensureNewLocationMarkerIsVisible() {
		if(!getMapWidget().getBounds().containsLatLng(newLocationMarker.getLatLng())) {
			getMapWidget().panTo(newLocationMarker.getLatLng());
		}
	}

	private InfoWindowContent createContent(LocationDTO dto) {
		
		NumberFormat decimalFormat = NumberFormat.getFormat("0.00000");
		CoordinateEditor longEditor = new CoordinateEditor(Axis.LONGITUDE);
		CoordinateEditor latEditor = new CoordinateEditor(Axis.LATITUDE);
		
		StringBuilder html = new StringBuilder();
		html.append("<b>")
			.append(dto.getName())
			.append("</b><br>");
		html.append(I18N.CONSTANTS.latitude())
			.append(": ")
			.append(latEditor.formatAsDMS(dto.getLatitude()))
			.append(" (")
			.append(decimalFormat.format(dto.getLatitude()))
			.append(")<br>");
			
		html.append(I18N.CONSTANTS.longitude())
			.append(": ")
			.append(longEditor.formatAsDMS(dto.getLongitude()))
			.append(" (")
			.append(decimalFormat.format(dto.getLongitude()))
			.append(")");
		
		return new InfoWindowContent(html.toString());
	}
}

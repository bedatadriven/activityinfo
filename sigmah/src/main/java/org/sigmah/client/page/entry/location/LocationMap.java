package org.sigmah.client.page.entry.location;

import org.sigmah.client.map.GoogleChartsIconBuilder;
import org.sigmah.client.widget.GoogleMapsWidget;
import org.sigmah.shared.dto.LocationDTO;
import org.sigmah.shared.report.content.AiLatLng;
import org.sigmah.shared.util.mapping.BoundingBoxDTO;

import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.store.StoreEvent;
import com.extjs.gxt.ui.client.store.StoreListener;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.Lists;
import com.google.gwt.maps.client.event.MapClickHandler;
import com.google.gwt.maps.client.event.MapZoomEndHandler;
import com.google.gwt.maps.client.event.MarkerDragEndHandler;
import com.google.gwt.maps.client.geom.LatLng;
import com.google.gwt.maps.client.geom.LatLngBounds;
import com.google.gwt.maps.client.overlay.Icon;
import com.google.gwt.maps.client.overlay.Marker;
import com.google.gwt.maps.client.overlay.MarkerOptions;

public class LocationMap extends GoogleMapsWidget {

	private final LocationSearchPresenter searchPresenter;
	private final NewLocationPresenter newLocationPresenter;

	private final BiMap<LocationDTO, Marker> searchMarkers = HashBiMap.create();
	private Marker newLocationMarker;
	
	public LocationMap(LocationSearchPresenter presenter, NewLocationPresenter newLocationPresenter) {
		super(presenter.getCountry());
		this.searchPresenter = presenter;
		this.newLocationPresenter = newLocationPresenter;
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
				if(event.getOverlay() != null) {
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
		
		BoundingBoxDTO bounds = BoundingBoxDTO.empty();
		
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
		zoomToBounds(newLatLngBounds(bounds));
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
}

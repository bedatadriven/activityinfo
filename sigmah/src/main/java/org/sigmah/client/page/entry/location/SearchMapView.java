package org.sigmah.client.page.entry.location;

import java.util.List;

import org.sigmah.client.map.GoogleChartsIconBuilder;
import org.sigmah.client.widget.GoogleMapsWidget;
import org.sigmah.shared.dto.LocationDTO;
import org.sigmah.shared.util.mapping.BoundingBoxDTO;

import com.extjs.gxt.ui.client.store.StoreEvent;
import com.extjs.gxt.ui.client.store.StoreListener;
import com.google.common.collect.Lists;
import com.google.gwt.maps.client.geom.LatLng;
import com.google.gwt.maps.client.overlay.Icon;
import com.google.gwt.maps.client.overlay.Marker;
import com.google.gwt.maps.client.overlay.MarkerOptions;

public class SearchMapView extends GoogleMapsWidget {

	private final LocationSearchPresenter presenter;
	private final List<Marker> markers = Lists.newArrayList();

	public SearchMapView(LocationSearchPresenter presenter) {
		super(presenter.getCountry());
		this.presenter = presenter;
	}

	@Override
	protected void onMapInitialized() {
		
		presenter.getStore().addStoreListener(new StoreListener<LocationDTO>() {

			@Override
			public void storeDataChanged(StoreEvent<LocationDTO> event) {
				updateMarkers();
			}
		});
	}

	protected void updateMarkers() {
		clearMarkers();
		
		GoogleChartsIconBuilder iconBuilder = new GoogleChartsIconBuilder();
		
		BoundingBoxDTO bounds = BoundingBoxDTO.empty();
		
		for(LocationDTO location : Lists.reverse(presenter.getStore().getModels())) {
			if(location.hasCoordinates()) {
				iconBuilder.setLabel(location.getMarker());
				Icon icon = iconBuilder.createPinUrl();
				
				MarkerOptions options = MarkerOptions.newInstance(icon);
				Marker marker = new Marker(LatLng.newInstance(location.getLatitude(), location.getLongitude()), 
						options);
				getMapWidget().addOverlay(marker);
				
				bounds.grow(location.getLongitude(), location.getLatitude());
				
				markers.add(marker);
			}
		}
		zoomToBounds(newLatLngBounds(bounds));
	}

	private void clearMarkers() {
		for(Marker marker : markers) {
			getMapWidget().removeOverlay(marker);
		}
	}	
}

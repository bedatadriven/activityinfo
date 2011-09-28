package org.sigmah.client.page.entry.editor;
import org.sigmah.client.i18n.I18N;
import org.sigmah.client.map.MapApiLoader;
import org.sigmah.client.map.MapTypeFactory;
import org.sigmah.shared.dto.CountryDTO;
import org.sigmah.shared.dto.LocationDTO2;

import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.google.gwt.maps.client.MapType;
import com.google.gwt.maps.client.MapWidget;
import com.google.gwt.maps.client.control.SmallMapControl;
import com.google.gwt.maps.client.geom.LatLng;
import com.google.gwt.maps.client.overlay.Marker;
import com.google.gwt.maps.client.overlay.MarkerOptions;
import com.google.gwt.user.client.rpc.AsyncCallback;

/** A mapwidget showing one location */
public class ShowLocationOnMap extends LayoutContainer {
	private MapWidget map;
	private CountryDTO country;
	private LocationDTO2 location;
	private boolean mapLoaded=false;
	
	public ShowLocationOnMap(CountryDTO country) {
		super();
		
		this.country=country;
		
		setLayout(new FitLayout());
		
		loadMapAsync();
	}

    private void loadMapAsync() {
    	MapApiLoader.load(null, new AsyncCallback<Void>() {
			@Override
			public void onSuccess(Void result) {
				createMapWidget();
				updateUI();
			}
			
			@Override
			public void onFailure(Throwable caught) {
				//ShowLocationOnMap.this.el().mask("Failure loading map. Check your internet connection");
			}
		});
	}
	
	protected void createMapWidget() {
		map = new MapWidget();

        map.addControl(new SmallMapControl());
        map.setZoomLevel(6);

        MapType adminMap = MapTypeFactory.createLocalisationMapType(country);
        map.addMapType(adminMap);
        map.setCurrentMapType(adminMap);

        add(map);
		mapLoaded=true;
	}

	public void setLocation(LocationDTO2 location) {
		this.location=location;
		updateUI();
	}
	
	private void updateUI() {
		if (mapLoaded && location != null) {
			if (location.hasCoordinates()) {
				el().unmask();
				map.clearOverlays();
				LatLng latLng = LatLng.newInstance(location.getLongitude(), location.getLatitude());
		        MarkerOptions options = MarkerOptions.newInstance();
		        options.setDraggable(false);
		        Marker marker = new Marker(latLng, options);
		        map.addOverlay(marker);
		        map.panTo(latLng);
			} else {
				el().mask(I18N.CONSTANTS.noCoordinates());
			}
		}
	}
	
}

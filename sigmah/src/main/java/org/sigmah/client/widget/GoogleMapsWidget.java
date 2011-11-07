/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.client.widget;


import org.sigmah.client.map.MapApiLoader;
import org.sigmah.client.map.MapTypeFactory;
import org.sigmah.shared.dto.CountryDTO;
import org.sigmah.shared.util.mapping.BoundingBoxDTO;

import com.allen_sauer.gwt.log.client.Log;
import com.extjs.gxt.ui.client.event.ContainerEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.google.gwt.maps.client.MapType;
import com.google.gwt.maps.client.MapWidget;
import com.google.gwt.maps.client.control.SmallMapControl;
import com.google.gwt.maps.client.geom.LatLng;
import com.google.gwt.maps.client.geom.LatLngBounds;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * Base class for integrating GoogleMaps widget into a GXT component
 */
public class GoogleMapsWidget extends LayoutContainer {
    private MapWidget map = null;

    private LatLngBounds pendingZoom = null;
    private final CountryDTO country;

    public GoogleMapsWidget(CountryDTO country) {
        this.country = country;
        
        setLayout(new FitLayout());
        setHeight(250);
    }
    
    

    @Override
	protected void onRender(Element parent, int index) {
		super.onRender(parent, index);
		
		loadMapAsync();
	}



	private void loadMapAsync() {
    	MapApiLoader.load(null, new AsyncCallback<Void>() {
			@Override
			public void onSuccess(Void result) {
				createMapWidget();				
			}
			
			@Override
			public void onFailure(Throwable caught) {
				Log.error("Failed to load google Maps", caught);
			}
		});
	}

	private void createMapWidget() {
		map = new MapWidget();

        map.addControl(new SmallMapControl());
        map.setCenter(LatLng.newInstance(
                country.getBounds().getCenterY(),
                country.getBounds().getCenterX()));
        map.setZoomLevel(6);

        MapType adminMap = MapTypeFactory.createLocalisationMapType(country);
        map.addMapType(adminMap);
        map.setCurrentMapType(adminMap);

        this.addListener(Events.AfterLayout, new Listener<ContainerEvent>() {

            @Override
            public void handleEvent(ContainerEvent be) {
                map.checkResizeAndCenter();
                if (pendingZoom != null) {
                    zoomToBounds(pendingZoom);
                }
            }
        });
        add(map);
	    layout();
	    
	    onMapInitialized();
	}
	
	protected void onMapInitialized() {
		
	}

    public final void zoomToBounds(LatLngBounds llbounds) {

        int zoomLevel = map.getBoundsZoomLevel(llbounds);

        if (zoomLevel == 0) {
            pendingZoom = llbounds;
        } else {
            map.setCenter(llbounds.getCenter());
            map.setZoomLevel(zoomLevel);
            pendingZoom = null;
        }
    }

    protected final LatLngBounds newLatLngBounds(BoundingBoxDTO bounds) {
        return LatLngBounds.newInstance(
                LatLng.newInstance(bounds.y1, bounds.x1),
                LatLng.newInstance(bounds.y2, bounds.x2));
    }

    protected final BoundingBoxDTO createBounds(LatLngBounds latlngbounds) {
        return new BoundingBoxDTO(latlngbounds.getNorthEast().getLongitude(),
                latlngbounds.getSouthWest().getLatitude(),
                latlngbounds.getSouthWest().getLongitude(),
                latlngbounds.getNorthEast().getLatitude());
    }

    protected final MapWidget getMapWidget() {
    	return map;
    }


}

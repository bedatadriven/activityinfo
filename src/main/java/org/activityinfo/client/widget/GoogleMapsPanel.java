/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.activityinfo.client.widget;


import org.activityinfo.client.i18n.I18N;
import org.activityinfo.client.map.MapApiLoader;
import org.activityinfo.shared.report.content.AiLatLng;
import org.activityinfo.shared.util.mapping.Extents;
import org.activityinfo.shared.util.mapping.Extents;

import org.activityinfo.client.Log;
import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.ContainerEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.Html;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.google.gwt.maps.client.MapWidget;
import com.google.gwt.maps.client.geom.LatLng;
import com.google.gwt.maps.client.geom.LatLngBounds;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * Base class for integrating GoogleMaps widget into a GXT component
 */
public class GoogleMapsPanel extends ContentPanel {
	private MapWidget map = null;

	private LatLngBounds pendingZoom = null;

	private boolean apiLoadFailed;

	public GoogleMapsPanel() {       
		setLayout(new FitLayout());
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
				handleApiLoadFailure();
			}
		});
	}

	/**
	 * Handles the failure of the Google Maps API to load.
	 */
	private void handleApiLoadFailure() {
		apiLoadFailed = true;
		
		if (this.getItemCount() == 0) {
			add(new Html(I18N.CONSTANTS.cannotLoadMap()));

			add(new Button(I18N.CONSTANTS.retry(), new SelectionListener<ButtonEvent>() {
				@Override
				public void componentSelected(ButtonEvent ce) {
					loadMapAsync();
				}
			}));

			layout();
		}
	}

	private void createMapWidget() {
		map = new MapWidget();

		configureMap(map);

		this.addListener(Events.AfterLayout, new Listener<ContainerEvent>() {

			@Override
			public void handleEvent(ContainerEvent be) {
				map.checkResizeAndCenter();
				if (pendingZoom != null) {
					zoomToBounds(pendingZoom);
				}
			}
		});
		removeAll();
		add(map);
		layout();

		onMapInitialized();
	}



	protected void configureMap(MapWidget map) {

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

	public final void zoomToBounds(Extents extents) {
		zoomToBounds(newLatLngBounds(extents));
	}

	protected final LatLng newLatLng(AiLatLng latLng) {
		return LatLng.newInstance(latLng.getLat(), latLng.getLng());
	}

	protected final LatLngBounds newLatLngBounds(Extents extents) {
		return LatLngBounds.newInstance(
				LatLng.newInstance(extents.getMinLat(), extents.getMinLon()),
				LatLng.newInstance(extents.getMaxLat(), extents.getMaxLon()));
	}

	protected final Extents createBounds(LatLngBounds latlngbounds) {
		return new Extents(
				latlngbounds.getSouthWest().getLatitude(),
				latlngbounds.getNorthEast().getLatitude(),
				latlngbounds.getNorthEast().getLongitude(),
				latlngbounds.getSouthWest().getLongitude());
	}

	protected final MapWidget getMapWidget() {
		return map;
	}

	public final boolean isMapLoaded() {
		return map != null;
	}
	
	public final boolean hasMapLoadFailed() {
		return apiLoadFailed;
	}

}

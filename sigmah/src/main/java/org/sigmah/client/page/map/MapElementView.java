/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.client.page.map;

import java.util.ArrayList;
import java.util.List;

import org.sigmah.client.dispatch.Dispatcher;
import org.sigmah.client.dispatch.monitor.MaskingAsyncMonitor;
import org.sigmah.client.i18n.I18N;
import org.sigmah.client.map.GcIconFactory;
import org.sigmah.client.map.IconFactory;
import org.sigmah.client.map.MapApiLoader;
import org.sigmah.client.map.MapTypeFactory;
import org.sigmah.shared.command.GenerateElement;
import org.sigmah.shared.command.GetSitePoints;
import org.sigmah.shared.command.result.SitePointList;
import org.sigmah.shared.dto.SitePointDTO;
import org.sigmah.shared.map.BaseMap;
import org.sigmah.shared.report.content.Content;
import org.sigmah.shared.report.content.MapContent;
import org.sigmah.shared.report.content.MapMarker;
import org.sigmah.shared.report.model.MapElement;
import org.sigmah.shared.report.model.ReportElement;
import org.sigmah.shared.util.mapping.BoundingBoxDTO;
import org.sigmah.shared.util.mapping.Extents;

import com.allen_sauer.gwt.log.client.Log;
import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.Html;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.Status;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.maps.client.MapType;
import com.google.gwt.maps.client.MapWidget;
import com.google.gwt.maps.client.control.Control;
import com.google.gwt.maps.client.control.LargeMapControl;
import com.google.gwt.maps.client.geom.LatLng;
import com.google.gwt.maps.client.geom.LatLngBounds;
import com.google.gwt.maps.client.overlay.Icon;
import com.google.gwt.maps.client.overlay.Marker;
import com.google.gwt.maps.client.overlay.MarkerOptions;
import com.google.gwt.maps.client.overlay.Overlay;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasValue;

/**
 * Displays the content of a MapElement using Google Maps.
 *
 * @author Alex Bertram (akbertram@gmail.com)
 */
class MapElementView extends ContentPanel implements HasValue<MapElement> {
    private MapWidget map = null;
    private String currentBaseMapId = null;
    private LatLngBounds pendingZoom = null;

    /**
     * List of <code>Overlay</code>s that have been added to the map.
     */
    private List<Overlay> overlays = new ArrayList<Overlay>();
    private Status status;

    private MapElement element;
    private MapContent content;

    /**
     * True if the Google Maps API is not loaded AND
     * an attempt to load the API has already failed.
     */
    private boolean apiLoadFailed = false;

    private final Dispatcher dispatcher;
    
    public MapElementView(Dispatcher dispatcher) {

    	this.dispatcher = dispatcher;
    	
        setHeading(I18N.CONSTANTS.preview());
        setLayout(new FitLayout());

        status = new Status();
        ToolBar toolBar = new ToolBar();
        toolBar.add(status);
        setBottomComponent(toolBar);

        // seems like a good time to preload the MapsApi if
        // we haven't already done so

        MapApiLoader.load();

        addListener(Events.AfterLayout, new Listener<BaseEvent>() {
            @Override
            public void handleEvent(BaseEvent be) {
                if (map != null) {
                    map.checkResizeAndCenter();
                }
                if (pendingZoom != null) {
                    Log.debug("MapPreview: zooming to " + map.getBoundsZoomLevel(pendingZoom));
                    map.setCenter(pendingZoom.getCenter(),
                            map.getBoundsZoomLevel(pendingZoom));
                }
            }
        });
    }

    private void zoomToBounds(LatLngBounds bounds) {

        int zoomLevel = map.getBoundsZoomLevel(bounds);
        if (zoomLevel == 0) {
            Log.debug("MapPreview: deferring zoom.");
            pendingZoom = bounds;
        } else {
            Log.debug("MapPreview: zooming to level " + zoomLevel);
            map.setCenter(bounds.getCenter(), zoomLevel);
            pendingZoom = null;
            DeferredCommand.addCommand(new Command() {
				
				@Override
				public void execute() {
					zoomToBounds(pendingZoom);
				}
			});
        }
    }

    private LatLngBounds llBoundsForExtents(Extents extents) {
        return LatLngBounds.newInstance(
                LatLng.newInstance(extents.getMinLat(), extents.getMinLon()),
                LatLng.newInstance(extents.getMaxLat(), extents.getMaxLon()));
    }

    protected LatLngBounds llBoundsForExtents(BoundingBoxDTO bounds) {
        return LatLngBounds.newInstance(
                LatLng.newInstance(bounds.getY1(), bounds.getX1()),
                LatLng.newInstance(bounds.getY2(), bounds.getX2()));	
        }


    public void createMapIfNeededAndUpdateMapContent() {
        if (map == null) {
            MapApiLoader.load(new MaskingAsyncMonitor(this, I18N.CONSTANTS.loadingMap()),
                    new AsyncCallback<Void>() {
                        @Override
                        public void onSuccess(Void result) {

                            apiLoadFailed = false;

                            map = new MapWidget();
                            map.addControl(new LargeMapControl());

                            //changeBaseMapIfNeeded(content.getBaseMap());

                            // clear the error message content
                            removeAll();

                            add(map);

                            updateMapToContent();
                        }

                        @Override
                        public void onFailure(Throwable caught) {
                            handleApiLoadFailure();
                        }
                    });

        } else {
            clearOverlays();
            updateMapToContent();
        }
    }

    private void changeBaseMapIfNeeded(BaseMap baseMap) {
        if (currentBaseMapId == null || !currentBaseMapId.equals(baseMap.getId())) {
            MapType baseMapType = MapTypeFactory.mapTypeForBaseMap(baseMap);
            map.addMapType(baseMapType);
            map.setCurrentMapType(baseMapType);
            map.removeMapType(MapType.getNormalMap());
            map.removeMapType(MapType.getHybridMap());
            currentBaseMapId = baseMap.getId();
        }
    }

    /**
     * Clears all existing content from the map
     */
    private void clearOverlays() {
        for (Overlay overlay : overlays) {
            map.removeOverlay(overlay);
        }
        overlays.clear();
    }


    /**
     * Updates the size of the map and adds Overlays to reflect the content
     * of the current selected indicators
     */
    private void updateMapToContent() {
    	if (element.getLayers().isEmpty())
    	{
    		return;
    	}
    	
    	dispatcher.execute(new GenerateElement<MapContent>(element), null, new AsyncCallback<MapContent>() {

			@Override
			public void onFailure(Throwable caught) {
				MessageBox.alert("Load failed", caught.getMessage(), null);
			}

			@Override
			public void onSuccess(MapContent result) {
		        Log.debug("MapPreview: Received content, extents are = " + result.getExtents().toString());

		        //changeBaseMapIfNeeded(result.getBaseMap());

		        layout();
		
		        // TODO: i18n
		        status.setStatus(result.getUnmappedSites().size() + " " + I18N.CONSTANTS.siteLackCoordiantes(), null);
		
		        GcIconFactory iconFactory = new GcIconFactory();
		        iconFactory.primaryColor = "#0000FF";
		
		        for (MapMarker marker : result.getMarkers()) {
		            Icon icon = IconFactory.createIcon(marker);
		            LatLng latLng = LatLng.newInstance(marker.getLat(), marker.getLng());
		
		            MarkerOptions options = MarkerOptions.newInstance();
		            options.setIcon(icon);
		
		            Marker overlay = new Marker(latLng, options);
		
		            map.addOverlay(overlay);
		            overlays.add(overlay);
		        }
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
                    createMapIfNeededAndUpdateMapContent();
                }
            }));
            layout();
        }
    }

	@Override
	public HandlerRegistration addValueChangeHandler(
			ValueChangeHandler<MapElement> handler) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MapElement getValue() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setValue(MapElement value) {
		this.element=value;
        if (!apiLoadFailed) {
            clearOverlays();
            createMapIfNeededAndUpdateMapContent();
        }
	}

	@Override
	public void setValue(MapElement value, boolean fireEvents) {
		// TODO Auto-generated method stub
		
	}
}

package org.sigmah.client.page.map;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.sigmah.client.map.GcIconFactory;
import org.sigmah.client.map.MapTypeFactory;
import org.sigmah.client.page.common.Shutdownable;
import org.sigmah.shared.command.result.SitePointList;
import org.sigmah.shared.dto.SitePointDTO;
import org.sigmah.shared.util.mapping.BoundingBoxDTO;

import com.ebessette.maps.core.client.overlay.MarkerManagerImpl;
import com.ebessette.maps.core.client.overlay.OverlayManagerOptions;
import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.maps.client.MapType;
import com.google.gwt.maps.client.MapWidget;
import com.google.gwt.maps.client.control.SmallMapControl;
import com.google.gwt.maps.client.event.MapClickHandler;
import com.google.gwt.maps.client.geom.LatLng;
import com.google.gwt.maps.client.geom.LatLngBounds;
import com.google.gwt.maps.client.overlay.Marker;
import com.google.gwt.maps.client.overlay.MarkerOptions;
import com.google.gwt.maps.client.overlay.Overlay;
import com.google.gwt.user.client.Element;

/*
 * A view showing a given list of sites
 */
public class MapViewImpl extends ContentPanel implements Shutdownable, MapView {

	private SimpleEventBus eventBus = new SimpleEventBus();
	
    private MapWidget map = null;
    private LatLngBounds pendingZoom = null;

    // Efficiently handles a large number of markers
    private MarkerManagerImpl markerMgr;

    // Maps markers to site ids
    private Map<Marker, Integer> markerIds;

    // Maps siteIds to markers
    private Map<Integer, Marker> siteMarkers;

    private Marker currentHighlightedMarker;
    private Marker highlitMarker;

	private SitePointList sites;

    public MapViewImpl() {
        setHeaderVisible(false);
    }

    public void selectSite(int siteId) {
        Marker marker = siteMarkers.get(siteId);
        if (marker != null) {

            // we can't change the icon ( I don't think )
            // so we'll bring in a ringer for the selected site

            if (highlitMarker == null) {
                GcIconFactory iconFactory = new GcIconFactory();
                iconFactory.primaryColor = "#0000FF";
                MarkerOptions opts = MarkerOptions.newInstance();
                opts.setIcon(iconFactory.createMarkerIcon());
                highlitMarker = new Marker(marker.getLatLng(), opts);
                map.addOverlay(highlitMarker);
            } else {
                // make sure this marker is on top
                map.removeOverlay(highlitMarker);
                highlitMarker.setLatLng(marker.getLatLng());
                map.addOverlay(highlitMarker);
            }

            if (currentHighlightedMarker != null) {
                currentHighlightedMarker.setVisible(true);
            }
            currentHighlightedMarker = marker;
            currentHighlightedMarker.setVisible(false);

            if (!map.getBounds().containsLatLng(marker.getLatLng())) {
                map.panTo(marker.getLatLng());
            }
        } else {
            // no coords, un highlight existing marker
            if (currentHighlightedMarker != null) {
                currentHighlightedMarker.setVisible(true);
            }
            if (highlitMarker != null) {
                map.removeOverlay(highlitMarker);
                highlitMarker = null;
            }
        }
    }

    public void shutdown() {
    }

    @Override
    protected void onRender(final Element parent, final int pos) {
    	MapViewImpl.super.onRender(parent, pos);
    	if (sites != null) {
            removeAll();

            map = new MapWidget(LatLng.newInstance(sites.getBounds().getCenterY(), sites.getBounds().getCenterX()), 8);

            MapType adminMap = MapTypeFactory.createLocalisationMapType(null);
            map.addMapType(adminMap);
            map.setCurrentMapType(adminMap);
            map.addControl(new SmallMapControl());

            setLayout(new FitLayout());
            add(map);

            map.addMapClickHandler(new MapClickHandler() {
                @Override
                public void onClick(MapClickEvent event) {
                    if (event.getOverlay() != null) {
                        int siteId = siteIdFromOverlay(event.getOverlay());
                        eventBus.fireEvent(new SiteSelectedEvent(siteId));
                    }

                }
            });

            // Listen for when this component is resized/layed out
            // to assure that map widget is properly restated
            Listener<BaseEvent> resizeListener = new Listener<BaseEvent>() {
                @Override
                public void handleEvent(BaseEvent be) {
                    map.checkResizeAndCenter();
                    if (pendingZoom != null) {
                        zoomToBounds(pendingZoom);
                    }
                }
            };

            addListener(Events.AfterLayout, resizeListener);
            addListener(Events.Resize, resizeListener);

            layout();
        }
    }


    private int siteIdFromOverlay(Overlay overlay) {
        if (overlay == highlitMarker) {
            return markerIds.get(currentHighlightedMarker);
        } else {
            return markerIds.get(overlay);
        }
    }

    /**
     * Attempts to pan to the center of the bounds and
     * zoom to the necessary zoom level. If the map widget is not
     * rendered or is in a funk because of resizing, the zoom
     * is deferred until a Resize or AfterLayout event is received.
     *
     * @param bounds
     */
    private void zoomToBounds(LatLngBounds bounds) {

        int zoomLevel = map.getBoundsZoomLevel(bounds);
        if (zoomLevel == 0) {
            pendingZoom = bounds;
        } else {
            map.setCenter(bounds.getCenter(), zoomLevel);
            pendingZoom = null;
        }
    }

    public void addSitesToMap(SitePointList points) {
        if (markerMgr == null) {
            OverlayManagerOptions options = new OverlayManagerOptions();
            options.setMaxZoom(map.getCurrentMapType().getMaximumResolution());

            markerMgr = new MarkerManagerImpl(map, options);
        } else {
            for (Marker marker : markerIds.keySet()) {
                markerMgr.removeMarker(marker);
            }
        }
        markerIds = new HashMap<Marker, Integer>();
        siteMarkers = new HashMap<Integer, Marker>();

        zoomToBounds(llBoundsForBounds(points.getBounds()));

        List<Marker> markers = new ArrayList<Marker>(points.getPoints().size());

        for (SitePointDTO point : points.getPoints()) {

            Marker marker = new Marker(LatLng.newInstance(point.getY(), point.getX()));
            markerIds.put(marker, point.getSiteId());
            siteMarkers.put(point.getSiteId(), marker);
            markers.add(marker);
        }

        markerMgr.addOverlays(markers, 0);
        markerMgr.refresh();

    }

    private LatLngBounds llBoundsForBounds(BoundingBoxDTO bounds) {
        LatLngBounds llbounds = LatLngBounds.newInstance(
                LatLng.newInstance(bounds.getY2(), bounds.getX1()),
                LatLng.newInstance(bounds.getY1(), bounds.getX2()));
        return llbounds;
    }

    public void updateSiteCoords(int siteId, double lat, double lng) {
        Marker marker = siteMarkers.get(siteId);
        if (marker != null) {
            // update existing site
            marker.setLatLng(LatLng.newInstance(lat, lng));
        } else {
            // create new marker
            marker = new Marker(LatLng.newInstance(lat, lng));
            markerIds.put(marker, siteId);
            siteMarkers.put(siteId, marker);
            markerMgr.addOverlay(marker, 0);
        }
    }

	@Override
	public void setSites(SitePointList sites) {
		this.sites=sites;
		
		addSitesToMap(sites);
	}

	@Override
	public HandlerRegistration addSiteSelectedHandler(
			SiteSelectedHandler handler) {
		return eventBus.addHandler(SiteSelectedEvent.TYPE, handler);
	}

}
/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.client.page.entry;

import com.ebessette.maps.core.client.overlay.MarkerManagerImpl;
import com.ebessette.maps.core.client.overlay.OverlayManagerOptions;
import com.extjs.gxt.ui.client.GXT;
import com.extjs.gxt.ui.client.dnd.DropTarget;
import com.extjs.gxt.ui.client.event.*;
import com.extjs.gxt.ui.client.state.StateManager;
import com.extjs.gxt.ui.client.store.Record;
import com.extjs.gxt.ui.client.widget.Component;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.Html;
import com.extjs.gxt.ui.client.widget.layout.CenterLayout;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.menu.Menu;
import com.extjs.gxt.ui.client.widget.menu.MenuItem;
import com.google.gwt.maps.client.MapType;
import com.google.gwt.maps.client.MapWidget;
import com.google.gwt.maps.client.control.SmallMapControl;
import com.google.gwt.maps.client.event.MapClickHandler;
import com.google.gwt.maps.client.event.MapRightClickHandler;
import com.google.gwt.maps.client.geom.LatLng;
import com.google.gwt.maps.client.geom.LatLngBounds;
import com.google.gwt.maps.client.geom.Point;
import com.google.gwt.maps.client.overlay.Marker;
import com.google.gwt.maps.client.overlay.MarkerOptions;
import com.google.gwt.maps.client.overlay.Overlay;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import org.sigmah.client.AppEvents;
import org.sigmah.client.EventBus;
import org.sigmah.client.dispatch.Dispatcher;
import org.sigmah.client.dispatch.monitor.MaskingAsyncMonitor;
import org.sigmah.client.event.SiteEvent;
import org.sigmah.client.i18n.I18N;
import org.sigmah.client.icon.IconImageBundle;
import org.sigmah.client.map.GcIconFactory;
import org.sigmah.client.map.MapApiLoader;
import org.sigmah.client.map.MapTypeFactory;
import org.sigmah.client.page.common.AdminBoundsHelper;
import org.sigmah.client.page.common.Shutdownable;
import org.sigmah.shared.command.GetSitePoints;
import org.sigmah.shared.command.result.SitePointList;
import org.sigmah.shared.dto.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A map panel that serves a counterpart to the SiteGrid, and
 * a drop target for <code>SiteDTO</code>.
 * <p/>
 * Note: this class is not split into presenter-view as nearly all
 * of the logic involves Javascript objects or other GWT entanglements.
 *
 * @author Alex Bertram (akbertram@gmail.com)
 */
public class SiteMap extends ContentPanel implements Shutdownable {

    private final EventBus eventBus;
    private final Dispatcher service;
    private final ActivityDTO activity;

    private MapWidget map = null;
    private LatLngBounds pendingZoom = null;
    private StateManager stateMgr;

    /**
     * Efficiently handles a large number of markers
     */
    private MarkerManagerImpl markerMgr;

    /**
     * Maps markers to site ids
     */
    private Map<Marker, Integer> markerIds;

    /**
     * Maps siteIds to markers
     */
    private Map<Integer, Marker> sites;

    private Listener<SiteEvent> siteListener;

    private Marker currentHighlightedMarker;
    private Marker highlitMarker;

    private Menu contextMenu;

    @Inject
    public SiteMap(EventBus eventBus, Dispatcher service, ActivityDTO activity) {
        this.eventBus = eventBus;
        this.service = service;
        this.activity = activity;

        setHeaderVisible(false);

    }

    private void onSiteChanged(SiteDTO site) {

    }

    private void onSiteCreated(SiteDTO site) {

    }

    private void onSiteSelected(SiteEvent se) {
        if (se.getSource() != this) {
            if (se.getSite() != null && !se.getSite().hasCoords()) {
                BoundingBoxDTO bounds = AdminBoundsHelper.calculate(activity, se.getSite());
                LatLngBounds llBounds = llBoundsForBounds(bounds);

                if (!llBounds.containsBounds(map.getBounds())) {
                    zoomToBounds(llBounds);
                }
            } else {
                highlightSite(se.getSiteId(), true);
            }
        }
    }


    public void shutdown() {
        eventBus.removeListener(AppEvents.SiteSelected, siteListener);
        eventBus.removeListener(AppEvents.SiteCreated, siteListener);
        eventBus.removeListener(AppEvents.SiteChanged, siteListener);
    }

    public void onSiteDropped(Record record, double lat, double lng) {
        record.set("x", lng);
        record.set("y", lat);
        updateSiteCoords(((SiteDTO) record.getModel()).getId(), lat, lng);

    }

    public BoundingBoxDTO getSiteBounds(SiteDTO site) {
        return AdminBoundsHelper.calculate(activity, site);
    }


    @Override
    protected void onRender(final Element parent, final int pos) {
        SiteMap.super.onRender(parent, pos);

        MapApiLoader.load(new MaskingAsyncMonitor(this, I18N.CONSTANTS.loadingComponent()), new AsyncCallback<Void>() {

            @Override
            public void onFailure(Throwable throwable) {
                removeAll();
                setLayout(new CenterLayout());
                add(new Html(I18N.CONSTANTS.connectionProblem()));
                layout();
            }

            @Override
            public void onSuccess(Void result) {
                removeAll();

                CountryDTO country = activity.getDatabase().getCountry();
                BoundingBoxDTO countryBounds = country.getBounds();
                map = new MapWidget(LatLng.newInstance(countryBounds.getCenterY(), countryBounds.getCenterX()), 8);

                MapType adminMap = MapTypeFactory.createLocalisationMapType(country);
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
                            highlightSite(siteId, false);
                            eventBus.fireEvent(new SiteEvent(AppEvents.SiteSelected, SiteMap.this, siteId));
                        }

                    }
                });
                map.addMapRightClickHandler(new MapRightClickHandler() {
                    public void onRightClick(MapRightClickEvent event) {
                        if (event.getOverlay() != null) {
                            showContextMenu(event);
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

                new MapDropTarget(SiteMap.this);

                layout();

                loadSites();
            }
        });
    }

    private void loadSites() {
        service.execute(new GetSitePoints(activity.getId()), null, new AsyncCallback<SitePointList>() {
            @Override
            public void onFailure(Throwable throwable) {

            }

            @Override
            public void onSuccess(SitePointList points) {
                addSitesToMap(points);

                siteListener = new Listener<SiteEvent>() {
                    public void handleEvent(SiteEvent be) {
                        if (be.getType() == AppEvents.SiteSelected) {
                            onSiteSelected(be);
                        } else if (be.getType() == AppEvents.SiteCreated) {
                            onSiteCreated(be.getSite());
                        } else if (be.getType() == AppEvents.SiteChanged) {
                            onSiteChanged(be.getSite());
                        }

                    }
                };

                eventBus.addListener(AppEvents.SiteSelected, siteListener);
                eventBus.addListener(AppEvents.SiteCreated, siteListener);
                eventBus.addListener(AppEvents.SiteChanged, siteListener);
            }
        });
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
        sites = new HashMap<Integer, Marker>();

        zoomToBounds(llBoundsForBounds(points.getBounds()));

        List<Marker> markers = new ArrayList<Marker>(points.getPoints().size());

        for (SitePointDTO point : points.getPoints()) {

            Marker marker = new Marker(LatLng.newInstance(point.getY(), point.getX()));
            markerIds.put(marker, point.getSiteId());
            sites.put(point.getSiteId(), marker);
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
        Marker marker = sites.get(siteId);
        if (marker != null) {
            // update existing site
            marker.setLatLng(LatLng.newInstance(lat, lng));
        } else {
            // create new marker
            marker = new Marker(LatLng.newInstance(lat, lng));
            markerIds.put(marker, siteId);
            sites.put(siteId, marker);
            markerMgr.addOverlay(marker, 0);
        }
    }

    public void highlightSite(int siteId, boolean panTo) {
        Marker marker = sites.get(siteId);
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

    private void showContextMenu(MapRightClickHandler.MapRightClickEvent event) {
        if (contextMenu == null) {

            contextMenu = new Menu();
            contextMenu.add(new MenuItem(I18N.CONSTANTS.showInGrid(),
                    IconImageBundle.ICONS.table(), new SelectionListener<MenuEvent>() {
                        @Override
                        public void componentSelected(MenuEvent ce) {


                        }
                    }));
        }
        Marker marker = (Marker) event.getOverlay();
        contextMenu.show(event.getElement(), "tr");

    }

    private class MapDropTarget extends DropTarget {

        BoundingBoxDTO bounds;
        String boundsName;

        private MapDropTarget(Component target) {
            super(target);
        }

        @Override
        protected void onDragEnter(DNDEvent event) {

            SiteDTO site = getSite(event);
            if (site == null) {
                bounds = null;
            } else {
                bounds = AdminBoundsHelper.calculate(activity, site);
                boundsName = AdminBoundsHelper.name(activity, bounds, site);
            }
            updateDragStatus(event);
        }

        @Override
        protected void onDragMove(DNDEvent event) {
            if (bounds != null) {
                updateDragStatus(event);
            }
        }

        private void updateDragStatus(DNDEvent event) {

            if (bounds == null) {
                // not a site that's being dragged
                event.setCancelled(true);
                event.getStatus().setStatus(false);
            } else {
                LatLng latlng = getLatLng(event);
                if (bounds.contains(latlng.getLongitude(), latlng.getLatitude())) {
                    // a site that's within its bounds
                    event.setCancelled(false);
                    event.getStatus().setStatus(true);
                    event.getStatus().update(GXT.MESSAGES.messageBox_ok());
                } else {
                    // a site that's outside it's bounds
                    event.setCancelled(true);
                    event.getStatus().setStatus(false);
                    event.getStatus().update(I18N.MESSAGES.coordOutsideBounds(boundsName));
                }
            }
        }

        @Override
        protected void onDragDrop(DNDEvent event) {

            if (bounds != null) {
                LatLng latlng = getLatLng(event);
                if (bounds.contains(latlng.getLongitude(), latlng.getLatitude())) {
                    event.setCancelled(false);
                    onSiteDropped((Record) event.getData(), latlng.getLatitude(), latlng.getLongitude());
                }
            }
        }

        private LatLng getLatLng(DNDEvent event) {
            int x = event.getClientX() - map.getElement().getAbsoluteLeft();
            int y = event.getClientY() - map.getElement().getAbsoluteTop();
            return map.convertContainerPixelToLatLng(Point.newInstance(x, y));
        }

        private SiteDTO getSite(DNDEvent event) {
            if (event.getData() instanceof Record) {
                Record record = (Record) event.getData();
                if (record.getModel() instanceof SiteDTO) {
                    return (SiteDTO) record.getModel();
                }
            }
            return null;
        }

    }

}

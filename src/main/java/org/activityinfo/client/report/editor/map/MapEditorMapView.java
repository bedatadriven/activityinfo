package org.activityinfo.client.report.editor.map;

/*
 * #%L
 * ActivityInfo Server
 * %%
 * Copyright (C) 2009 - 2013 UNICEF
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the 
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public 
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */

import org.activityinfo.client.EventBus;
import org.activityinfo.client.Log;
import org.activityinfo.client.dispatch.Dispatcher;
import org.activityinfo.client.dispatch.monitor.MaskingAsyncMonitor;
import org.activityinfo.client.i18n.I18N;
import org.activityinfo.client.map.GoogleMapsReportOverlays;
import org.activityinfo.client.page.report.HasReportElement;
import org.activityinfo.client.page.report.ReportChangeHandler;
import org.activityinfo.client.page.report.ReportEventHelper;
import org.activityinfo.client.widget.GoogleMapsPanel;
import org.activityinfo.shared.command.GenerateElement;
import org.activityinfo.shared.map.BaseMap;
import org.activityinfo.shared.report.content.AdminOverlay;
import org.activityinfo.shared.report.content.AiLatLng;
import org.activityinfo.shared.report.content.MapContent;
import org.activityinfo.shared.report.content.MapMarker;
import org.activityinfo.shared.report.model.MapReportElement;

import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.Status;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.google.gwt.maps.client.InfoWindowContent;
import com.google.gwt.maps.client.MapWidget;
import com.google.gwt.maps.client.control.ControlAnchor;
import com.google.gwt.maps.client.control.ControlPosition;
import com.google.gwt.maps.client.control.LargeMapControl;
import com.google.gwt.maps.client.event.MapClickHandler;
import com.google.gwt.maps.client.event.MapClickHandler.MapClickEvent;
import com.google.gwt.maps.client.event.MapMoveEndHandler;
import com.google.gwt.maps.client.event.MapZoomEndHandler;
import com.google.gwt.maps.client.geom.LatLng;
import com.google.gwt.maps.client.overlay.Marker;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * Displays the content of a MapElement using Google Maps.
 */
public class MapEditorMapView extends GoogleMapsPanel implements
    HasReportElement<MapReportElement> {

    private static final int DEFAULT_ZOOM_CONTROL_OFFSET_X = 5;
    private static final int ZOOM_CONTROL_OFFSET_Y = 5;

    private static final int RDC_CENTER_LONG = 25;
    private static final int RDC_CENTER_LAT = -1;

    private final Dispatcher dispatcher;
    private final ReportEventHelper events;

    private BaseMap currentBaseMap = null;

    private final Status statusWidget;

    private MapReportElement model = new MapReportElement();
    private GoogleMapsReportOverlays overlays;

    // Model of a the map
    private MapContent content;

    private boolean zoomSet = false;

    // True when the first layer is just put on the map
    private boolean isFirstLayerUpdate = true;

    private LargeMapControl zoomControl;
    private int zoomControlOffsetX = DEFAULT_ZOOM_CONTROL_OFFSET_X;

    public MapEditorMapView(Dispatcher dispatcher, EventBus eventBus) {
        this.dispatcher = dispatcher;
        this.events = new ReportEventHelper(eventBus, this);
        this.events.listen(new ReportChangeHandler() {

            @Override
            public void onChanged() {
                loadContent();
            }
        });

        setHeaderVisible(false);

        statusWidget = new Status();
        ToolBar toolBar = new ToolBar();
        toolBar.add(statusWidget);
        setBottomComponent(toolBar);
    }

    @Override
    protected void configureMap(MapWidget map) {
        zoomControl = new LargeMapControl();
        map.addControl(zoomControl, zoomControlPosition());

        // TODO: generalize
        map.panTo(LatLng.newInstance(RDC_CENTER_LAT, RDC_CENTER_LONG));

        map.addMapClickHandler(new MapClickHandler() {
            @Override
            public void onClick(MapClickEvent event) {
                onMapClick(event);
            }
        });

        map.addMapZoomEndHandler(new MapZoomEndHandler() {

            @Override
            public void onZoomEnd(MapZoomEndEvent event) {
                updateModelFromMap();
            }
        });

        map.addMapMoveEndHandler(new MapMoveEndHandler() {

            @Override
            public void onMoveEnd(MapMoveEndEvent event) {
                updateModelFromMap();
            }
        });
    }

    @Override
    protected void onMapInitialized() {

        // initialize our overlay manager
        this.overlays = new GoogleMapsReportOverlays(getMapWidget());

        loadContent();
    }

    public void setZoomControlOffsetX(int offset) {
        zoomControlOffsetX = offset;
        try {
            if (zoomControl != null) {
                getMapWidget().removeControl(zoomControl);
                getMapWidget().addControl(zoomControl, zoomControlPosition());
            }
        } catch (Exception e) {
            Log.error("Exception thrown while setting zoom control", e);
        }
    }

    @Override
    public void bind(MapReportElement model) {
        this.model = model;
        loadContent();
    }

    @Override
    public void disconnect() {
        events.disconnect();
    }

    @Override
    public MapReportElement getModel() {
        return model;
    }

    private ControlPosition zoomControlPosition() {
        return new ControlPosition(ControlAnchor.TOP_LEFT, zoomControlOffsetX,
            ZOOM_CONTROL_OFFSET_Y);
    }

    /**
     * Updates the size of the map and adds Overlays to reflect the content of
     * the current selected indicators
     */
    private void loadContent() {

        if (!isMapLoaded()) {
            return;
        }

        overlays.clear();

        // Don't update when no layers are present
        if (model.getLayers().isEmpty()) {
            isFirstLayerUpdate = true;
            return;
        }
        // Prevent setting the extents for the MapWidget when more then 1 layer
        // is added
        if (isFirstLayerUpdate &&
            model.getLayers().size() > 0) {
            isFirstLayerUpdate = false;
        }

        dispatcher.execute(new GenerateElement<MapContent>(model),
            new MaskingAsyncMonitor(this, I18N.CONSTANTS.loadingMap()),
            new AsyncCallback<MapContent>() {
                @Override
                public void onFailure(Throwable caught) {
                    MessageBox.alert("Load failed", caught.getMessage(), null);
                }

                @Override
                public void onSuccess(MapContent result) {
                    onContentLoaded(result);
                }
            });
    }

    private void onContentLoaded(MapContent result) {
        Log.debug("MapPreview: Received content");

        content = result;

        statusWidget.setStatus(result.getUnmappedSites().size() + " "
            + I18N.CONSTANTS.siteLackCoordiantes(), null);

        overlays.setBaseMap(result.getBaseMap());
        overlays.addMarkers(result.getMarkers());
        for (AdminOverlay overlay : result.getAdminOverlays()) {
            overlays.addAdminLayer(overlay);
        }

        if (!zoomSet) {
            zoomToBounds(result.getExtents());
            zoomSet = true;
        }

    }

    private void onMapClick(MapClickEvent event) {
        if (event.getOverlay() != null) {
            MapMarker marker = overlays.getMapMarker(event.getOverlay());
            if (marker != null) {
                getMapWidget().getInfoWindow().open(
                    (Marker) event.getOverlay(),
                    new InfoWindowContent(I18N.CONSTANTS.loading()));
            }
        }
    }

    private void updateModelFromMap() {
        model.setZoomLevel(getMapWidget().getZoomLevel());
        model.setCenter(new AiLatLng(
            getMapWidget().getCenter().getLatitude(),
            getMapWidget().getCenter().getLongitude()));
    }
}

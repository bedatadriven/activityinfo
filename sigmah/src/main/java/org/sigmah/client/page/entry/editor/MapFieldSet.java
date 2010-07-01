/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.client.page.entry.editor;


import com.extjs.gxt.ui.client.event.ContainerEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.FieldEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.form.FieldSet;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.toolbar.LabelToolItem;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.google.gwt.maps.client.MapType;
import com.google.gwt.maps.client.MapWidget;
import com.google.gwt.maps.client.control.SmallMapControl;
import com.google.gwt.maps.client.event.MapMoveEndHandler;
import com.google.gwt.maps.client.event.MapZoomEndHandler;
import com.google.gwt.maps.client.event.MarkerDragEndHandler;
import com.google.gwt.maps.client.geom.LatLng;
import com.google.gwt.maps.client.geom.LatLngBounds;
import com.google.gwt.maps.client.overlay.Marker;
import com.google.gwt.maps.client.overlay.MarkerOptions;
import org.sigmah.client.i18n.I18N;
import org.sigmah.client.map.MapTypeFactory;
import org.sigmah.client.page.common.widget.CoordinateField;
import org.sigmah.client.page.config.form.FieldSetFitLayout;
import org.sigmah.shared.dto.BoundingBoxDTO;
import org.sigmah.shared.dto.CountryDTO;

public class MapFieldSet extends FieldSet implements MapPresenter.View {


    private ContentPanel panel;
    private MapWidget map = null;
    private Marker marker = null;

    private CoordinateField latField;
    private CoordinateField lngField;

    private LatLngBounds pendingZoom = null;

    private MapPresenter presenter;
    private final CountryDTO country;

    public MapFieldSet(CountryDTO country) {
        this.country = country;
    }

    public void init(final MapPresenter presenter) {

        this.presenter = presenter;

        setHeading(I18N.CONSTANTS.geoPosition());
        setLayout(new FieldSetFitLayout());
        setHeight(250);

        /*
           * Create the content panel that houses
           * the map and the coordinate fields
           */

        panel = new ContentPanel();
        panel.setHeaderVisible(false);
        panel.setLayout(new FitLayout());

        /*
           * The bottom ToolBar records the latitude and longitude
           *
           */

        Listener<FieldEvent> latLngListener = new Listener<FieldEvent>() {
            public void handleEvent(FieldEvent be) {
                presenter.onCoordsChanged(latField.getValue(), lngField.getValue());
            }
        };

        /* Create the Lat/Lng entry fields */

        latField = new CoordinateField(CoordinateField.LATITUDE);
        latField.setName("y");
        latField.setFireChangeEventOnSetValue(true);

        lngField = new CoordinateField(CoordinateField.LONGITUDE);
        lngField.setName("x");
        lngField.setFireChangeEventOnSetValue(true);

        latField.addListener(Events.Change, latLngListener);
        lngField.addListener(Events.Change, latLngListener);

        ToolBar coordBar = new ToolBar();
        coordBar.add(new LabelToolItem(I18N.CONSTANTS.lat()));
        coordBar.add(latField);
        coordBar.add(new LabelToolItem(I18N.CONSTANTS.lng()));
        coordBar.add(lngField);

        panel.setBottomComponent(coordBar);

        /* Create the map itself */

        map = new MapWidget();
        panel.add(map);

        map.addControl(new SmallMapControl());
        map.setCenter(LatLng.newInstance(
                country.getBounds().getCenterY(),
                country.getBounds().getCenterX()));
        map.setZoomLevel(6);

        MapType adminMap = MapTypeFactory.createLocalisationMapType(country);
        map.addMapType(adminMap);
        map.setCurrentMapType(adminMap);

        map.addMapZoomEndHandler(new MapZoomEndHandler() {
            public void onZoomEnd(MapZoomEndEvent event) {
                presenter.onMapViewChanged(createBounds(map.getBounds()));
            }
        });

        map.addMapMoveEndHandler(new MapMoveEndHandler() {
            @Override
            public void onMoveEnd(MapMoveEndEvent mapMoveEndEvent) {
                presenter.onMapViewChanged(createBounds(map.getBounds()));
            }
        });

        this.addListener(Events.AfterLayout, new Listener<ContainerEvent>() {

            @Override
            public void handleEvent(ContainerEvent be) {

                map.checkResizeAndCenter();

                if (pendingZoom != null) {
                    zoomToBounds(pendingZoom);
                }
            }
        });

        add(panel);
    }

    @Override
    public BoundingBoxDTO getMapView() {
        return createBounds(map.getBounds());
    }

    @Override
    public void panTo(double lat, double lng) {
        LatLng latlng = LatLng.newInstance(lat, lng);
        map.panTo(latlng);
    }

    public CoordinateField getLatField() {
        return latField;
    }

    public CoordinateField getLngField() {
        return lngField;
    }


    @Override
    public void setCoords(Double lat, Double lng) {
        latField.setValue(lat);
        lngField.setValue(lng);
    }

    @Override
    public void setBounds(String name, BoundingBoxDTO bounds) {

        latField.setBounds(name, bounds.y1, bounds.y2);
        lngField.setBounds(name, bounds.x1, bounds.x2);

        latField.validate();
        lngField.validate();
    }

    @Override
    public Double getX() {
        return lngField.getValue();
    }

    @Override
    public Double getY() {
        return latField.getValue();
    }

    @Override
    public void setMapView(BoundingBoxDTO bounds) {
        zoomToBounds(createLatLngBounds(bounds));
    }

    @Override
    public void setMarkerPos(double lat, double lng) {
        LatLng latlng = LatLng.newInstance(lat, lng);
        if (marker == null) {
            createMarker(latlng);
        } else {
            marker.setLatLng(latlng);
        }
    }

    public void zoomToBounds(LatLngBounds llbounds) {

        int zoomLevel = map.getBoundsZoomLevel(llbounds);

        if (zoomLevel == 0) {
            pendingZoom = llbounds;
        } else {
            map.setCenter(llbounds.getCenter());
            map.setZoomLevel(zoomLevel);
            pendingZoom = null;
        }
    }

    private void createMarker(LatLng latlng) {
        MarkerOptions options = MarkerOptions.newInstance();
        options.setDraggable(true);

        marker = new Marker(latlng, options);

        marker.addMarkerDragEndHandler(new MarkerDragEndHandler() {
            public void onDragEnd(MarkerDragEndEvent event) {

                LatLng latlng = marker.getLatLng();
                presenter.onMarkerMoved(latlng.getLatitude(), latlng.getLongitude());
            }
        });

        map.addOverlay(marker);
    }


    private static LatLngBounds createLatLngBounds(BoundingBoxDTO bounds) {
        return LatLngBounds.newInstance(
                LatLng.newInstance(bounds.y1, bounds.x1),
                LatLng.newInstance(bounds.y2, bounds.x2));
    }

    private BoundingBoxDTO createBounds(LatLngBounds latlngbounds) {
        return new BoundingBoxDTO(latlngbounds.getNorthEast().getLongitude(),
                latlngbounds.getSouthWest().getLatitude(),
                latlngbounds.getSouthWest().getLongitude(),
                latlngbounds.getNorthEast().getLatitude());

    }

}

package org.activityinfo.clientjre.place.entry.editor.mock;

import org.activityinfo.client.page.entry.editor.MapPresenter;
import org.activityinfo.shared.dto.Bounds;

/**
 * @author Alex Bertram (akbertram@gmail.com)
 */
public class MockMapView implements MapPresenter.View {

    public String boundsName;
    public Bounds bounds;
    public Double lat;
    public Double lng;
    public Bounds mapView;

    public Double markerX;
    public Double markerY;

    @Override
    public void init(MapPresenter presenter) {

    }

    @Override
    public void setBounds(String name, Bounds bounds) {
        this.boundsName = name;
        this.bounds = bounds;
    }

    @Override
    public void setCoords(Double lat, Double lng) {
        this.lat = lat;
        this.lng = lng;
    }

    @Override
    public Double getX() {
        return lng;
    }

    @Override
    public Double getY() {
        return lat;
    }

    @Override
    public void setMarkerPos(double lat, double lng) {
        markerY = lat;
        markerX = lng;
    }

    @Override
    public void setMapView(Bounds bounds) {
        this.mapView = bounds;
    }

    @Override
    public Bounds getMapView() {
        return mapView;
    }

    @Override
    public void panTo(double lat, double lng) {

    }
}

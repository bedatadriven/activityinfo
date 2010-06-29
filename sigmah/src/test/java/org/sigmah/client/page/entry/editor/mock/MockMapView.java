package org.sigmah.client.page.entry.editor.mock;

import org.sigmah.client.page.entry.editor.MapPresenter;
import org.sigmah.shared.dto.BoundingBoxDTO;

/**
 * @author Alex Bertram (akbertram@gmail.com)
 */
public class MockMapView implements MapPresenter.View {

    public String boundsName;
    public BoundingBoxDTO bounds;
    public Double lat;
    public Double lng;
    public BoundingBoxDTO mapView;

    public Double markerX;
    public Double markerY;

    @Override
    public void init(MapPresenter presenter) {

    }

    @Override
    public void setBounds(String name, BoundingBoxDTO bounds) {
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
    public void setMapView(BoundingBoxDTO bounds) {
        this.mapView = bounds;
    }

    @Override
    public BoundingBoxDTO getMapView() {
        return mapView;
    }

    @Override
    public void panTo(double lat, double lng) {

    }
}

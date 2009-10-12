package org.activityinfo.client.page.entry.editor;

import org.activityinfo.client.Application;
import org.activityinfo.client.page.common.widget.CoordinateField;
import org.activityinfo.shared.dto.Bounds;

/**
 * @author Alex Bertram (akbertram@gmail.com)
 */
public class CoordFieldSet extends AbstractFieldSet implements MapPresenter.View {

    MapPresenter presenter;
    protected CoordinateField latField;
    protected CoordinateField lngField;

    public CoordFieldSet() {

        super(Application.CONSTANTS.geoPosition(), 100, 200);

    }

    public void init(MapPresenter presenter) {
        this.presenter = presenter;
        
        latField = new CoordinateField(CoordinateField.LATITUDE);
        latField.setFieldLabel(Application.CONSTANTS.latitude());
        latField.setName("y");
        add(latField);

        lngField = new CoordinateField(CoordinateField.LONGITUDE);
        lngField.setFieldLabel(Application.CONSTANTS.longitude());
        lngField.setName("x");
        add(lngField);
    }


    @Override
    public void setBounds(String name, Bounds bounds) {
        latField.setBounds(name, bounds.getY1(), bounds.getY2());
        lngField.setBounds(name, bounds.getX1(), bounds.getX2());
    }

    @Override
    public void setCoords(Double lat, Double lng) {
        latField.setValue(lat);
        latField.validate();

        lngField.setValue(lng);
        lngField.validate();
    }


    @Override
    public Double getX() {
        return latField.getValue();
    }

    @Override
    public Double getY() {
        return lngField.getValue();
    }

    @Override
    public Bounds getMapView() {
        return new Bounds(-180, -90, 180, 90);
    }

    @Override
    public void setMarkerPos(double lat, double lng) {
        // noop
    }

    @Override
    public void setMapView(Bounds bounds) {
        // nooop
    }

    @Override
    public void panTo(double lat, double lng) {
        // noop
    }


}

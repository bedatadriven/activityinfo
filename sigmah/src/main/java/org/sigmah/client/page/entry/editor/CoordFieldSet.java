/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.client.page.entry.editor;

import org.sigmah.client.i18n.I18N;
import org.sigmah.client.page.common.widget.CoordinateField;
import org.sigmah.shared.dto.BoundingBoxDTO;

/**
 * @author Alex Bertram (akbertram@gmail.com)
 */
public class CoordFieldSet extends AbstractFieldSet implements MapPresenter.View {

    MapPresenter presenter;
    protected CoordinateField latField;
    protected CoordinateField lngField;

    public CoordFieldSet() {

        super(I18N.CONSTANTS.geoPosition(), 100, 200);

    }

    public void init(MapPresenter presenter) {
        this.presenter = presenter;
        
        latField = new CoordinateField(CoordinateField.Axis.LATITUDE);
        latField.setFieldLabel(I18N.CONSTANTS.latitude());
        latField.setName("y");
        add(latField);

        lngField = new CoordinateField(CoordinateField.Axis.LONGITUDE);
        lngField.setFieldLabel(I18N.CONSTANTS.longitude());
        lngField.setName("x");
        add(lngField);
    }


    @Override
    public void setBounds(String name, BoundingBoxDTO bounds) {
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
    public BoundingBoxDTO getMapView() {
        return new BoundingBoxDTO(-180, -90, 180, 90);
    }

    @Override
    public void setMarkerPos(double lat, double lng) {
        // noop
    }

    @Override
    public void setMapView(BoundingBoxDTO bounds) {
        // nooop
    }

    @Override
    public void panTo(double lat, double lng) {
        // noop
    }


}

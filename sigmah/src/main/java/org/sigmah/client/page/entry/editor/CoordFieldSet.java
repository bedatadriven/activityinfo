/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.client.page.entry.editor;

import org.sigmah.client.dispatch.AsyncMonitor;
import org.sigmah.client.i18n.I18N;
import org.sigmah.client.page.common.widget.CoordinateField;
import org.sigmah.shared.report.content.AiLatLng;
import org.sigmah.shared.util.mapping.BoundingBoxDTO;

import com.google.gwt.event.shared.HandlerRegistration;

/**
 * @author Alex Bertram (akbertram@gmail.com)
 */
public class CoordFieldSet extends AbstractFieldSet implements MapEditView {

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
    public void setEditBounds(String name, BoundingBoxDTO bounds) {
        latField.setBounds(name, bounds.getY1(), bounds.getY2());
        lngField.setBounds(name, bounds.getX1(), bounds.getX2());
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
    public void setViewBounds(BoundingBoxDTO bounds) {
        // nooop
    }

	@Override
	public HandlerRegistration addMarkerMovedHandler(MarkerMovedHandler handler) {
		return null;
	}

	/*
	 * No events supported on this View
	 * @see org.sigmah.client.page.entry.editor.MapPresenter.View#addCoordinatesChangedHandler(org.sigmah.client.page.entry.editor.MapPresenter.View.CoordinatesChangedHandler)
	 */
	@Override
	public HandlerRegistration addCoordinatesChangedHandler(
			CoordinatesChangedHandler handler) {
		return null;
	}

	@Override
	public HandlerRegistration addMapViewChangedHandler(
			MapViewChangedHandler handler) {
		return null;
	}

	@Override
	public void initialize() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public AsyncMonitor getAsyncMonitor() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setValue(AiLatLng value) {
        latField.setValue(value.getLat());
        latField.validate();

        lngField.setValue(value.getLng());
        lngField.validate();
	}

	@Override
	public AiLatLng getValue() {
		return new AiLatLng(latField.getValue(), lngField.getValue());
	}

	@Override
	public void setMarkerPosition(AiLatLng latLng) {
	}

	@Override
	public void panTo(AiLatLng latLng) {
	}

	@Override
	public BoundingBoxDTO getViewBounds() {
        return new BoundingBoxDTO(-180, -90, 180, 90);
	}
}

/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.client.page.entry.editor;

import org.sigmah.shared.dto.SiteDTO;
import org.sigmah.shared.report.content.AiLatLng;
import org.sigmah.shared.util.mapping.BoundingBoxDTO;

/*
 * UI logic for managing a MapView where the user is allowed to change a location
 * by dragging a marker over a map
 */
public class MapPresenter 
	implements 
		MapEditView.CoordinatesChangedHandler, 
		MapEditView.MarkerMovedHandler {

    private BoundingBoxDTO bounds;
    private MapEditView view;

    public MapPresenter(MapEditView view) {
        this.view = view;
        
        addHandlers();
        
        this.bounds = new BoundingBoxDTO(-180, -90, 180, 90);
    }

    private void addHandlers() {
    	view.addCoordinatesChangedHandler(this);
    	view.addMarkerMovedHandler(this);
	}

	public void setSite(SiteDTO site, String name, BoundingBoxDTO bounds) {
        setBounds(name, bounds);
    }
	
    public void setBounds(String name, BoundingBoxDTO bounds) {
        this.bounds = bounds;
        view.setEditBounds(name, bounds);
        view.setViewBounds(bounds);

        if(!haveValidCoords()) {
            view.setMarkerPosition(new AiLatLng(bounds.getCenterY(), bounds.getCenterX()));
        }
    }

    private boolean haveValidCoords() {
        Double x = view.getX();
        Double y = view.getY();

        return x!=null && y!=null && bounds.contains(x, y);
    }

	@Override
	public void onCoordinatesChanged(MapEditView.CoordinatesChangedEvent event) {
		double x = event.getX();
		double y = event.getY();
		
        if(haveValidCoords()) {
            view.setValue(new AiLatLng(y, x));

            if(!view.getViewBounds().contains(x, y)) {
                view.panTo(new AiLatLng(y, x));
            }
        }
	}

	@Override
	public void onMarkedMoved(MapEditView.MarkerMovedEvent event) {
		double my = event.getMy();
		double mx = event.getMx();
		
        if(!bounds.contains(mx, my)) {
            double clampedY = bounds.clampY(my);
            double clampedX = bounds.clampX(mx);
            view.setValue(new AiLatLng(clampedY, clampedX));
        } else {
            view.setValue(new AiLatLng(my, mx));
        }
	}
}

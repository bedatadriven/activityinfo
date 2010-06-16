package org.activityinfo.client.page.entry.editor;

import org.activityinfo.shared.dto.BoundingBoxDTO;
import org.activityinfo.shared.dto.SiteDTO;

/**
 * @author Alex Bertram (akbertram@gmail.com)
 */
public class MapPresenter {


    public interface View {

        public void init(MapPresenter presenter);

        public void setBounds(String name, BoundingBoxDTO bounds);

        public void setCoords(Double lat, Double lng);

        public Double getX();

        public Double getY();

        public void setMarkerPos(double lat, double lng);

        public void setMapView(BoundingBoxDTO bounds);

        public BoundingBoxDTO getMapView();

        public void panTo(double lat, double lng);
    }

    private BoundingBoxDTO bounds;
    private View view;

    public MapPresenter(View view) {
        this.view = view;
        this.view.init(this);
        this.bounds = new BoundingBoxDTO(-180, -90, 180, 90);
    }

    public void setSite(SiteDTO site, String name, BoundingBoxDTO bounds) {

        setBounds(name, bounds);

    }

    public void setBounds(String name, BoundingBoxDTO bounds) {
        this.bounds = bounds;
        view.setBounds(name, bounds);
        view.setMapView(bounds);

        if(!haveValidCoords()) {
            view.setMarkerPos(bounds.getCenterY(), bounds.getCenterX());
        }
    }



    private boolean haveValidCoords() {
        Double x = view.getX();
        Double y = view.getY();

        return x!=null && y!=null && bounds.contains(x, y);
    }

    public void onMarkerMoved(double my, double mx) {

        if(!bounds.contains(mx, my)) {
            double clampedY = bounds.clampY(my);
            double clampedX = bounds.clampX(mx);
            view.setMarkerPos(clampedY, clampedX);
            view.setCoords(clampedY, clampedX);
        } else {
            view.setCoords(my, mx);
        }
    }

    public void onCoordsChanged(Double y, Double x) {
        if(haveValidCoords()) {
            view.setMarkerPos(y, x);

            if(!view.getMapView().contains(x, y)) {
                view.panTo(y, x);
            }
        }
    }

    public void onMapViewChanged(BoundingBoxDTO newBounds) {

    }

}

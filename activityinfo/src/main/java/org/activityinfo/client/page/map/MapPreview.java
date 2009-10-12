package org.activityinfo.client.page.map;

import com.extjs.gxt.ui.client.Style;
import com.extjs.gxt.ui.client.state.StateManager;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.Status;
import com.extjs.gxt.ui.client.widget.layout.FlowLayout;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.google.gwt.maps.client.MapType;
import com.google.gwt.maps.client.MapWidget;
import com.google.gwt.maps.client.geom.LatLng;
import com.google.gwt.maps.client.geom.LatLngBounds;
import com.google.gwt.maps.client.overlay.Icon;
import com.google.gwt.maps.client.overlay.Marker;
import com.google.gwt.maps.client.overlay.MarkerOptions;
import com.google.gwt.maps.client.overlay.Overlay;
import org.activityinfo.client.Application;
import org.activityinfo.client.map.GcIconFactory;
import org.activityinfo.client.map.MapTypeFactory;
import org.activityinfo.client.map.IconFactory;
import org.activityinfo.shared.map.BaseMap;
import org.activityinfo.shared.report.content.Content;
import org.activityinfo.shared.report.content.Extents;
import org.activityinfo.shared.report.content.MapContent;
import org.activityinfo.shared.report.content.MapMarker;
import org.activityinfo.shared.report.model.MapElement;
import org.activityinfo.shared.report.model.ReportElement;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Alex Bertram (akbertram@gmail.com)
 */
public class MapPreview extends ContentPanel {

    private MapPresenter presenter;
    private MapWidget map = null;
    private LatLngBounds pendingZoom = null;
    private StateManager stateMgr;
    private List<Overlay> overlays = new ArrayList<Overlay>();
    private ToolBar statusBar;
    private Status status;


    public MapPreview() {

        setHeading(Application.CONSTANTS.preview());
        setLayout(new FlowLayout());
        setScrollMode(Style.Scroll.AUTO);

        status = new Status();
        statusBar = new ToolBar();
        statusBar.add(status);
        setBottomComponent(statusBar);
    }

    private void createMap(BaseMap baseMap) {
        map = new MapWidget();

        MapType baseMapType = MapTypeFactory.createBaseMapType(baseMap);
        map.addMapType(baseMapType);
        map.setCurrentMapType(baseMapType);
        map.removeMapType(MapType.getNormalMap());
        map.removeMapType(MapType.getHybridMap());

        map.setDraggable(false);

        add(map);
    }

    private void zoomToBounds(LatLngBounds bounds) {

        int zoomLevel = map.getBoundsZoomLevel(bounds);
        if(zoomLevel == 0) {
            pendingZoom = bounds;
        } else {
            map.setCenter(bounds.getCenter(), zoomLevel);
        }
    }

//    @Override
//    public void init(final MapPresenter presenter) {
//        this.presenter = presenter;
//
//
//        DelayedTask task = new DelayedTask(new Listener() {
//            @Override
//            public void handleEvent(BaseEvent be) {
//                //presenter.onMapLoaded(map.getZoomLevel(), map.getBounds());
//
//            }
//        });
//        task.delay(1);
//    }


    public void setContent(ReportElement element, Content content) {

        clearOverlays();

        if(content instanceof MapContent) {
            render((MapElement)element, (MapContent) content);
        }
    }

    private LatLngBounds llBoundsForExtents(Extents extents) {
        return LatLngBounds.newInstance(
            LatLng.newInstance(extents.getMaxLat(), extents.getMinLon()),
            LatLng.newInstance(extents.getMinLat(), extents.getMaxLon()));
    }

    private void clearOverlays() {
        for(Overlay overlay : overlays) {
            map.removeOverlay(overlay);
        }
        overlays.clear();
    }

    public void render(MapElement element, MapContent content) {

        if(map == null) {
            createMap(content.getBaseMap());
        } else {
            if( map.getCurrentMapType().equals(content.getBaseMap())) {
                        
            }
        }

        map.setWidth(element.getWidth()+ "px");
        map.setHeight(element.getHeight() + "px");

        layout();
    
        map.checkResizeAndCenter();


        zoomToBounds(llBoundsForExtents(content.getExtents()));

        status.setStatus(content.getUnmappedSites().size() + " site(s) ont manqué des coordinées géographique", null);

        GcIconFactory iconFactory = new GcIconFactory();
        iconFactory.primaryColor = "#0000FF";

        for(MapMarker marker : content.getMarkers()) {

            Icon icon;
            if(marker.getIcon() != null) {
                icon = IconFactory.createIcon(marker.getIcon());
            } else {
                iconFactory.width = marker.getRadius() * 2;
                iconFactory.height = marker.getRadius() * 2;
                iconFactory.primaryColor = "#" + Integer.toHexString(marker.getColor());

                icon = IconFactory.createBubble(marker.getColor(), marker.getRadius());
            }

            LatLng latLng = LatLng.newInstance(marker.getLat(), marker.getLng());

            MarkerOptions options = MarkerOptions.newInstance();
            options.setIcon(icon);

            Marker overlay = new Marker(latLng, options);

            map.addOverlay(overlay);
            overlays.add(overlay);

        }

    }
}
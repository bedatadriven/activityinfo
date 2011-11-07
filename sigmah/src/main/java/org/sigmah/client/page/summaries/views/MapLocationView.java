package org.sigmah.client.page.summaries.views;

import org.sigmah.client.dispatch.AsyncMonitor;
import org.sigmah.client.i18n.I18N;
import org.sigmah.client.map.MapTypeFactory;
import org.sigmah.client.page.entry.form.MapView;
import org.sigmah.shared.dto.CountryDTO;
import org.sigmah.shared.report.content.AiLatLng;
import org.sigmah.shared.util.mapping.BoundingBoxDTO;

import com.extjs.gxt.ui.client.event.ContainerEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.form.LabelField;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.toolbar.LabelToolItem;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.google.gwt.maps.client.MapType;
import com.google.gwt.maps.client.MapWidget;
import com.google.gwt.maps.client.control.SmallMapControl;
import com.google.gwt.maps.client.geom.LatLngBounds;
import com.google.gwt.maps.client.overlay.Marker;
import com.google.gwt.maps.client.overlay.MarkerOptions;

/*
 * Displays a read-only view of a location somehwere on the world
 */
public class MapLocationView extends LayoutContainer implements MapView {
    private ContentPanel panel;
    private MapWidget map = null;
    private Marker marker = null;
    private AiLatLng latLng;

    private LabelField latField;
    private LabelField lngField;

    private LatLngBounds pendingZoom = null;

    private final CountryDTO country;

    public MapLocationView(final CountryDTO country) {
        this.country = country;
        
        initializeComponent();

        createPanel();
        createPanelToolbar();
        createMapWidget();
    }

    private void initializeComponent() {
	}

	public void init() {
    }

	private void createMapWidget() {
		map = new MapWidget();

        map.addControl(new SmallMapControl());
        map.setZoomLevel(6);

        MapType adminMap = MapTypeFactory.createLocalisationMapType(country);
        map.addMapType(adminMap);
        map.setCurrentMapType(adminMap);
        map.setDraggable(false);
        map.setPinchToZoom(false);

        this.addListener(Events.AfterLayout, new Listener<ContainerEvent>() {
            @Override
            public void handleEvent(ContainerEvent be) {
                map.checkResizeAndCenter();
                if (pendingZoom != null) {
                    zoomToBounds(pendingZoom);
                }
            }
        });
        panel.add(map);
	}

	private void createPanelToolbar() {
		latField = new LabelField();
        latField.setName("y");

        lngField = new LabelField();
        lngField.setName("x");

        ToolBar coordBar = new ToolBar();
        coordBar.add(new LabelToolItem(I18N.CONSTANTS.lat()));
        coordBar.add(latField);
        coordBar.add(new LabelToolItem(I18N.CONSTANTS.lng()));
        coordBar.add(lngField);

        panel.setBottomComponent(coordBar);
	}

	private void createPanel() {
		panel = new ContentPanel();
        panel.setHeaderVisible(false);
        panel.setLayout(new FitLayout());
        add(panel);
	}

    @Override
    public void setViewBounds(BoundingBoxDTO bounds) {
        zoomToBounds(createLatLngBounds(bounds));
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

    private void createMarker() {
    	com.google.gwt.maps.client.geom.LatLng latlng = 
    		com.google.gwt.maps.client.geom.LatLng.newInstance(latLng.getLat(), latLng.getLng());
        MarkerOptions options = MarkerOptions.newInstance();
        options.setDraggable(false);

        marker = new Marker(latlng, options);
        map.addOverlay(marker);
    }


    private static LatLngBounds createLatLngBounds(BoundingBoxDTO bounds) {
        return LatLngBounds.newInstance(
        		com.google.gwt.maps.client.geom.LatLng.newInstance(bounds.y1, bounds.x1),
        		com.google.gwt.maps.client.geom.LatLng.newInstance(bounds.y2, bounds.x2));
    }

	@Override
	public void initialize() {
	}

	@Override
	public AsyncMonitor getAsyncMonitor() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setValue(org.sigmah.shared.report.content.AiLatLng value) {
		this.latLng = value;
		
        latField.setValue(value.getLat());
        lngField.setValue(value.getLng());
        createMarker();
        map.setCenter(com.google.gwt.maps.client.geom.LatLng.newInstance(
        		value.getLat(),
        		value.getLng()));
	}

	@Override
	public org.sigmah.shared.report.content.AiLatLng getValue() {
		return latLng;
	}

	@Override
	public BoundingBoxDTO getViewBounds() {
		// TODO Auto-generated method stub
		return null;
	}
}

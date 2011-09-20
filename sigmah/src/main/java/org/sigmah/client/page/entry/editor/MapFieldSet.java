/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.client.page.entry.editor;


import org.sigmah.client.dispatch.AsyncMonitor;
import org.sigmah.client.i18n.I18N;
import org.sigmah.client.map.MapApiLoader;
import org.sigmah.client.map.MapTypeFactory;
import org.sigmah.client.page.common.widget.CoordinateField;
import org.sigmah.client.page.config.form.FieldSetFitLayout;
import org.sigmah.shared.dto.CountryDTO;
import org.sigmah.shared.report.content.AiLatLng;
import org.sigmah.shared.util.mapping.BoundingBoxDTO;

import com.extjs.gxt.ui.client.event.ContainerEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.FieldEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.form.FieldSet;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.toolbar.LabelToolItem;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.event.shared.SimpleEventBus;
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
import com.google.gwt.user.client.rpc.AsyncCallback;

public class MapFieldSet extends FieldSet implements MapEditView {
	private EventBus eventBus = new SimpleEventBus();
	
    private ContentPanel panel;
    private MapWidget map = null;
    private Marker marker = null;

    private CoordinateField latField;
    private CoordinateField lngField;

    private LatLngBounds pendingZoom = null;

    private final CountryDTO country;

    public MapFieldSet(CountryDTO country) {
        this.country = country;
        
        initializeComponent();

        createPanel();
        createPanelToolbar();
        loadMapAync();
    }

    private void loadMapAync() {
    	MapApiLoader.load(null, new AsyncCallback<Void>() {
			@Override
			public void onSuccess(Void result) {
				createMapWidget();				
			}
			
			@Override
			public void onFailure(Throwable caught) {
				// TODO Handle failure
			}
		});
	}

	private void initializeComponent() {
        setHeading(I18N.CONSTANTS.geoPosition());
        setLayout(new FieldSetFitLayout());
        setHeight(250);
	}

	public void init() {
    }

	private void createMapWidget() {
		map = new MapWidget();

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
            	eventBus.fireEvent(
            			new MapViewChangedEvent(createBounds(map.getBounds())));
            }
        });

        map.addMapMoveEndHandler(new MapMoveEndHandler() {
            @Override
            public void onMoveEnd(MapMoveEndEvent mapMoveEndEvent) {
            	eventBus.fireEvent(
            			new MapViewChangedEvent(createBounds(map.getBounds())));
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
        panel.add(map);
	}

	private void createPanelToolbar() {
        Listener<FieldEvent> latLngListener = new Listener<FieldEvent>() {
            public void handleEvent(FieldEvent be) {
            	if (latField.getValue() != null && lngField.getValue()!=null) {
	            	eventBus.fireEvent(new CoordinatesChangedEvent(new AiLatLng(
	            			latField.getValue(), lngField.getValue())));
            	}
            }
        };

		latField = new CoordinateField(CoordinateField.Axis.LATITUDE);
        latField.setName("y");
        latField.setFireChangeEventOnSetValue(true);

        lngField = new CoordinateField(CoordinateField.Axis.LONGITUDE);
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
	}

	private void createPanel() {
		panel = new ContentPanel();
        panel.setHeaderVisible(false);
        panel.setLayout(new FitLayout());
        add(panel);
	}

    public CoordinateField getLatField() {
        return latField;
    }

    public CoordinateField getLngField() {
        return lngField;
    }

    @Override
    public void setEditBounds(String name, BoundingBoxDTO bounds) {
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

    private void createMarker(LatLng latlng) {
        MarkerOptions options = MarkerOptions.newInstance();
        options.setDraggable(true);

        marker = new Marker(latlng, options);

        marker.addMarkerDragEndHandler(new MarkerDragEndHandler() {
            public void onDragEnd(MarkerDragEndEvent event) {
                LatLng latlng = marker.getLatLng();
                eventBus.fireEvent(
                		new MarkerMovedEvent(latlng.getLatitude(), latlng.getLongitude()));
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

	@Override
	public HandlerRegistration addMarkerMovedHandler(MarkerMovedHandler handler) {
		return eventBus.addHandler(MarkerMovedEvent.TYPE, handler);
	}

	@Override
	public HandlerRegistration addCoordinatesChangedHandler(
			CoordinatesChangedHandler handler) {
		return eventBus.addHandler(CoordinatesChangedEvent.TYPE, handler);
	}

	@Override
	public HandlerRegistration addMapViewChangedHandler(
			MapViewChangedHandler handler) {
		return eventBus.addHandler(MapViewChangedEvent.TYPE, handler);
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
        latField.setValue(value.getLat());
        lngField.setValue(value.getLng());
	}

	@Override
	public org.sigmah.shared.report.content.AiLatLng getValue() {
		return new org.sigmah.shared.report.content.AiLatLng(latField.getValue(), lngField.getValue());
	}

	@Override
	public void setMarkerPosition(AiLatLng latLng) {
        LatLng latlng = LatLng.newInstance(latLng.getLat(), latLng.getLng());
        if (marker == null) {
            createMarker(latlng);
        } else {
            marker.setLatLng(latlng);
        }
	}

	@Override
	public void panTo(org.sigmah.shared.report.content.AiLatLng latLng) {
        LatLng latlng = LatLng.newInstance(latLng.getLat(), latLng.getLng());
        map.panTo(latlng);
	}

	@Override
	public BoundingBoxDTO getViewBounds() {
        return createBounds(map.getBounds());
	}

}

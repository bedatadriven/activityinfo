/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.client.report.editor.map;

import java.util.HashMap;
import java.util.Map;

import org.sigmah.client.EventBus;
import org.sigmah.client.dispatch.Dispatcher;
import org.sigmah.client.dispatch.monitor.MaskingAsyncMonitor;
import org.sigmah.client.i18n.I18N;
import org.sigmah.client.map.GoogleMapsReportOverlays;
import org.sigmah.client.map.IconFactory;
import org.sigmah.client.map.MapTypeFactory;
import org.sigmah.client.page.report.HasReportElement;
import org.sigmah.client.page.report.ReportChangeHandler;
import org.sigmah.client.page.report.ReportEventHelper;
import org.sigmah.client.widget.GoogleMapsPanel;
import org.sigmah.shared.command.GenerateElement;
import org.sigmah.shared.command.GetSchema;
import org.sigmah.shared.dto.SchemaDTO;
import org.sigmah.shared.map.BaseMap;
import org.sigmah.shared.report.content.AiLatLng;
import org.sigmah.shared.report.content.MapContent;
import org.sigmah.shared.report.content.MapMarker;
import org.sigmah.shared.report.model.MapReportElement;
import org.sigmah.shared.util.mapping.Extents;

import com.allen_sauer.gwt.log.client.Log;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.Status;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.google.common.base.Objects;
import com.google.gwt.maps.client.InfoWindowContent;
import com.google.gwt.maps.client.MapType;
import com.google.gwt.maps.client.MapWidget;
import com.google.gwt.maps.client.control.ControlAnchor;
import com.google.gwt.maps.client.control.ControlPosition;
import com.google.gwt.maps.client.control.LargeMapControl;
import com.google.gwt.maps.client.event.MapClickHandler;
import com.google.gwt.maps.client.event.MapMoveEndHandler;
import com.google.gwt.maps.client.event.MapClickHandler.MapClickEvent;
import com.google.gwt.maps.client.event.MapZoomEndHandler;
import com.google.gwt.maps.client.geom.LatLng;
import com.google.gwt.maps.client.overlay.Icon;
import com.google.gwt.maps.client.overlay.Marker;
import com.google.gwt.maps.client.overlay.MarkerOptions;
import com.google.gwt.maps.client.overlay.Overlay;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * Displays the content of a MapElement using Google Maps.
 */
public class MapEditorMapView extends GoogleMapsPanel implements HasReportElement<MapReportElement> {
    
	private static final int DEFAULT_ZOOM_CONTROL_OFFSET_X = 5;
    private static final int ZOOM_CONTROL_OFFSET_Y = 5;
	
	private static final int RDC_CENTER_LONG = 25;
	private static final int RDC_CENTER_LAT = -1;

	private final Dispatcher dispatcher;
	private final ReportEventHelper events;
		
	private BaseMap currentBaseMap = null;

    private final Status statusWidget;

    private MapReportElement model = new MapReportElement();
    private GoogleMapsReportOverlays overlays; 
    
    // Model of a the map
    private MapContent content;
	
	// True when the first layer is just put on the map
	private boolean isFirstLayerUpdate=true;

	private LargeMapControl zoomControl;
	private int zoomControlOffsetX = DEFAULT_ZOOM_CONTROL_OFFSET_X;
    
    public MapEditorMapView(Dispatcher dispatcher, EventBus eventBus) {
    	this.dispatcher = dispatcher;
    	this.events = new ReportEventHelper(eventBus, this);
    	this.events.listen(new ReportChangeHandler() {
			
			@Override
			public void onChanged() {
				loadContent();
			}
		});
    	
    	setHeaderVisible(false);

        statusWidget = new Status();
        ToolBar toolBar = new ToolBar();
        toolBar.add(statusWidget);
        setBottomComponent(toolBar);
    }
        
	@Override
	protected void configureMap(MapWidget map) {
		zoomControl = new LargeMapControl();
		map.addControl(zoomControl, zoomControlPosition());
		
		// TODO: generalize
        map.panTo(LatLng.newInstance(RDC_CENTER_LAT, RDC_CENTER_LONG));
        
        map.addMapClickHandler(new MapClickHandler() {
			@Override
			public void onClick(MapClickEvent event) {
				onMapClick(event);
			}
		});
        
        map.addMapZoomEndHandler(new MapZoomEndHandler() {

			@Override
			public void onZoomEnd(MapZoomEndEvent event) {
				updateModelFromMap();
			}
        });
        
        map.addMapMoveEndHandler(new MapMoveEndHandler() {

			@Override
			public void onMoveEnd(MapMoveEndEvent event) {
				updateModelFromMap();
			}
        });
	}
	
    @Override
	protected void onMapInitialized() {
    	
    	// initialize our overlay manager
    	this.overlays = new GoogleMapsReportOverlays(getMapWidget());
    	
        loadContent();
	}
    
    public void setZoomControlOffsetX(int offset) {
    	zoomControlOffsetX = offset;
    	try {
    	if(zoomControl != null) {
    		getMapWidget().removeControl(zoomControl);
    		getMapWidget().addControl(zoomControl, zoomControlPosition());
    	}
    	} catch(Exception e) {
    		Log.error("Exception thrown while setting zoom control", e);
    	}
    }
   
	@Override
	public void bind(MapReportElement model) {
		this.model = model;
		loadContent();
	}
	
	
	
	@Override
	public void disconnect() {
		events.disconnect();
	}

	@Override
	public MapReportElement getModel() {
		return model;
	}

	private ControlPosition zoomControlPosition() {
		return new ControlPosition(ControlAnchor.TOP_LEFT, zoomControlOffsetX, ZOOM_CONTROL_OFFSET_Y);
	}
	
    /**
     * Updates the size of the map and adds Overlays to reflect the content
     * of the current selected indicators
     */
    private void loadContent() {
    	
    	if(!isMapLoaded()) {
    		return;
    	}
    	     
    	overlays.clear();
    	      
    	
    	// Don't update when no layers are present
    	if (model.getLayers().isEmpty()) {
    		isFirstLayerUpdate = true;
    		return;
    	}
    	// Prevent setting the extents for the MapWidget when more then 1 layer is added
		if (isFirstLayerUpdate && 
				model.getLayers().size() > 0) {
			isFirstLayerUpdate = false;
		}
		   	
    	dispatcher.execute(new GenerateElement<MapContent>(model), new MaskingAsyncMonitor(this, I18N.CONSTANTS.loadingMap()), new AsyncCallback<MapContent>() {
			@Override
			public void onFailure(Throwable caught) {
				MessageBox.alert("Load failed", caught.getMessage(), null);
			}

			@Override
			public void onSuccess(MapContent result) {
		        onContentLoaded(result);
			}
		});
    }
   
	private void onContentLoaded(MapContent result) {
		Log.debug("MapPreview: Received content");

		content = result;

		statusWidget.setStatus(result.getUnmappedSites().size() + " " + I18N.CONSTANTS.siteLackCoordiantes(), null);

		overlays.setBaseMap(result.getBaseMap());
		Extents extents = overlays.addMarkers(result.getMarkers());
		
		// can we zoom in further and still see all the markers?
		if(getMapWidget().getBounds().containsBounds(newLatLngBounds(extents))) {
			zoomToBounds(extents);
		}
	}
    
	
	private void onMapClick(MapClickEvent event) {
		if(event.getOverlay() != null) {
			MapMarker marker = overlays.getMapMarker(event.getOverlay());
			if (marker != null) {
				getMapWidget().getInfoWindow().open((Marker)event.getOverlay(), 
						new InfoWindowContent(I18N.CONSTANTS.loading()));
			}
		}
	}

	private void updateModelFromMap() {
		model.setZoomLevel(getMapWidget().getZoomLevel());
		model.setCenter(new AiLatLng(
				getMapWidget().getCenter().getLatitude(), 
				getMapWidget().getCenter().getLongitude()));
	}
}

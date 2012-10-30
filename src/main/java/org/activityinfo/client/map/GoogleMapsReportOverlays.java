package org.activityinfo.client.map;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activityinfo.client.Log;
import org.activityinfo.shared.map.BaseMap;
import org.activityinfo.shared.report.content.AdminMarker;
import org.activityinfo.shared.report.content.AdminOverlay;
import org.activityinfo.shared.report.content.MapContent;
import org.activityinfo.shared.report.content.MapMarker;
import org.activityinfo.shared.report.model.MapReportElement;
import org.activityinfo.shared.util.mapping.Extents;

import com.google.common.base.Objects;
import com.google.gwt.maps.client.MapType;
import com.google.gwt.maps.client.MapWidget;
import com.google.gwt.maps.client.geom.LatLng;
import com.google.gwt.maps.client.overlay.EncodedPolyline;
import com.google.gwt.maps.client.overlay.Icon;
import com.google.gwt.maps.client.overlay.Marker;
import com.google.gwt.maps.client.overlay.MarkerOptions;
import com.google.gwt.maps.client.overlay.Overlay;
import com.google.gwt.maps.client.overlay.Polygon;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class GoogleMapsReportOverlays {
	
	private final MapWidget mapWidget;
    private final Map<Overlay, MapMarker> markers = new HashMap<Overlay, MapMarker>();
    
    private BaseMap currentBaseMap = null;
    
	public GoogleMapsReportOverlays(MapWidget mapWidget) {
		super();
		this.mapWidget = mapWidget;
	}

    public void clear() {
    	mapWidget.clearOverlays();
    	markers.clear();
    }
    
    public void setBaseMap(BaseMap baseMap) {
    	if (!Objects.equal(baseMap, currentBaseMap)) {
            MapType baseMapType = MapTypeFactory.mapTypeForBaseMap(baseMap);
            mapWidget.removeMapType(MapType.getNormalMap());
            mapWidget.removeMapType(MapType.getHybridMap());
            mapWidget.addMapType(baseMapType);
            mapWidget.setCurrentMapType(baseMapType);
            currentBaseMap = baseMap;
    	}
    }

	public Extents addMarkers(List<MapMarker> markers) {
		Extents extents = Extents.emptyExtents();
		for (MapMarker marker : markers) {
            Icon icon = IconFactory.createIcon(marker);
            LatLng latLng = LatLng.newInstance(marker.getLat(), marker.getLng());
            extents.grow(marker.getLat(), marker.getLng());
            MarkerOptions options = MarkerOptions.newInstance();
            options.setIcon(icon);
            options.setTitle(marker.getTitle());
            Marker overlay = new Marker(latLng, options);
            
            mapWidget.addOverlay(overlay);
        }
		return extents;
	}

	public MapMarker getMapMarker(Overlay overlay) {
		return markers.get(overlay);
	}

	public void setView(MapContent content) {
		mapWidget.setZoomLevel(content.getZoomLevel());
		mapWidget.setCenter(LatLng.newInstance(content.getCenter().getLat(), content.getCenter().getLng()));
	}
	
	public void syncWith(MapReportElement element) {
		clear();
		addMarkers(element.getContent().getMarkers());
		setBaseMap(element.getContent().getBaseMap());
		setView(element.getContent());
	}
	
	public void addAdminLayer(final AdminOverlay adminOverlay) {
		AdminGeometryProvider.INSTANCE.get(adminOverlay.getAdminLevelId(), new AsyncCallback<AdminGeometry>() {
			
			@Override
			public void onSuccess(AdminGeometry geometries) {
				addAdminOverlays(adminOverlay, geometries);
			}
			
			@Override
			public void onFailure(Throwable caught) {
				Log.error("Failed to retrieve admin geometry", caught);
			}
		});
		
	}

	protected void addAdminOverlays(AdminOverlay adminOverlay,
			AdminGeometry geometry) {
		
		for(int i=0;i!=geometry.getEntities().length(); ++i) {
			AdminEntityGeometry entityGeometry = geometry.getEntities().get(i);
			AdminMarker marker = adminOverlay.getPolygon(entityGeometry.getAdminEntityId());
			if(marker != null) {
				EncodedPolyline[] polylines = toPolylines(entityGeometry, 
						geometry.getZoomFactor(), geometry.getNumLevels());
				Polygon polygon = Polygon.fromEncoded(polylines, 
						marker.hasValue(), formatColor(marker), 0.5, true);
				
				mapWidget.addOverlay(polygon);
			}
		}
		
	}

	private String formatColor(AdminMarker marker) {
		if(marker.getColor().startsWith("#")) {
			return marker.getColor();
		}
		return "#" + marker.getColor();
	}

	private EncodedPolyline[] toPolylines(AdminEntityGeometry entityGeometry, int zoomFactor, int numLevels) {
		EncodedPolyline[] polylines = new EncodedPolyline[entityGeometry.getPolygons().length()];
		for(int j=0;j!=entityGeometry.getPolygons().length();++j) {
			AdminPolygon adminPolygon = entityGeometry.getPolygons().get(j);
			polylines[j] = EncodedPolyline.newInstance(
					adminPolygon.getPoints(), 
					zoomFactor,
					adminPolygon.getLevels(),
					numLevels);
		}
		return polylines;
	}
}

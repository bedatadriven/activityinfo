package org.sigmah.client.map;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.sigmah.shared.map.BaseMap;
import org.sigmah.shared.report.content.MapContent;
import org.sigmah.shared.report.content.MapMarker;
import org.sigmah.shared.report.model.MapReportElement;
import org.sigmah.shared.util.mapping.Extents;

import com.google.common.base.Objects;
import com.google.gwt.maps.client.MapType;
import com.google.gwt.maps.client.MapWidget;
import com.google.gwt.maps.client.geom.LatLng;
import com.google.gwt.maps.client.overlay.Icon;
import com.google.gwt.maps.client.overlay.Marker;
import com.google.gwt.maps.client.overlay.MarkerOptions;
import com.google.gwt.maps.client.overlay.Overlay;

public class GoogleMapsReportOverlays {
	
	private final MapWidget mapWidget;
    private final Map<Overlay, MapMarker> overlays = new HashMap<Overlay, MapMarker>();
    private BaseMap currentBaseMap = null;
    
	public GoogleMapsReportOverlays(MapWidget mapWidget) {
		super();
		this.mapWidget = mapWidget;
	}

    public void clear() {
    	mapWidget.clearOverlays();
    	overlays.clear();
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
            overlays.put(overlay, marker);
        }
		return extents;
	}

	public MapMarker getMapMarker(Overlay overlay) {
		return overlays.get(overlay);
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
	
}

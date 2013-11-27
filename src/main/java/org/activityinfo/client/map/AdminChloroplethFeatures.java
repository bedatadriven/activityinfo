package org.activityinfo.client.map;

import org.activityinfo.client.util.LeafletUtil;
import org.activityinfo.shared.report.content.AdminMarker;
import org.activityinfo.shared.report.content.AdminOverlay;
import org.discotools.gwt.leaflet.client.jsobject.JSObject;
import org.discotools.gwt.leaflet.client.layers.others.GeoJSONFeatures;

public class AdminChloroplethFeatures extends GeoJSONFeatures {
    
    private final AdminOverlay overlay;
    

    public AdminChloroplethFeatures(AdminOverlay overlay) {
        super();
        this.overlay = overlay;
    }

    @Override
    public JSObject pointToLayer(JSObject feature, JSObject latlng) {
        return null;
    }

    @Override
    public JSObject onEachFeature(JSObject feature, JSObject layer) {
        return feature;
    }

    @Override
    public JSObject style(JSObject feature) {
        int adminEntityId = feature.getPropertyAsInt("id");
        AdminMarker polygon = overlay.getPolygon(adminEntityId);
        
        JSObject style = JSObject.createJSObject();
        style.setProperty("fillColor", LeafletUtil.color(polygon.getColor()));
        style.setProperty("fillOpacity", 0.5);
        style.setProperty("stroke", true);
        style.setProperty("weight", 2);
        style.setProperty("color", LeafletUtil.color(overlay.getOutlineColor()));
        return style;
    }

    @Override
    public boolean filter(JSObject feature, JSObject layer) {
        return true;
    }

}

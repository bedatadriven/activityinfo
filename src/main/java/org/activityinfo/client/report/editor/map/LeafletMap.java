package org.activityinfo.client.report.editor.map;

import org.activityinfo.client.util.LeafletUtil;
import org.activityinfo.shared.util.mapping.Extents;
import org.discotools.gwt.leaflet.client.LeafletResourceInjector;
import org.discotools.gwt.leaflet.client.map.Map;
import org.discotools.gwt.leaflet.client.map.MapOptions;

import com.extjs.gxt.ui.client.widget.Html;

public class LeafletMap extends Html {

    private MapOptions options;
    private Map map;

    public LeafletMap(MapOptions options) {
        this.options = options;
        LeafletResourceInjector.ensureInjected();
        
        setStyleName("gwt-Map");   
        setHtml("<div style=\"width:100%; height: 100%; position: relative;\"></div>");
    }
    
    @Override
    protected void afterRender() {
        super.afterRender();
        map = new Map(getElement().getElementsByTagName("div").getItem(0), options);
    }

    @Override
    protected void onResize(int width, int height) {
        super.onResize(width, height);
        map.invalidateSize(false);
    }

    public Map getMap() {
        return map;
    }

    public void fitBounds(Extents extents) {
        map.fitBounds(LeafletUtil.newLatLngBounds(extents));
    }   
}

package org.activityinfo.client.report.editor.map;


import org.activityinfo.client.report.editor.map.layer.LayerController;
import org.activityinfo.shared.report.model.MapReportElement;
import org.activityinfo.shared.report.model.layers.MapLayer;
import org.discotools.gwt.leaflet.client.layers.raster.TileLayer;
import org.discotools.gwt.leaflet.client.map.Map;

import com.google.common.collect.Maps;
import com.google.gwt.dom.client.MapElement;

public class MapController {
   
    private java.util.Map<MapElement, LayerController> controllers = Maps.newIdentityHashMap();
    private Map map;
    private MapReportElement model;
    private TileLayer tileLayer;
    
    public MapController(Map map, MapReportElement model) {
        this.map = map;
        this.model = model;
    }
    
    public void sync() {
        
        
        for(MapLayer layer : model.getLayers()) {
            LayerController controller = controllers.get(layer);
            if(controller == null) {
                createController(layer);
            } else {
                controller.update();
            }
        }
    }


    private void createController(MapLayer layer) {
    }

}

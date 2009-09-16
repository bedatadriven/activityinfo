package org.activityinfo.client.map;

import com.google.gwt.maps.client.MapType;
import com.google.gwt.maps.client.MapWidget;
import com.google.gwt.maps.client.TileLayer;
import com.google.gwt.maps.client.geom.MercatorProjection;
import com.google.gwt.maps.client.geom.Projection;
import org.activityinfo.client.map.BaseMapLayer;
import org.activityinfo.client.map.RgcTileLayer;
import org.activityinfo.shared.map.BaseMap;

public class MapTypeFactory {

	
	public static MapType createLocalisationMapType() {
		
		TileLayer[] layers = new TileLayer[1];
		layers[0] = new RgcTileLayer("http://www.pear.cd/ActivityInfo/tiles/admin", 6, 10);
		
		Projection projection = new MercatorProjection(11);
		
		return new MapType(layers, projection, "Admin");
	
	}

    public static MapType createBaseMapType(BaseMap baseMap) {

        TileLayer[] layers = new TileLayer[1];
	    layers[0] = new BaseMapLayer(baseMap);
        Projection projection = new MercatorProjection(baseMap.getMaxZoom()+1);

        return new MapType(layers, projection, baseMap.getName());
    }
	
	public static void clear(MapWidget w) {
		for(MapType t : w.getMapTypes()) {
			w.removeMapType(t);			
		}
		
	}
}

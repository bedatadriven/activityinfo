package org.activityinfo.shared.map;

public class MapboxLayers {

    public static final String MAPBOX_STREETS = "http://{s}.tiles.mapbox.com/v3/activityinfo.gc3n5efh/{z}/{x}/{y}.png";

    public static final String MAPBOX_TERRAIN = "http://{s}.tiles.mapbox.com/v3/activityinfo.gcg3g01h/{z}/{x}/{y}.png";
    
    public static final String MAPBOX_SATELLITE = "http://{s}.tiles.mapbox.com/v3/activityinfo.gcg3l5eb/{z}/{x}/{y}.png";
    
    public static final String MAPBOX_HYBRID = "http://{s}.tiles.mapbox.com/v3/activityinfo.gcg4ei82/{z}/{x}/{y}.png";

    public static TileBaseMap toTileBaseMap(BaseMap baseMap) {
        if(baseMap instanceof TileBaseMap) {
            return (TileBaseMap) baseMap;
        } else {
            String url;
            if(baseMap.equals(GoogleBaseMap.ROADMAP)) {
                url = MAPBOX_STREETS;
            } else if(baseMap.equals(GoogleBaseMap.TERRAIN)) {
                url = MAPBOX_TERRAIN;
            } else if(baseMap.equals(GoogleBaseMap.SATELLITE)) {
                url = MAPBOX_SATELLITE;
            } else if(baseMap.equals(GoogleBaseMap.HYBRID)) {
                url = MAPBOX_HYBRID;
            } else {
                url = MAPBOX_STREETS;
            }
            TileBaseMap tileBaseMap = new TileBaseMap();
            tileBaseMap.setId(url);
            tileBaseMap.setMinZoom(2);
            tileBaseMap.setMaxZoom(18);
            tileBaseMap.setTileUrlPattern(url);
            return tileBaseMap;
        }
    }
}

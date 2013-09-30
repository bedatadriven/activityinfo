package org.activityinfo.server.util.mapping;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryCollection;
import com.vividsolutions.jts.geom.Point;

public class JtsUtil {

    public static boolean contains(Geometry geometry, Point point) {
        // MySQL seems to store all of our multipolygons as GeometryCollections
        
        if(geometry instanceof GeometryCollection) {
            GeometryCollection collection = (GeometryCollection) geometry;
            for(int i=0;i!=collection.getNumGeometries();++i) {
                if(contains(collection.getGeometryN(i), point)) {
                    return true;
                }
            }
            return false;
        } else {
            return geometry.contains(point);
        }
    }

}

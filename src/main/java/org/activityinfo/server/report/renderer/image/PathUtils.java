package org.activityinfo.server.report.renderer.image;

import org.activityinfo.server.report.generator.map.TiledMap;
import org.activityinfo.shared.report.content.AiLatLng;
import org.activityinfo.shared.report.content.Point;

import com.google.code.appengine.awt.geom.GeneralPath;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.Polygon;

public class PathUtils {

    public static GeneralPath toPath(TiledMap map, Geometry geometry) {
        GeneralPath path = new GeneralPath();
        for (int i = 0; i != geometry.getNumGeometries(); ++i) {
            Polygon polygon = (Polygon) geometry.getGeometryN(i);
            PathUtils.addRingToPath(map, path, polygon.getExteriorRing().getCoordinates());
            for (int j = 0; j != polygon.getNumInteriorRing(); ++j) {
                PathUtils.addRingToPath(map, path, polygon.getInteriorRingN(j)
                    .getCoordinates());
            }
            break;
        }
        return path;
    }

    private static void addRingToPath(TiledMap map, GeneralPath path,
        Coordinate[] coordinates) {
        System.out.println("--ring--");

        float lastX = Float.NaN;
        float lastY = Float.NaN;
        for (int j = 0; j != coordinates.length; ++j) {
            Point point = map.fromLatLngToPixel(new AiLatLng(coordinates[j].y,
                coordinates[j].x));
            float x = point.getX();
            float y = point.getY();

            if (x != lastX || y != lastY) {
                System.out.println(point.getX() + "," + point.getY());
                if (j == 0) {
                    path.moveTo(x, y);
                } else {
                    path.lineTo(x, y);
                }
            }
            lastX = x;
            lastY = y;
        }
        path.closePath();
    }

}

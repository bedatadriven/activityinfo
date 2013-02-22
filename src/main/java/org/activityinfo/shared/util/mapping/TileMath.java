package org.activityinfo.shared.util.mapping;

/*
 * #%L
 * ActivityInfo Server
 * %%
 * Copyright (C) 2009 - 2013 UNICEF
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the 
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public 
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */

import org.activityinfo.shared.report.content.AiLatLng;
import org.activityinfo.shared.report.content.Point;

/**
 * Provides a series of utility functions useful for processing map tiles
 * created in the Googlian sense (spherical mercator) on the server.
 * 
 * See:
 * <ul>
 * <li>http://en.wikipedia.org/wiki/Mercator_projection</li>
 * <li>
 * http://code.google.com/apis/maps/documentation/overlays.html#CustomMapTiles</li>
 * </ul>
 * 
 * @author Alex Bertram
 * 
 */
public final class TileMath {

    private static final int MAX_ZOOM = 16;
    /*
     * Various math constants
     */
    private static final double D2R = 0.0174532925;
    private static final double EPSLN = 1.0e-10;
    private static final double HALF_PI = 1.5707963267948966192313216916398;
    private static final double TWO_PI = 6.283185307179586476925286766559;
    private static final double FORTPI = 0.78539816339744833;
    private static final double DEGREES_PER_RADIAN = 57.2957795;

    private static final int TILE_SIZE = 256;

    private TileMath() {
    }

    /**
     * Returns the circumference of the projected coordinate system at the
     * equator (and elsewhere perhaps? )
     * 
     * @param zoom
     * @return
     */
    public static int size(int zoom) {
        return (int) (float) (Math.pow(2, zoom) * TILE_SIZE);
    }

    public static double radius(int zoom) {
        return size(zoom) / TWO_PI;
    }

    /**
     * 
     * Projects a geographic X (longitude) coordinate forward into the Googlian
     * screen pixel coordinate system, based on a given zoom level
     * 
     * @param lng
     * @param zoom
     * @return
     */
    public static Point fromLatLngToPixel(AiLatLng latlng, int zoom) {
        double size = size(zoom);
        double radius = size / TWO_PI;

        double x = (latlng.getLng() + 180.0) / 360.0 * size;

        double lat = latlng.getLat() * D2R;

        if (Math.abs(Math.abs(lat) - HALF_PI) <= EPSLN) {
            throw new IllegalArgumentException(
                "Too close to the poles to project");
        }

        double ty = (size) / 2.0;
        double y = radius * Math.log(Math.tan(FORTPI + 0.5 * lat));
        y = ty - y;

        return new Point(x, y);
    }

    public static AiLatLng inverse(Point px, int zoom) {

        double size = size(zoom);
        double radius = size / TWO_PI;

        double lng = (px.getDoubleX() / size * 360d) - 180d;

        double ty = (size) / 2.0;
        double y = (ty - px.getDoubleY()) / radius;
        double lat = Math.atan(Math.sinh(y)) * DEGREES_PER_RADIAN;

        return new AiLatLng(lat, lng);
    }

    /**
     * 
     * Returns the maximum zoom level at which the given extents will fit inside
     * the map of the given size
     * 
     * @param extent
     * @param mapWidth
     * @param mapHeight
     * @return
     */
    public static int zoomLevelForExtents(Extents extent, int mapWidth,
        int mapHeight) {

        int zoomLevel = 1;

        do {

            Point upperLeft = fromLatLngToPixel(new AiLatLng(
                extent.getMaxLat(), extent.getMinLon()), zoomLevel);
            Point lowerRight = fromLatLngToPixel(
                new AiLatLng(extent.getMinLat(), extent.getMaxLon()), zoomLevel);

            int extentWidth = lowerRight.getX() - upperLeft.getX();

            // assert extentWidth >= 0;

            if (extentWidth > mapWidth) {
                return zoomLevel - 1;
            }

            int extentHeight = lowerRight.getY() - upperLeft.getY();

            if (extentHeight > mapHeight) {
                return zoomLevel - 1;
            }

            zoomLevel++;

        } while (zoomLevel < MAX_ZOOM);

        return zoomLevel;
    }

    /**
     * Returns the coordinate of the tile given a point in the projected
     * coordinate system.
     * 
     * @param px
     * @return
     */
    public static Tile tileForPoint(Point px) {
        return new Tile(px.getX() / TILE_SIZE, px.getY() / TILE_SIZE);
    }
}

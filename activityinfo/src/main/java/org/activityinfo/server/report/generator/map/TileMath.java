package org.activityinfo.server.report.generator.map;

import org.activityinfo.shared.report.content.Extents;
import org.activityinfo.shared.report.content.LatLng;
import org.activityinfo.shared.report.content.Point;


/**
 * Provides a series of utility functions useful for processing
 * map tiles created in the Googlian sense (spherical mercator)
 * on the server.
 * 
 * See:
 * <ul>
 * <li>http://en.wikipedia.org/wiki/Mercator_projection</li>
 * <li>http://code.google.com/apis/maps/documentation/overlays.html#CustomMapTiles</li>
 * </ul>
 * 
 * @author Alex Bertram
 *
 */

public class TileMath {

	/*
	 * Various math constants
	 */
	private static final double D2R = 0.0174532925;
	private static final double EPSLN = 1.0e-10;
	private static final double HALF_PI = 1.5707963267948966192313216916398;
	private static final double TWO_PI = 6.283185307179586476925286766559;
	private static final double FORTPI = 0.78539816339744833;

	
	/**
	 * Returns the circumference of the projected coordinate 
	 * system at the equator (and elsewhere perhaps? )
	 * 
	 * @param zoom
	 * @return
	 */
	public static int size(int zoom) {
		return (int)(float)(Math.pow(2, zoom) * 256f);
	}
	
	public static double radius(int zoom) { 
		return size(zoom) / TWO_PI;
	}

	/**
	 * 
	 * Projects a geographic X (longitude) coordinate forward into
	 * the Googlian screen pixel coordinate system, based on a given
	 * zoom level
	 * 
	 * @param lng
	 * @param zoom
	 * @return
	 */
	
	public static Point fromLatLngToPixel(LatLng latlng, int zoom) {

		double size = size(zoom);
		double radius = size / TWO_PI;
		
		double x = (latlng.getLng() + 180.0) / 360.0 * size; 
		
		double lat = latlng.getLat() * D2R;
		
		if(Math.abs( Math.abs(lat) - HALF_PI)  <= EPSLN) {
			throw new IllegalArgumentException("Too close to the poles to project");
		}
		
		double ty = ((double)size) / 2.0;
	    double y = radius * Math.log(Math.tan(FORTPI + 0.5*lat));
	    	   y = ty - y;
			   
		return new Point( (int)Math.round(x), (int)Math.round(y) );
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
	public static int zoomLevelForExtents(Extents extent, int mapWidth, int mapHeight) {

		int zoomLevel = 1;

		do {

			Point upperLeft = fromLatLngToPixel(new LatLng(extent.getMaxLat(), extent.getMinLon()), zoomLevel);
			Point lowerRight = fromLatLngToPixel(new LatLng(extent.getMinLat(), extent.getMaxLon()), zoomLevel);
			
			int extentWidth = lowerRight.x - upperLeft.x;
			
			//assert extentWidth >= 0;

			if(extentWidth > mapWidth) 
				return zoomLevel - 1;

			int extentHeight = lowerRight.y - upperLeft.y;
			
			if(extentHeight > mapHeight)
				return zoomLevel - 1;

			zoomLevel++;
			
		} while(zoomLevel < 16);

		return zoomLevel;
	}

	/**
	 * Returns the coordinate of the tile given a point in the
	 * projected coordinate system.
	 * 
	 * @param px
	 * @return
	 */
	public static Tile tileForPoint(Point px) {
		
		return new Tile( px.x / 256, px.y / 256 );
		
	}
	
}


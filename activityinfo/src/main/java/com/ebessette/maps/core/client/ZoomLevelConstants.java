/**
 * Created on Jun 4, 2008
 */
package com.ebessette.maps.core.client;

/**
 * Constants used by Google Maps<br>
 * See
 * http://code.google.com/apis/maps/documentation/reference.html#GGeoAddressAccuracy
 * for more info
 * @author Eric Bessette <dev@ebessette.com>
 */
public class ZoomLevelConstants {

	// Acurracy levels

	/**
	 * 
	 */
	public static final int	ACCURACY_UNKNOWN		= 0;

	/**
	 * 
	 */
	public static final int	ACCURACY_COUNTRY		= 1;

	/**
	 * 
	 */
	public static final int	ACCURACY_REGION			= 2;

	/**
	 * 
	 */
	public static final int	ACCURACY_SUBREGION		= 3;

	/**
	 * 
	 */
	public static final int	ACCURACY_TOWN			= 4;

	/**
	 * 
	 */
	public static final int	ACCURACY_POSTALCODE		= 5;

	/**
	 * 
	 */
	public static final int	ACCURACY_STREET			= 6;

	/**
	 * 
	 */
	public static final int	ACCURACY_INTERSECTION	= 7;

	/**
	 * 
	 */
	public static final int	ACCURACY_ADDRESS		= 8;

	/**
	 * 
	 */
	public static final int	ACCURACY_PREMISE		= 9;

	// Zoom levels
	/**
	 * The minimum zoom level possible for Google Maps
	 */
	public static final int	ZOOM_MINIMUM			= 0;

	/**
	 * 
	 */
	public static final int	ZOOM_COUNTRY			= 4;

	/**
	 * 
	 */
	public static final int	ZOOM_REGION				= 6;

	/**
	 * 
	 */
	public static final int	ZOOM_SUBREGION			= 7;

	/**
	 * 
	 */
	public static final int	ZOOM_TOWN				= 8;

	/**
	 * 
	 */
	public static final int	ZOOM_POSTALCODE			= 10;

	/**
	 * 
	 */
	public static final int	ZOOM_STREET				= 12;

	/**
	 * 
	 */
	public static final int	ZOOM_ADDRESS			= 14;

	/**
	 * The maximum zoom level possible for Google Maps
	 */
	public static final int	ZOOM_MAXIMUM			= 17;

}

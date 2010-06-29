package org.sigmah.shared.report.content;

/**
 * LatLng is a point in geographical coordinates longitude and latitude
 * 
 * This class is immutable
 * 
 * @author Alex Bertram
 *
 */
public class LatLng {
	
	public LatLng(double lat, double lon) {
		super();
		this.lat = lat;
		this.lng = lon;
	}
	
	private final double lat;
	private final double lng;
	
	/**
	 * 
	 * @return The latitude of the point (y-axis)
	 */
	public double getLat() {
		return lat;
	}
	
	/**
	 * 
	 * @return The longitude of the point (x-axis)
	 */
	public double getLng() {
		return lng;
	}
}

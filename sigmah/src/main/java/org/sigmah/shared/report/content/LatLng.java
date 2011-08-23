/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.shared.report.content;

import java.io.Serializable;

/**
 * LatLng is a point in geographical coordinates longitude and latitude
 * 
 * This class is immutable
 */
public class LatLng implements Serializable {
	
	public LatLng() {
	}

	public LatLng(double lat, double lon) {
		super();
		this.lat = lat;
		this.lng = lon;
	}
	
	private double lat;
	private double lng;
	
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

	public void setLat(double lat) {
		this.lat = lat;
	}

	public void setLng(double lng) {
		this.lng = lng;
	}
	
	
}

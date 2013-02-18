/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.activityinfo.shared.util.mapping;

import java.io.Serializable;

import org.activityinfo.shared.report.content.AiLatLng;

/*
 * Bounding box for a map. 
 * 
 * This cannot be mapped 1:1 to a rectangle, since a lat/long combination is a coordinate on
 * a sphere as opposed to a coordinate on a 2D plane.
 */
public class Extents implements Serializable {

	private static final int LAT_MAX = 90;
	private static final int LNG_MAX = 180;
	private static final int LAT_MIN = -LAT_MAX;
	private static final int LNG_MIN = -180;

	
	private double minLat;
	private double maxLat;
	private double minLon;
	private double maxLon;

	private Extents() {

	}

	public Extents(double minLat, double maxLat, double minLon, double maxLon) {
		super();
		this.minLat = minLat;
		this.maxLat = maxLat;
		this.minLon = minLon;
		this.maxLon = maxLon;
	}

	public Extents(Extents toCopy) {
		super();
		this.minLat = toCopy.minLat;
		this.maxLat = toCopy.maxLat;
		this.minLon = toCopy.minLon;
		this.maxLon = toCopy.maxLon;
	}

	/**
	 *    
	 * @return maximum geographic bounds (-180, -90, 180, 90)s
	 */
	public static Extents maxGeoBounds() {
		return new Extents(LAT_MIN, LAT_MAX, LNG_MIN, LNG_MAX);
	}

	public double getMinLat() {
		return minLat;
	}
	public void setMinLat(double minLat) {
		this.minLat = minLat;
	}
	public double getMaxLat() {
		return maxLat;
	}
	public void setMaxLat(double maxLat) {
		this.maxLat = maxLat;
	}
	public double getMinLon() {
		return minLon;
	}
	public void setMinLon(double minLon) {
		this.minLon = minLon;
	}
	public double getMaxLon() {
		return maxLon;
	}
	public void setMaxLon(double maxLon) {
		this.maxLon = maxLon;
	}


	public void grow(double lat, double lng) {
		if(lat < minLat) {
			minLat = lat;
		} 
		if(lat > maxLat) {
			maxLat = lat;
		}
		if(lng < minLon) {
			minLon = lng;
		}
		if(lng > maxLon) {
			maxLon = lng;
		}
	}


	/**
	 * Calculates the intersection of this Extents with given
	 * Extents
	 * 
	 * @param b
	 *            another Extents with which to intersect this
	 *            Extents
	 * @return the intersection of the two Extentss
	 */
	public Extents intersect(Extents b) {
		return new Extents(
				Math.max(minLat, b.minLat), 
				Math.min(maxLat, b.maxLat),
				Math.max(minLon, b.minLon), 
				Math.min(maxLon, b.maxLon) 
			);
	}

	/**
	 * @return true if this Extents intersects with <code>b</code>
	 */
	public boolean intersects(Extents b) {
		return !(b.maxLon < minLon || b.minLon > maxLon || b.maxLat < minLat || b.minLat > maxLat);
	}


	public void grow(Extents extents) {

		if(!extents.isEmpty()) {
			grow(extents.minLat, extents.minLon);
			grow(extents.maxLat, extents.maxLon);
		}
	}

	public static Extents emptyExtents() {
		return new Extents(+90.0, -90.0, +180.0, -180.0);		
	}

	public static Extents empty() {
		return emptyExtents();
	}


	/**
	 * 
	 * @param b
	 * @return true if this Extents contains <code>b</code>
	 */
	public boolean contains(Extents b) {
		return b.minLon >= minLon && b.maxLon <= maxLon && b.minLat >= minLat && b.maxLat <= maxLat;
	}

	public boolean contains(AiLatLng center) {
		return contains(center.getLng(), center.getLat());
	}
	/**
	 * 
	 * @return true if this Extents contains the point at (x,y)
	 */
	public boolean contains(double x, double y) {
		return x >= minLon && x <= maxLon && y >= minLat && y <= maxLat;
	}

	public static Extents create(double x1, double y1, double x2, double y2) {
		return new Extents(y1,y2,x1,x2);
	}

	/**
	 * @return the x (longitude) coordinate of the Extents's centroid,
	 *         (x1+x2)/2
	 */
	public double getCenterX() {
		return (minLon + maxLon) / 2;
	}

	/**
	 * 
	 * @return the y (latitudinal) coordinate of the Extents's centroid,
	 *         (y1+y2)/2
	 */
	public double getCenterY() {
		return (minLat + maxLat) / 2;
	}

	public boolean isEmpty() {
		return minLat > maxLat || minLon > maxLon;
	}

	public AiLatLng center() {
		return new AiLatLng( (minLat + maxLat) / 2.0, (minLon + maxLon) / 2.0 );
	}
	
	

	@Override
	public int hashCode() {
		return (minLon+"").hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		Extents other = (Extents) obj;
		return minLat == other.minLat &&
			   maxLat == other.maxLat && 
			   minLon == other.minLon &&
			   maxLon == other.maxLon;
	}

	@Override
	public String toString() {
		return "Extents{" +
				"minLat=" + minLat +
				", maxLat=" + maxLat +
				", minLon=" + minLon +
				", maxLon=" + maxLon +
				'}';
	}

}

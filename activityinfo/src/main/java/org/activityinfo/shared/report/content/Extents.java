package org.activityinfo.shared.report.content;

import org.activityinfo.shared.report.content.LatLng;

import java.io.Serializable;

public class Extents implements Serializable {

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

    public void grow(Extents extents) {

        if(!extents.isEmpty()) {
            grow(extents.minLat, extents.minLon);
            grow(extents.maxLat, extents.maxLon);
        }
    }
	
	public static Extents emptyExtents() {
		return new Extents(+90.0, -90.0, +180.0, -180.0);		
	}

    public boolean isEmpty() {
        return minLat > maxLat || minLon > maxLon;
    }

	public LatLng center() {
		return new LatLng( (minLat + maxLat) / 2.0, (minLon + maxLon) / 2.0 );
	}
}

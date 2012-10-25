package org.activityinfo.server.geo;

import com.vividsolutions.jts.geom.Geometry;

/**
 * Geometry of an administrative entity, in the WGS84 geographic
 * coordinate system.
 */
public class AdminGeo {
	private int id;
	private Geometry geometry;
	
	public AdminGeo(int id, Geometry geometry) {
		super();
		this.id = id;
		this.geometry = geometry;
	}
	/**
	 * 
	 * @return the id of the administrative entity ({@code AdminEntityId}
	 */
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	/**
	 * 
	 * @return the geometry of the administrative entity in the WGS84
	 * geographic coordinate system
	 */
	public Geometry getGeometry() {
		return geometry;
	}
	
	
	public void setGeometry(Geometry geometry) {
		this.geometry = geometry;
	}
	
	
}

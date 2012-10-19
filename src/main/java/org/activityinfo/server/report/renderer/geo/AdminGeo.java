package org.activityinfo.server.report.renderer.geo;

import com.vividsolutions.jts.geom.Geometry;

public class AdminGeo {
	private int id;
	private Geometry geometry;
	
	public AdminGeo(int id, Geometry geometry) {
		super();
		this.id = id;
		this.geometry = geometry;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public Geometry getGeometry() {
		return geometry;
	}
	public void setGeometry(Geometry geometry) {
		this.geometry = geometry;
	}
	
	
}

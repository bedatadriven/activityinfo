package org.activityinfo.shared.report.content;

import java.io.Serializable;
import java.util.Collection;
import java.util.Map;

import com.google.common.collect.Maps;



public class AdminOverlay implements Serializable {
	
	private Map<Integer, Polygon> polygons;
	private int adminLevelId;
	
	public AdminOverlay() {
		
	}
	
	public AdminOverlay(int adminLevelId) {
		super();
		this.adminLevelId = adminLevelId;
		this.polygons = Maps.newHashMap();
	}

	public Polygon getPolygon(int adminLevelId) {
		return polygons.get(adminLevelId);
	}
	
	public Collection<Polygon> getPolygons() {
		return polygons.values();
	}

	public void setPolygons(Map<Integer, Polygon> polygons) {
		this.polygons = polygons;
	}

	public int getAdminLevelId() {
		return adminLevelId;
	}

	public void setAdminLevelId(int adminLevelId) {
		this.adminLevelId = adminLevelId;
	}

	public void addPolygon(Polygon polygon) {
		polygons.put(polygon.getAdminEntityId(), polygon);
	}
	
}

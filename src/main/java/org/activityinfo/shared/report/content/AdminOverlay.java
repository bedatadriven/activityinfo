package org.activityinfo.shared.report.content;

import java.io.Serializable;
import java.util.Collection;
import java.util.Map;

import com.google.common.base.Joiner;
import com.google.common.collect.Maps;



public class AdminOverlay implements Serializable {
	
	private Map<Integer, AdminPolygon> polygons;
	private int adminLevelId;
	
	public AdminOverlay() {
		
	}
	
	public AdminOverlay(int adminLevelId) {
		super();
		this.adminLevelId = adminLevelId;
		this.polygons = Maps.newHashMap();
	}

	public AdminPolygon getPolygon(int adminEntityId) {
		return polygons.get(adminEntityId);
	}
	
	public Collection<AdminPolygon> getPolygons() {
		return polygons.values();
	}

	public void setPolygons(Map<Integer, AdminPolygon> polygons) {
		this.polygons = polygons;
	}

	public int getAdminLevelId() {
		return adminLevelId;
	}

	public void setAdminLevelId(int adminLevelId) {
		this.adminLevelId = adminLevelId;
	}

	public void addPolygon(AdminPolygon polygon) {
		polygons.put(polygon.getAdminEntityId(), polygon);
	}
	
	@Override
	public String toString() {
		return Joiner.on("\n").join(polygons.values());
	}
	
}

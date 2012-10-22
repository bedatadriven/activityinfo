package org.activityinfo.shared.report.content;

import java.io.Serializable;

import org.activityinfo.shared.dto.AdminEntityDTO;
import org.activityinfo.shared.util.mapping.Extents;

public class AdminMarker implements Serializable {
	private int adminEntityId;
	private String name;
	private Double value;
	private String color;
	private Extents bounds;
	
	public AdminMarker(AdminEntityDTO entity) {
		this.adminEntityId = entity.getId();
		this.name = entity.getName();
		this.bounds = entity.getBounds();
	}
	public int getAdminEntityId() {
		return adminEntityId;
	}
	public void setAdminEntityId(int adminEntityId) {
		this.adminEntityId = adminEntityId;
	}
	public double getValue() {
		return value;
	}
	public void setValue(Double value) {
		this.value = value;
	}
	public String getColor() {
		return color;
	}
	public void setColor(String color) {
		this.color = color;
	}
	public Extents getExtents() {
		return bounds;
	}
	public void setBounds(Extents bounds) {
		this.bounds = bounds;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public boolean hasValue() {
		return value != null;
	}
	
	@Override
	public String toString() {
		return name + "[" + adminEntityId + "] => " + value + " (" + color + ")";
	}
}
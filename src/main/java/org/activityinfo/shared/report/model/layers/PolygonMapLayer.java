package org.activityinfo.shared.report.model.layers;


public class PolygonMapLayer extends AbstractMapLayer {

	private int adminLevelId;
	private String maxColor = "#FF0000";
	
	@Override
	public boolean supportsMultipleIndicators() {
		return true;
	}

	@Override
	public String getTypeName() {
		return "Polygon";
	}

	public int getAdminLevelId() {
		return adminLevelId;
	}

	public void setAdminLevelId(int adminLevelId) {
		this.adminLevelId = adminLevelId;
	}

	public String getMaxColor() {
		return maxColor;
	}

	public void setMaxColor(String maxColor) {
		this.maxColor = maxColor;
	}

	@Override
	public String toString() {
		return "PolygonMapLayer [adminLevelId=" + adminLevelId + ", maxColor="
				+ maxColor + "]";
	}
	
}

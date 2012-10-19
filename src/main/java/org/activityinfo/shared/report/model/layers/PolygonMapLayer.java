package org.activityinfo.shared.report.model.layers;


public class PolygonMapLayer extends AbstractMapLayer {

	private int adminLevelId;
	
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
	
}

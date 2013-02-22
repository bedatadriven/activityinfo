package org.activityinfo.shared.report.model.layers;

/*
 * #%L
 * ActivityInfo Server
 * %%
 * Copyright (C) 2009 - 2013 UNICEF
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the 
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public 
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */


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

package org.activityinfo.shared.map;

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

import org.activityinfo.shared.dto.DTO;

/**
 * Basemaps provide the reference imagery on which ActivityInfo-specific
 * overlays are painted.
 */
public abstract class BaseMap implements DTO {

	public static TileBaseMap createNullMap(String baseMapId) {
		TileBaseMap result = new TileBaseMap();
		
		result.setId(baseMapId);
		result.setMinZoom(0);
		result.setMaxZoom(16);
		result.setName("Default map");
		result.setTileUrlPattern("http://google.com");
		
		return result;
	}
	
	public static String getDefaultMapId() {
		return "admin";
	}
	
	public abstract String getId();

	/**
	 * @return the minimum zoom level for which this base map
	 * is available
	 */
	public abstract int getMinZoom();

	/**
	 * @return the maximum zoom level for which this base map
	 * is available
	 */
	public abstract int getMaxZoom();


	public BaseMap() {
		super();
	}

	public boolean hasSingleZoomLevel() {
		return getMinZoom() == getMaxZoom();
	}

}
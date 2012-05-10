package org.activityinfo.shared.map;

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
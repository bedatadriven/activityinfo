package org.sigmah.shared.map;

import org.sigmah.shared.dto.DTO;

public class BaseMap implements DTO {

	protected String id;
	protected String name = "";
	protected int minZoom;
	protected int maxZoom;
	protected String copyright = "";

	public static TileBaseMap createNullMap(String baseMapId) {
		TileBaseMap result = new TileBaseMap();
		
		result.setId(baseMapId);
		result.setMinZoom(0);
		result.setMaxZoom(16);
		result.setName("Default map");
		result.setTileUrlPattern("http://google.com");
		
		return result;
	}

	public String getId() {
	    return id;
	}

	public void setId(String id) {
	    this.id = id;
	}

	public String getName() {
	    return name;
	}

	public void setName(String name) {
	    this.name = name;
	}

	/**
	 * @return the minimum zoom level for which this base map
	 * is available
	 */
	public int getMinZoom() {
	    return minZoom;
	}

	public void setMinZoom(int minZoom) {
	    this.minZoom = minZoom;
	}

	/**
	 * @return the maximum zoom level for which this base map
	 * is available
	 */
	public int getMaxZoom() {
	    return maxZoom;
	}

	public void setMaxZoom(int maxZoom) {
	    this.maxZoom = maxZoom;
	}

	/**
	 * @return the copyright message to be displayed when browsing for this
	 * map.
	 */
	public String getCopyright() {
	    return copyright;
	}

	public void setCopyright(String copyright) {
	    this.copyright = copyright;
	}

	public BaseMap() {
		super();
	}

	public boolean hasSingleZoomLevel() {
		return minZoom==maxZoom;
	}

}
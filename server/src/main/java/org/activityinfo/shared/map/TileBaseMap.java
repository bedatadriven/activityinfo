/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.activityinfo.shared.map;


/**
 * Defines a custom base map (or "background map") that is 
 * accessible on a public server somewhere.
 * 
 * Base Maps are essentially collections of 256x256 images that are
 * tiled together to form a projected base map. 
 * 
 * @author Alex Bertram
 */
public class TileBaseMap extends BaseMap  {

    private String tileUrlPattern = "";
	private String id;
	private String name = "nada";
	private int minZoom;
	private int maxZoom;
	private String copyright = "";
    private String thumbnailUrl;
    
    /**
     * 
     * @return the url pattern used to retrieve individual tiles.
     * Should be in the format http://mt{s}.mytiles.com/tiles?x={x}&y={y}&z={z}
     * 
     * The {s} parameter is used to get around the limitations some browsers 
     * impose on the number of open connections for a given host.
     * 
     */
    public String getTileUrlPattern() {
		return tileUrlPattern;
	}

    public void setTileUrlPattern(String tileUrlPattern) {
		this.tileUrlPattern = tileUrlPattern;
	}

	public String getTileUrl(int zoom, int x, int y) {
		return tileUrlPattern
				.replace("{s}", Integer.toString(x%2+y%2))
				.replace("{x}", Integer.toString(x))
				.replace("{y}", Integer.toString(y))
				.replace("{z}", Integer.toString(zoom));
	}

	@Override
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCopyright() {
		return copyright;
	}

	public void setCopyright(String copyright) {
		this.copyright = copyright;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public int getMinZoom() {
		return minZoom;
	}

	public void setMinZoom(int minZoom) {
		this.minZoom = minZoom;
	}

	@Override
	public int getMaxZoom() {
		return maxZoom;
	}

	public void setMaxZoom(int maxZoom) {
		this.maxZoom = maxZoom;
	}

	/**
	 * 
	 * @return a thumbnail image (175x95) used to preview this layer in the 
	 * Base map dialog
	 */
	public String getThumbnailUrl() {
		return thumbnailUrl;
	}

	public void setThumbnailUrl(String thumbnail) {
		this.thumbnailUrl = thumbnail;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		TileBaseMap other = (TileBaseMap) obj;
		if (id == null) {
			if (other.id != null) {
				return false;
			}
		} else if (!id.equals(other.id)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return name;
	}
}

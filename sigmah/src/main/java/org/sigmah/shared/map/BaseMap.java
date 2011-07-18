/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.shared.map;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;

import org.sigmah.shared.dto.DTO;

/**
 * Defines a base map (or "background map") that is 
 * accessible on a public server somewhere.
 * 
 * Base Maps are essentially collections of 256x256 images that are
 * tiled together to form a projected base map.
 * 
 * @author Alex Bertram
 */
@Entity
public class BaseMap implements DTO {

    private String id;
    private String name = "";
    private int minZoom;
    private int maxZoom;
    private String copyright = "";
    private String tileUrlPattern = "";
    
    @Id
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
    @Lob
    public String getCopyright() {
        return copyright;
    }

    public void setCopyright(String copyright) {
        this.copyright = copyright;
    }
    
    /**
     * 
     * @return the url pattern used to retrieve individual tiles.
     * Should be in the format http://mt{s}.mytiles.com/tiles?x={x}&y={y}&z={z}
     * 
     * The {s} parameter is used to get around the limitations some browsers 
     * impose on the number of open connections for a given host.
     * 
     */
    @Lob
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
	
	public static BaseMap createNullMap(String baseMapId)
	{
		BaseMap result = new BaseMap();
		
		result.setId(baseMapId);
		result.setMinZoom(0);
		result.setMaxZoom(16);
		result.setName("Default map");
		result.setTileUrlPattern("http://google.com");
		
		return result;
	}
}

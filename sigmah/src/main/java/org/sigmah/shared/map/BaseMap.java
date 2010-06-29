/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.shared.map;

import org.sigmah.shared.dto.DTO;
/*
 * @author Alex Bertram
 */

public abstract class BaseMap implements DTO {

    private String id;
    private String name;
    private int minZoom;
    private int maxZoom;
    private String copyright;

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

    public int getMinZoom() {
        return minZoom;
    }

    public void setMinZoom(int minZoom) {
        this.minZoom = minZoom;
    }

    public int getMaxZoom() {
        return maxZoom;
    }

    public void setMaxZoom(int maxZoom) {
        this.maxZoom = maxZoom;
    }

    public String getCopyright() {
        return copyright;
    }

    public void setCopyright(String copyright) {
        this.copyright = copyright;
    }

    public abstract String getTileUrl(int zoom, int x, int y);

    public abstract String getLocalTilePath(int zoom, int x, int y);
}

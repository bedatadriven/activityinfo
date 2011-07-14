/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.shared.report.model;

import java.io.Serializable;

import com.google.gwt.resources.client.ImageResource;

/**
 * A generic bitmap icon that can be used on the client or server side,
 * in tiled maps as well as SVG maps.
 *
 * @author Alex Bertram
 */
public class MapIcon implements Serializable, ImageResource {
    private String name;
	private String url;
	
	// Default to 32x32 sized icons
    private int width = 32; 
    private int height = 32;
    
    private int anchorX;
    private int anchorY;

    public MapIcon() {
    }
	
	public MapIcon(String name) {
        setName(name);
	}
	
    public MapIcon(String name, int width, int height, int anchorX, int anchorY) {
        setName(name);
        this.width = width;
        this.height = height;
        this.anchorX = anchorX;
        this.anchorY = anchorY;
    }

    /**
     *
     * @return A short name of the icon
     */
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        url = "mapicons/" + name + ".png";
    }

    /**
     * 
     * @return The width of the icon image, in pixels
     */
    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    /**
     *
     * @return The height of the icon image, in pixels
     */
    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    /**
     *
     * @return The distance from the left side at which the image should be
     * anchored to the point.
     */
    public int getAnchorX() {
        return anchorX;
    }


    public void setAnchorX(int anchorX) {
        this.anchorX = anchorX;
    }

    /**
     *
     * @return The distance from the top at which of the image should be
     * anchored to the point.
     */
    public int getAnchorY() {
        return anchorY;
    }

    public void setAnchorY(int anchorY) {
        this.anchorY = anchorY;
    }


	@Override
	public int getLeft() {
		return 0;
	}

	@Override
	public int getTop() {
		return 0;
	}

	@Override
	public String getURL() {
		return url;
	}

	@Override
	public boolean isAnimated() {
		return false;
	}
	
	public static ImageResource fromEnum(Icon icon) {
		return new MapIcon(icon.toString());
	}
    
	public enum Icon {
		Default,
		Doctor,
		Fire,
		Food,
		Water
	}
}

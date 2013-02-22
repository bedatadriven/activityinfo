

package org.activityinfo.shared.report.model;

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


import java.io.Serializable;
import com.google.gwt.resources.client.ImageResource;

/**
 * A generic bitmap icon that can be used on the client or server side,
 * in tiled maps as well as SVG maps.
 *
 * @author Alex Bertram
 */
public final class MapIcon implements Serializable, ImageResource {
    private static final int DEFAULT_SIZE = 32;
    
	private String name;
	private String url;
	
	// Default to 32x32 sized icons
    private int width = DEFAULT_SIZE; 
    private int height = DEFAULT_SIZE;
    
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
    @Override
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
    @Override
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
    @Override
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
	
	public static MapIcon fromEnum(Icon icon) {
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

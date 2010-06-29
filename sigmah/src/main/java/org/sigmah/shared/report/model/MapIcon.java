package org.sigmah.shared.report.model;

import java.io.Serializable;

/**
 * A generic bitmap icon that can be used on the client or server side,
 * in tiled maps as well as SVG maps.
 *
 * @author Alex Bertram
 */
public class MapIcon implements Serializable {

    private String name;
    private int width;
    private int height;
    private int anchorX;
    private int anchorY;

    public MapIcon() {
    }

    public MapIcon(String name, int width, int height, int anchorX, int anchorY) {
        this.name = name;
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
}

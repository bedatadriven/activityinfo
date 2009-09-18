package org.activityinfo.shared.report.model;

import java.io.Serializable;
/*
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getAnchorX() {
        return anchorX;
    }

    public void setAnchorX(int anchorX) {
        this.anchorX = anchorX;
    }

    public int getAnchorY() {
        return anchorY;
    }

    public void setAnchorY(int anchorY) {
        this.anchorY = anchorY;
    }
}

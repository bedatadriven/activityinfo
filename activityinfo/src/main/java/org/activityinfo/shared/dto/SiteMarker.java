package org.activityinfo.shared.dto;

import org.activityinfo.shared.command.result.CommandResult;

/**
 * @author Alex Bertram
 */
public class SiteMarker implements CommandResult {

    private float x;
    private float y;
    private int[] siteIds;

    private SiteMarker() {

    }

    public SiteMarker( int[] siteIds, float x, float y) {
        this.x = x;
        this.y = y;
        this.siteIds = siteIds;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public int[] getSiteIds() {
        return siteIds;
    }

    public void setX(float x) {
        this.x = x;
    }

    public void setY(float y) {
        this.y = y;
    }

    public void setSiteIds(int[] siteIds) {
        this.siteIds = siteIds;
    }
}

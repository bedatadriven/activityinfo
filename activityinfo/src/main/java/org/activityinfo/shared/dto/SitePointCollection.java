package org.activityinfo.shared.dto;

import org.activityinfo.shared.command.result.CommandResult;

import java.util.List;

/**
 * @author Alex Bertram
 */
public class SitePointCollection implements CommandResult {

    private Bounds bounds;
    private List<SitePoint> points;

    private SitePointCollection() {

    }

    public SitePointCollection(Bounds bounds, List<SitePoint> points) {
        this.bounds = bounds;
        this.points = points;
    }

    public Bounds getBounds() {
        return bounds;
    }

    public void setBounds(Bounds bounds) {
        this.bounds = bounds;
    }

    public List<SitePoint> getPoints() {
        return points;
    }

    public void setPoints(List<SitePoint> points) {
        this.points = points;
    }
}

package org.sigmah.shared.command.result;

import org.sigmah.shared.dto.BoundingBoxDTO;
import org.sigmah.shared.dto.SitePointDTO;

import java.util.List;

/**
 * @author Alex Bertram
 */
public class SitePointList implements CommandResult {

    private BoundingBoxDTO bounds;
    private List<SitePointDTO> points;

    private SitePointList() {

    }

    public SitePointList(BoundingBoxDTO bounds, List<SitePointDTO> points) {
        this.bounds = bounds;
        this.points = points;
    }

    public BoundingBoxDTO getBounds() {
        return bounds;
    }

    public void setBounds(BoundingBoxDTO bounds) {
        this.bounds = bounds;
    }

    public List<SitePointDTO> getPoints() {
        return points;
    }

    public void setPoints(List<SitePointDTO> points) {
        this.points = points;
    }
}

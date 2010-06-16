package org.activityinfo.server.report.generator.map;

import org.activityinfo.shared.report.content.Point;
import org.activityinfo.shared.report.model.MapIcon;

import java.awt.*;
import java.util.List;

/**
 * @author Alex Bertram
 */
public class IconRectCalculator implements RadiiCalculator {

    private MapIcon icon;

    public IconRectCalculator(MapIcon icon) {
        this.icon = icon;
    }

    public void calculate(List<Cluster> clusters) {
        for(Cluster cluster : clusters) {
            cluster.setRectangle(iconRect(cluster.getPoint()));
        }
     }

    public Rectangle iconRect(int x, int y) {
        return new Rectangle(
                x - icon.getAnchorX(),
                y - icon.getAnchorY(),
                icon.getWidth(),
                icon.getHeight());
    }

    public Rectangle iconRect(Point p) {
        return iconRect(p.getX(), p.getY());
    }
}

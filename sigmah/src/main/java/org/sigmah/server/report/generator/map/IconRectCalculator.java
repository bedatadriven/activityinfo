/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.server.report.generator.map;

import org.sigmah.shared.report.content.Point;
import org.sigmah.shared.report.model.MapIcon;

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

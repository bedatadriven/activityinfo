package org.activityinfo.server.report.generator.map;

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

import java.util.List;

import org.activityinfo.server.report.generator.map.cluster.Cluster;
import org.activityinfo.shared.geom.Rectangle;
import org.activityinfo.shared.report.content.Point;
import org.activityinfo.shared.report.model.MapIcon;

/**
 * @author Alex Bertram
 */
public class IconRectCalculator implements RadiiCalculator {

    private MapIcon icon;

    public IconRectCalculator(MapIcon icon) {
        this.icon = icon;
    }

    @Override
    public void calculate(List<Cluster> clusters) {
        for (Cluster cluster : clusters) {
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

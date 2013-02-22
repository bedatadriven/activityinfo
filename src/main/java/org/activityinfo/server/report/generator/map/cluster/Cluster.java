package org.activityinfo.server.report.generator.map.cluster;

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

import java.util.ArrayList;
import java.util.List;

import org.activityinfo.server.report.generator.map.cluster.genetic.MarkerGraph;
import org.activityinfo.shared.geom.Rectangle;
import org.activityinfo.shared.report.content.Point;
import org.activityinfo.shared.report.model.PointValue;

/**
 * Represents a collection of nodes that
 * are clustered together in a given chromosome
 */
public class Cluster {
    private List<PointValue> pointValues;
    private double radius;
    private Point point;
    private Rectangle rectangle;
    private String title;
    
    /**
     * Creates a cluster positioned at the given point
     * on the projected space.
     * 
     * @param point the position of the cluster
     */
    public Cluster(Point point) {
        pointValues = new ArrayList<PointValue>();
        this.point = point;
    }

    /**
     * Creates a cluster positioned on the given 
     * PointValue
     * 
     * @param pointValue
     */
    public Cluster(PointValue pointValue) {
        pointValues = new ArrayList<PointValue>(1);
        pointValues.add(pointValue);
        point = pointValue.getPx();
    }

	public void addNode(MarkerGraph.Node node) {
        pointValues.add(node.getPointValue());
    }

	public void addPointValue(PointValue pv) {
		pointValues.add(pv);
	}

    public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public boolean hasTitle() {
		return this.title != null;
	}
	
	/**
     *
     * @return The weighted centroid of the cluster
     */
	public Point bboxCenter() {
        int minX = Integer.MAX_VALUE;
        int minY = Integer.MAX_VALUE;
        int maxX = Integer.MIN_VALUE;
        int maxY = Integer.MIN_VALUE;
        for(PointValue pointValue : pointValues) {
            Point p = pointValue.getPx();
            if(p.getX() < minX) {
                minX = p.getX();
            }
            if(p.getY() < minY) {
                minY = p.getY();
            }
            if(p.getX() > maxX) {
                maxX = p.getX();
            }
            if(p.getY() > maxY) {
                maxY = p.getY();
            }
        }

        return new Point((minX + maxX)/2, (minY + maxY)/2);
    }


	public double getRadius() {
        return radius;
    }

    public void setRadius(double radius) {
        this.radius = radius;
    }

    public Point getPoint() {
        return point;
    }

    public void setPoint(Point point) {
        this.point = point;
    }

    public double sumValues() {
        double sum = 0;
        for(PointValue pointValue : pointValues) {
            sum += pointValue.getValue();
        }
        return sum;
    }

	public List<PointValue> getPointValues() {
        return pointValues;
    }

	public Rectangle getRectangle() {
        return rectangle;
    }

	public void setRectangle(Rectangle rectangle) {
        this.rectangle = rectangle;
    }

	public boolean hasPoint() {
		return point != null;
	}
}

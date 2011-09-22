package org.sigmah.server.report.generator.map.cluster;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

import org.sigmah.server.report.generator.map.cluster.genetic.MarkerGraph;
import org.sigmah.shared.report.content.LatLng;
import org.sigmah.shared.report.content.Point;
import org.sigmah.shared.report.model.PointValue;

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
        point = pointValue.px;
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

	
//	public LatLng latLngCentroid() {
//        double latSum=0;
//        double lngSum=0;
//        double count=0;
//
//        for(PointValue pointValue : pointValues) {
//        	if(pointValue.site.hasLatLong()) {
//	            latSum += pointValue.site.getLatitude();
//	            lngSum += pointValue.site.getLongitude();
//	            count++;
//        	}
//        }
//
//        return count == 0 ? null : new LatLng(latSum / count, lngSum / count);
//    }

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
            sum += pointValue.value;
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

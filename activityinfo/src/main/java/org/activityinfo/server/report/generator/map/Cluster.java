package org.activityinfo.server.report.generator.map;

import org.activityinfo.server.report.generator.map.MarkerGraph;
import org.activityinfo.shared.report.content.LatLng;
import org.activityinfo.shared.report.content.Point;

import java.util.List;
import java.util.ArrayList;
import java.awt.*;
/*
 * @author Alex Bertram
 */

/**
 * Represents a collection of nodes that
 * are clustered together in a given chromosone
 */
public class Cluster {
    private List<PointValue> pointValues;
    private double radius;
    private Point point;
    private Rectangle rectangle;

    public Cluster(Point point) {
        pointValues = new ArrayList<PointValue>();
        this.point = point;
    }

    public Cluster(MarkerGraph.Node node) {
        pointValues = new ArrayList<PointValue>(1);
        pointValues.add(node.getPointValue());
        this.point = node.getPoint();
    }

    public Cluster(PointValue pointValue) {
        pointValues = new ArrayList<PointValue>(1);
        pointValues.add(pointValue);
        point = pointValue.px;
    }

    public void addNode(MarkerGraph.Node node) {
        pointValues.add(node.getPointValue());
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
            Point p = pointValue.px;
            if(p.getX() < minX) minX = p.getX();
            if(p.getY() < minY) minY = p.getY();
            if(p.getX() > maxX) maxX = p.getX();
            if(p.getY() > maxY) maxY = p.getY();
        }

        return new Point((minX + maxX)/2, (minY + maxY)/2);
    }

    public LatLng latLngCentroid() {
        double latSum=0;
        double lngSum=0;
        double count=0;

        for(PointValue pointValue : pointValues) {
            latSum += pointValue.site.getLatitude();
            lngSum += pointValue.site.getLongitude();
            count++;
        }

        return new LatLng(latSum / count, lngSum / count);

    }

    public Point weightedCentroid() {
            double weightSum = 0;
            double x = 0;
            double y = 0;
            for(PointValue pointValue : pointValues) {
                weightSum += pointValue.value;
                x += pointValue.px.getX() * pointValue.value;
                y += pointValue.px.getY() * pointValue.value;
            }

            return new Point((int)Math.round(x / weightSum), (int)Math.round(y / weightSum));
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
}

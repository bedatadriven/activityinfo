package org.activityinfo.server.report.generator.map;

import org.activityinfo.server.report.generator.map.cluster.genetic.MarkerGraph;
import org.activityinfo.server.report.generator.map.cluster.genetic.MarkerGraph.IntersectionCalculator;


public class BubbleIntersectionCalculator implements IntersectionCalculator {
    private int radius;

    public BubbleIntersectionCalculator(int radius) {
        this.radius = radius;
    }

    public boolean intersects(MarkerGraph.Node a, MarkerGraph.Node b) {
        return a.getPoint().distance(b.getPoint()) < radius *2 &&
                a.getPointValue().getSymbol().equals(b.getPointValue().getSymbol());
    }
}
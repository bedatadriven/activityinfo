package org.activityinfo.server.report.generator.map;

import org.activityinfo.server.report.generator.map.CircleMath;
import org.activityinfo.shared.report.content.Point;
import org.junit.Test;
import org.junit.Assert;

public class CircleMathTest {


    @Test
    public void testNoIntersection() {

        Point a = new Point(0, 0);
        Point b = new Point(5, 0);

        Assert.assertEquals(0.0, CircleMath.intersectionArea(a, b, 1, 2));

    }

    @Test
    public void testTangentIntersection() {
        Point a = new Point(0, 0);
        Point b = new Point(2, 0);

        Assert.assertEquals(0.0, CircleMath.intersectionArea(a, b, 1, 1));
    }

    @Test
    public void testCompletelyContained() {
        Point a = new Point(297, 212);
        Point b = new Point(295, 213);

        Assert.assertEquals(CircleMath.area(5), CircleMath.intersectionArea(a, b, 8, 5));
    }


}

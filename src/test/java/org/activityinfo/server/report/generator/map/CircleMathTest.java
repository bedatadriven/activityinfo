

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

import org.activityinfo.server.report.generator.map.CircleMath;
import org.activityinfo.shared.report.content.Point;
import org.junit.Assert;
import org.junit.Test;

public class CircleMathTest {
    private static final double DELTA = 0.001;


    @Test
    public void testNoIntersection() {

        Point a = new Point(0, 0);
        Point b = new Point(5, 0);

        Assert.assertEquals(0.0, CircleMath.intersectionArea(a, b, 1, 2), DELTA);
    }

    @Test
    public void testTangentIntersection() {
        Point a = new Point(0, 0);
        Point b = new Point(2, 0);

        Assert.assertEquals(0.0, CircleMath.intersectionArea(a, b, 1, 1), DELTA);
    }

    @Test
    public void testCompletelyContained() {
        Point a = new Point(297, 212);
        Point b = new Point(295, 213);

        Assert.assertEquals(CircleMath.area(5), CircleMath.intersectionArea(a, b, 8, 5), DELTA);
    }


}

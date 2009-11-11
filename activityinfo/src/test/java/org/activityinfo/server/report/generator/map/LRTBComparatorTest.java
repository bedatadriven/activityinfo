/*
 * This file is part of ActivityInfo.
 *
 * ActivityInfo is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * ActivityInfo is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with ActivityInfo.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Copyright 2009 Alex Bertram and contributors.
 */

package org.activityinfo.server.report.generator.map;

import junit.framework.Assert;
import org.activityinfo.shared.report.content.MapMarker;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Alex Bertram
 */
public class LRTBComparatorTest {

    @Test
    public void testComparator() {

        MapMarker ul = new MapMarker();
        ul.setX(1);
        ul.setY(1);

        MapMarker ul2 = new MapMarker();
        ul2.setX(60);
        ul2.setY(0);

        MapMarker c = new MapMarker();
        c.setX(30);
        c.setY(50);

        MapMarker ll = new MapMarker();
        ll.setX(0);
        ll.setY(80);

        List<MapMarker> list = new ArrayList<MapMarker>();
        list.add(ll);
        list.add(c);
        list.add(ul2);
        list.add(ul);

        Collections.sort(list, new MapMarker.LRTBComparator());

        for(MapMarker marker : list) {
            System.out.println(marker.toString());
        }

        Assert.assertEquals(ul, list.get(0));
        Assert.assertEquals(ul2, list.get(1));
        Assert.assertEquals(c, list.get(2));
        Assert.assertEquals(ll, list.get(3));


    }
}

/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.server.report.generator.map;

import junit.framework.Assert;
import org.junit.Test;
import org.sigmah.shared.report.content.MapMarker;

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

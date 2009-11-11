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
import org.activityinfo.server.domain.SiteData;
import org.activityinfo.shared.domain.SiteColumn;
import org.activityinfo.shared.report.content.LatLng;
import org.activityinfo.shared.report.content.MapContent;
import org.activityinfo.shared.report.content.PieMapMarker;
import org.activityinfo.shared.report.model.BubbleMapLayer;
import org.activityinfo.shared.report.model.Dimension;
import org.activityinfo.shared.report.model.DimensionType;
import org.activityinfo.shared.report.model.MapElement;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Alex Bertram
 */
public class PieMapMarkerTest {


    @Test
    public void testPies() {

        Dimension dim = new Dimension(DimensionType.Indicator);
        dim.setCategoryColor(101, 255);
        dim.setCategoryColor(102, 0x00FF00);
        dim.setCategoryColor(103, 0x0000FF);

        SiteData site1 = new SiteData();
        site1.setValue(SiteColumn.id,  1);
        site1.setValue(SiteColumn.x, 0d);
        site1.setValue(SiteColumn.y, 0d);
        site1.setIndicatorValue(101, 50d);
        site1.setIndicatorValue(102, 40d);
        site1.setIndicatorValue(103, 10d);

        List<SiteData> sites = new ArrayList<SiteData>();
        sites.add(site1);

        BubbleMapLayer layer = new BubbleMapLayer();
        layer.setPie(true);
        layer.addIndicator(101);
        layer.addIndicator(102);
        layer.addIndicator(103);
        layer.getColorDimensions().add(dim);

        MapElement mapElement = new MapElement();
        mapElement.addLayer(layer);

        MapContent content = new MapContent();

        TiledMap map = new TiledMap(640, 480, new LatLng(0, 0), 6);

        BubbleLayerGenerator gtor = new BubbleLayerGenerator(mapElement, layer);
        gtor.generate(sites, map, content);

        Assert.assertEquals(1, content.getMarkers().size());

        PieMapMarker marker = (PieMapMarker) content.getMarkers().get(0);
        Assert.assertEquals(3, marker.getSlices().size());
    }
}

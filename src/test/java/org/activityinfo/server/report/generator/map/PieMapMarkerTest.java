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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import junit.framework.Assert;

import org.activityinfo.server.database.hibernate.entity.Indicator;
import org.activityinfo.shared.dto.SiteDTO;
import org.activityinfo.shared.report.content.AiLatLng;
import org.activityinfo.shared.report.content.MapContent;
import org.activityinfo.shared.report.content.PieMapMarker;
import org.activityinfo.shared.report.model.Dimension;
import org.activityinfo.shared.report.model.DimensionType;
import org.activityinfo.shared.report.model.MapReportElement;
import org.activityinfo.shared.report.model.layers.PiechartMapLayer;
import org.junit.Test;

import com.google.common.collect.Maps;

public class PieMapMarkerTest {

    @Test
    public void testPies() {

        Dimension dimension = new Dimension(DimensionType.Indicator);
        dimension.setCategoryColor(101, 255);
        dimension.setCategoryColor(102, 0x00FF00);
        dimension.setCategoryColor(103, 0x0000FF);

        SiteDTO site1 = new SiteDTO();
        site1.setId(1);
        site1.setX(0d);
        site1.setY(0d);
        site1.setIndicatorValue(101, 50d);
        site1.setIndicatorValue(102, 40d);
        site1.setIndicatorValue(103, 10d);

        List<SiteDTO> sites = new ArrayList<SiteDTO>();
        sites.add(site1);

        PiechartMapLayer layer = new PiechartMapLayer();
        layer.addIndicatorId(101);
        layer.addIndicatorId(102);
        layer.addIndicatorId(103);
        // layer.getColorDimensions().add(dimension);

        MapReportElement mapElement = new MapReportElement();
        mapElement.addLayer(layer);

        MapContent content = new MapContent();

        TiledMap map = new TiledMap(640, 480, new AiLatLng(0, 0), 6);

        
        Map<Integer, Indicator> indicators = Maps.newHashMap();
        indicators.put(101, new Indicator());
        indicators.put(102, new Indicator());
        indicators.put(103, new Indicator());
        
        PiechartLayerGenerator generator = new PiechartLayerGenerator(layer, indicators);
        generator.setSites(sites);
        generator.generate(map, content);

        Assert.assertEquals(1, content.getMarkers().size());

        PieMapMarker marker = (PieMapMarker) content.getMarkers().get(0);
        Assert.assertEquals(3, marker.getSlices().size());
    }
}

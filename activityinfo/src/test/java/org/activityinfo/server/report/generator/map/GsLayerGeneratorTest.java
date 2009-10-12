package org.activityinfo.server.report.generator.map;

import org.activityinfo.shared.domain.SiteColumn;
import org.activityinfo.shared.report.content.LatLng;
import org.activityinfo.shared.report.content.MapContent;
import org.activityinfo.shared.report.content.SiteData;
import org.activityinfo.shared.report.model.*;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
/*
 * @author Alex Bertram
 */

public class GsLayerGeneratorTest {


    @Test
    public void testColorByIndicators() {

        MapElement element = new MapElement();

        GsMapLayer layer = new GsMapLayer();
        layer.setClustered(true);
        layer.addIndicator(101);
        layer.addIndicator(102);

        Dimension dim = new Dimension(DimensionType.Indicator);
        dim.setProperties(101, CategoryProperties.Color(0, 0, 255));
        dim.setProperties(102, CategoryProperties.Color(0, 0, 0));
        layer.getColorDimensions().add(dim);

        List<SiteData> sites = new ArrayList<SiteData>();

        SiteData site1 = new SiteData();
        site1.setValue(SiteColumn.x, 29.3 );
        site1.setValue(SiteColumn.y, -1.5);
        site1.indicatorValues.put(101, 35.0);
        site1.indicatorValues.put(102, 55.0);
        sites.add(site1);

        element.addLayer(layer);

        TiledMap map = new TiledMap(640, 480, new LatLng(-1.5, 29.3), 9);

        MapContent content = new MapContent();

        GsLayerGenerator gtor = new GsLayerGenerator(element, layer);
        gtor.generate(sites, map, content);

        Assert.assertEquals("marker count", 2, content.getMarkers().size());
        Assert.assertEquals(255, content.getMarkers().get(0).getColor());
        Assert.assertEquals(0, content.getMarkers().get(1).getColor());
    }

    @Test
    public void testColorByPartner() {

        MapElement element = new MapElement();

        GsMapLayer layer = new GsMapLayer();
        layer.setClustered(true);
        layer.addIndicator(101);

        Dimension dim = new Dimension(DimensionType.Partner);
        dim.setProperties(301, CategoryProperties.Color(0, 0, 255));
        dim.setProperties(302, CategoryProperties.Color(0, 0, 0));
        layer.getColorDimensions().add(dim);

        List<SiteData> sites = new ArrayList<SiteData>();

        SiteData site1 = new SiteData();
        site1.setValue(SiteColumn.partner_id, 301);
        site1.setValue(SiteColumn.x, 29.3 );
        site1.setValue(SiteColumn.y, -1.5);
        site1.indicatorValues.put(101, 35.0);
        sites.add(site1);

        SiteData site2 = new SiteData();
        site2.setValue(SiteColumn.partner_id, 302);
        site2.setValue(SiteColumn.x, 29.3 );
        site2.setValue(SiteColumn.y, -1.5);
        site2.indicatorValues.put(101, 70.0);
        sites.add(site2);

        SiteData site3 = new SiteData();
        site3.setValue(SiteColumn.partner_id, 302);
        site3.setValue(SiteColumn.x, 29.3 );
        site3.setValue(SiteColumn.y, -1.5);
        site3.indicatorValues.put(101, 100.0);
        sites.add(site3);

        element.addLayer(layer);

        TiledMap map = new TiledMap(640, 480, new LatLng(-1.5, 29.3), 9);

        MapContent content = new MapContent();

        GsLayerGenerator gtor = new GsLayerGenerator(element, layer);
        gtor.generate(sites, map, content);

        Assert.assertEquals("marker count", 2, content.getMarkers().size());
        Assert.assertEquals(255, content.getMarkers().get(0).getColor());
        Assert.assertEquals(0, content.getMarkers().get(1).getColor());
    }


}

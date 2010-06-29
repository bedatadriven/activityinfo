package org.sigmah.server.report.generator.map;

import org.junit.Assert;
import org.junit.Test;
import org.sigmah.server.dao.SiteTableColumn;
import org.sigmah.server.domain.SiteData;
import org.sigmah.shared.report.content.BubbleMapMarker;
import org.sigmah.shared.report.content.LatLng;
import org.sigmah.shared.report.content.MapContent;
import org.sigmah.shared.report.model.*;

import java.util.ArrayList;
import java.util.List;
/*
 * @author Alex Bertram
 */
public class BubbleGeneratorTest {


    @Test
    public void testColorByIndicators() {

        MapElement element = new MapElement();

        BubbleMapLayer layer = new BubbleMapLayer();
        layer.setClustered(true);
        layer.addIndicator(101);
        layer.addIndicator(102);

        Dimension dim = new Dimension(DimensionType.Indicator);
        dim.setProperties(101, CategoryProperties.Color(0, 0, 255));
        dim.setProperties(102, CategoryProperties.Color(0, 0, 0));
        layer.getColorDimensions().add(dim);

        List<SiteData> sites = new ArrayList<SiteData>();

        SiteData site1 = new SiteData();
        site1.setValue(SiteTableColumn.id, 1) ;
        site1.setValue(SiteTableColumn.x, 29.3 );
        site1.setValue(SiteTableColumn.y, -1.5);
        site1.indicatorValues.put(101, 35.0);
        site1.indicatorValues.put(102, 55.0);
        sites.add(site1);

        element.addLayer(layer);

        TiledMap map = new TiledMap(640, 480, new LatLng(-1.5, 29.3), 9);

        MapContent content = new MapContent();

        BubbleLayerGenerator gtor = new BubbleLayerGenerator(element, layer);
        gtor.generate(sites, map, content);

        Assert.assertEquals("marker count", 2, content.getMarkers().size());
        Assert.assertEquals(255, ((BubbleMapMarker) content.getMarkers().get(0)).getColor());
        Assert.assertEquals(0, ((BubbleMapMarker) content.getMarkers().get(1)).getColor());
        Assert.assertTrue(((BubbleMapMarker) content.getMarkers().get(0)).getRadius() >= layer.getMinRadius());
    }

    @Test
    public void testColorByPartner() {

        MapElement element = new MapElement();

        BubbleMapLayer layer = new BubbleMapLayer();
        layer.setClustered(true);
        layer.addIndicator(101);

        Dimension dim = new Dimension(DimensionType.Partner);
        dim.setProperties(301, CategoryProperties.Color(0, 0, 255));
        dim.setProperties(302, CategoryProperties.Color(0, 0, 0));
        layer.getColorDimensions().add(dim);

        List<SiteData> sites = new ArrayList<SiteData>();

        SiteData site1 = new SiteData();
        site1.setValue(SiteTableColumn.id, 1) ;
        site1.setValue(SiteTableColumn.partner_id, 301);
        site1.setValue(SiteTableColumn.x, 29.3 );
        site1.setValue(SiteTableColumn.y, -1.5);
        site1.indicatorValues.put(101, 35.0);
        sites.add(site1);

        SiteData site2 = new SiteData();
        site2.setValue(SiteTableColumn.id, 2) ;
        site2.setValue(SiteTableColumn.partner_id, 302);
        site2.setValue(SiteTableColumn.x, 29.3 );
        site2.setValue(SiteTableColumn.y, -1.5);
        site2.indicatorValues.put(101, 70.0);
        sites.add(site2);

        SiteData site3 = new SiteData();
        site3.setValue(SiteTableColumn.id, 3) ;
        site3.setValue(SiteTableColumn.partner_id, 302);
        site3.setValue(SiteTableColumn.x, 29.3 );
        site3.setValue(SiteTableColumn.y, -1.5);
        site3.indicatorValues.put(101, 100.0);
        sites.add(site3);

        element.addLayer(layer);

        TiledMap map = new TiledMap(640, 480, new LatLng(-1.5, 29.3), 9);

        MapContent content = new MapContent();

        BubbleLayerGenerator gtor = new BubbleLayerGenerator(element, layer);
        gtor.generate(sites, map, content);

        Assert.assertEquals("marker count", 2, content.getMarkers().size());
        Assert.assertEquals(255, ((BubbleMapMarker) content.getMarkers().get(0)).getColor());
        Assert.assertEquals(0, ((BubbleMapMarker) content.getMarkers().get(1)).getColor());
    }


}

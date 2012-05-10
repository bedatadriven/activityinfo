/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.activityinfo.server.report.generator.map;

import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

import org.activityinfo.server.report.generator.map.PiechartLayerGenerator;
import org.activityinfo.server.report.generator.map.TiledMap;
import org.activityinfo.shared.dto.SiteDTO;
import org.activityinfo.shared.report.content.AiLatLng;
import org.activityinfo.shared.report.content.MapContent;
import org.activityinfo.shared.report.content.PieMapMarker;
import org.activityinfo.shared.report.model.Dimension;
import org.activityinfo.shared.report.model.DimensionType;
import org.activityinfo.shared.report.model.MapReportElement;
import org.activityinfo.shared.report.model.layers.PiechartMapLayer;
import org.junit.Test;

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
        //layer.getColorDimensions().add(dimension);

        MapReportElement mapElement = new MapReportElement();
        mapElement.addLayer(layer);

        MapContent content = new MapContent();

        TiledMap map = new TiledMap(640, 480, new AiLatLng(0, 0), 6);

        PiechartLayerGenerator generator = new PiechartLayerGenerator(layer, sites);
        generator.generate(map, content);

        Assert.assertEquals(1, content.getMarkers().size());

        PieMapMarker marker = (PieMapMarker) content.getMarkers().get(0);
        Assert.assertEquals(3, marker.getSlices().size());
    }
}

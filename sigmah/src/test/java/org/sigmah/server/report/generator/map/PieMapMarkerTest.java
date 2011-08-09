/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.server.report.generator.map;

import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

import org.junit.Test;
import org.sigmah.shared.dao.SiteTableColumn;
import org.sigmah.shared.report.content.AiLatLng;
import org.sigmah.shared.report.content.MapContent;
import org.sigmah.shared.report.content.PieMapMarker;
import org.sigmah.shared.report.model.Dimension;
import org.sigmah.shared.report.model.DimensionType;
import org.sigmah.shared.report.model.MapReportElement;
import org.sigmah.shared.report.model.SiteData;
import org.sigmah.shared.report.model.layers.PiechartMapLayer;

public class PieMapMarkerTest {

    @Test
    public void testPies() {

        Dimension dimension = new Dimension(DimensionType.Indicator);
        dimension.setCategoryColor(101, 255);
        dimension.setCategoryColor(102, 0x00FF00);
        dimension.setCategoryColor(103, 0x0000FF);

        SiteData site1 = new SiteData();
        site1.setValue(SiteTableColumn.id,  1);
        site1.setValue(SiteTableColumn.x, 0d);
        site1.setValue(SiteTableColumn.y, 0d);
        site1.setIndicatorValue(101, 50d);
        site1.setIndicatorValue(102, 40d);
        site1.setIndicatorValue(103, 10d);

        List<SiteData> sites = new ArrayList<SiteData>();
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

        PiechartLayerGenerator generator = new PiechartLayerGenerator(mapElement, layer);
        generator.generate(sites, map, content);

        Assert.assertEquals(1, content.getMarkers().size());

        PieMapMarker marker = (PieMapMarker) content.getMarkers().get(0);
        Assert.assertEquals(3, marker.getSlices().size());
    }
}

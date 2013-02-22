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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import org.activityinfo.server.command.CommandTestCase2;
import org.activityinfo.server.database.OnDataSet;
import org.activityinfo.server.database.TestDatabaseModule;
import org.activityinfo.server.report.generator.MapGenerator;
import org.activityinfo.server.report.renderer.itext.PdfReportRenderer;
import org.activityinfo.server.report.renderer.itext.TestGeometry;
import org.activityinfo.shared.command.GenerateElement;
import org.activityinfo.shared.dto.IndicatorDTO;
import org.activityinfo.shared.map.GoogleBaseMap;
import org.activityinfo.shared.report.content.AdminMarker;
import org.activityinfo.shared.report.content.AdminOverlay;
import org.activityinfo.shared.report.content.AiLatLng;
import org.activityinfo.shared.report.content.FilterDescription;
import org.activityinfo.shared.report.content.MapContent;
import org.activityinfo.shared.report.content.PolygonLegend;
import org.activityinfo.shared.report.model.MapReportElement;
import org.activityinfo.shared.report.model.layers.PolygonMapLayer;
import org.activityinfo.test.InjectionSupport;
import org.activityinfo.test.Modules;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.google.common.collect.Lists;
import com.google.inject.Inject;

@RunWith(InjectionSupport.class)
@Modules({ TestDatabaseModule.class })
@OnDataSet("/dbunit/polygons.db.xml")
public class PolygonGeneratorTest extends CommandTestCase2 {

    @Inject
    private MapGenerator generator;

    @Before
    public void setUpDirs() {
        new File("target/report-tests").mkdirs();
    }

    @Test
    public void basicTest() throws IOException {

        PolygonMapLayer layer = new PolygonMapLayer();
        layer.addIndicatorId(1);
        layer.setAdminLevelId(1383);

        MapReportElement map = new MapReportElement();
        map.addLayer(layer);

        MapContent content = execute(new GenerateElement<MapContent>(map));
        map.setContent(content);

        FileOutputStream fos = new FileOutputStream(
            "target/report-tests/polygon.pdf");
        PdfReportRenderer pdfr = new PdfReportRenderer(TestGeometry.get(), "");
        pdfr.render(map, fos);
        fos.close();
    }

    @Test
    public void polygonWithHole() throws IOException {

        AdminMarker marker = new AdminMarker();
        marker.setAdminEntityId(1930);
        marker.setColor("#FFBBBB");

        AdminOverlay overlay = new AdminOverlay(1383);
        overlay.setOutlineColor("#FF0000");
        overlay.addPolygon(marker);

        PolygonMapLayer layer = new PolygonMapLayer();
        layer.addIndicatorId(1);
        layer.setAdminLevelId(1383);

        MapContent content = new MapContent();
        content.setZoomLevel(8);
        content.setBaseMap(GoogleBaseMap.ROADMAP);
        content.setCenter(new AiLatLng(12.60500192642215, -7.98924994468689));
        content.getAdminOverlays().add(overlay);
        content.setFilterDescriptions(new ArrayList<FilterDescription>());

        PolygonLegend.ColorClass clazz1 = new PolygonLegend.ColorClass(1, 53.6,
            "0000FF");
        PolygonLegend.ColorClass clazz2 = new PolygonLegend.ColorClass(600,
            600, "FF0000");

        PolygonLegend legend = new PolygonLegend(layer, Lists.newArrayList(
            clazz1, clazz2));

        content.getLegends().add(legend);

        IndicatorDTO indicator = new IndicatorDTO();
        indicator.setId(1);
        indicator.setName("Indicator Test");

        content.getIndicators().add(indicator);

        MapReportElement map = new MapReportElement();
        map.addLayer(layer);
        map.setContent(content);

        FileOutputStream fos = new FileOutputStream(
            "target/report-tests/polygon-hole.pdf");
        PdfReportRenderer pdfr = new PdfReportRenderer(TestGeometry.get(), "");
        pdfr.render(map, fos);
        fos.close();
    }
}

package org.activityinfo.server.report.renderer.ppt;

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
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collections;

import org.activityinfo.server.report.renderer.itext.TestGeometry;
import org.activityinfo.shared.map.BaseMap;
import org.activityinfo.shared.map.GoogleBaseMap;
import org.activityinfo.shared.map.TileBaseMap;
import org.activityinfo.shared.report.content.AiLatLng;
import org.activityinfo.shared.report.content.MapContent;
import org.activityinfo.shared.report.model.MapReportElement;
import org.junit.Before;
import org.junit.Test;

public class PPTMapRendererTest {

    private TileBaseMap referenceBaseMap;

    @Before
    public void setUpDirs() {
        new File("target/report-tests").mkdirs();

        referenceBaseMap = new TileBaseMap();
        referenceBaseMap
            .setTileUrlPattern("http://mt{s}.aimaps.net/admin/v1/z{z}/{x}x{y}.png");
        referenceBaseMap.setName("Administrative Map");

    }

    @Test
    public void renderPPtScaled2() throws IOException {

        MapReportElement map = createMap(GoogleBaseMap.ROADMAP);
        renderPPT(map, "googleBaseMap.ppt");

    }

    @Test
    public void renderGoogleBaseMapsLong() throws IOException {
        MapReportElement map = createMap(GoogleBaseMap.ROADMAP);
        map.setHeight(1024);
        renderPPT(map, "googleBaseMapLong.ppt");

        MapReportElement tiledMap = createMap(referenceBaseMap);
        tiledMap.setHeight(1024);
        renderPPT(tiledMap, "tiledMapLong.ppt");
    }

    @Test
    public void renderTitledMap() throws FileNotFoundException, IOException {
        MapReportElement map = createMap(referenceBaseMap);
        renderPPT(map, "tiledMap.ppt");
    }

    private void renderPPT(MapReportElement map, String filename)
        throws FileNotFoundException,
        IOException {
        FileOutputStream fos = new FileOutputStream("target/report-tests/"
            + filename);

        PPTMapRenderer renderer = new PPTMapRenderer(TestGeometry.get(), "");
        renderer.render(map, fos);

        fos.close();
    }

    private MapReportElement createMap(BaseMap baseMap) {
        MapContent content = new MapContent();
        content.setBaseMap(baseMap);
        content
            .setCenter(new AiLatLng(-0.07965085325106624, 29.326629638671875));
        content.setZoomLevel(10);
        content.setIndicators(Collections.EMPTY_SET);
        content.setMarkers(Collections.EMPTY_LIST);

        MapReportElement element = new MapReportElement();
        element.setContent(content);
        element.setWidth(1022);
        element.setHeight(634);
        return element;
    }

    @Test
    public void renderPPtScaled() throws IOException {

        MapReportElement element = createMap(GoogleBaseMap.ROADMAP);

        FileOutputStream fos = new FileOutputStream(
            "target/report-tests/map-custom-extents.ppt");

        PPTMapRenderer renderer = new PPTMapRenderer(TestGeometry.get(), "");
        renderer.render(element, fos);

        fos.close();
    }
}

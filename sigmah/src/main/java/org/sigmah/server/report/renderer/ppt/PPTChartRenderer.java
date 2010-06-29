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

package org.sigmah.server.report.renderer.ppt;

import org.apache.poi.hslf.model.PPGraphics2D;
import org.apache.poi.hslf.model.ShapeGroup;
import org.apache.poi.hslf.model.Slide;
import org.apache.poi.hslf.usermodel.SlideShow;
import org.sigmah.server.report.renderer.ChartRendererJC;
import org.sigmah.shared.report.model.PivotChartElement;

import java.awt.*;
import java.io.IOException;

/**
 * @author Alex Bertram
 */
public class PPTChartRenderer {


    public void render(PivotChartElement element, SlideShow ppt) throws IOException {

        //add first slide
        Slide slide = ppt.createSlide();

        //define position of the drawing in the slide
        Dimension pageSize = ppt.getPageSize();
        Dimension chartSize = new Dimension(
                (int)(pageSize.getWidth() - 72),
                (int)(pageSize.getHeight() - 183));
        Rectangle bounds = new java.awt.Rectangle(
                new Point(36, 126), chartSize);

        ShapeGroup group = new ShapeGroup();
        group.setAnchor(bounds);

        slide.addShape(group);
        Graphics2D graphics = new PPGraphics2D(group);

        ChartRendererJC jc = new ChartRendererJC();
        jc.render(element, false, graphics, (int)chartSize.getWidth(), (int)chartSize.getHeight(), 72);

    }


}

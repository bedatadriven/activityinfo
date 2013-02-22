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

import java.io.IOException;
import java.io.OutputStream;

import org.activityinfo.server.report.renderer.ChartRendererJC;
import org.activityinfo.shared.report.model.PivotChartReportElement;
import org.apache.poi.hslf.model.PPGraphics2D;
import org.apache.poi.hslf.model.ShapeGroup;
import org.apache.poi.hslf.model.Slide;
import org.apache.poi.hslf.usermodel.SlideShow;

import com.google.code.appengine.awt.Dimension;
import com.google.code.appengine.awt.Graphics2D;
import com.google.code.appengine.awt.Point;
import com.google.code.appengine.awt.Rectangle;

/**
 * @author Alex Bertram
 */
public class PPTChartRenderer {

    public void render(PivotChartReportElement element, OutputStream stream)
        throws IOException {
        // create a new empty slide show
        SlideShow ppt = new SlideShow();
        Dimension pageSize = new Dimension(720, 540); // Onscreen Show (4:5)
        ppt.setPageSize(pageSize);

        render(element, ppt);

        // write to stream
        ppt.write(stream);

    }

    public void render(PivotChartReportElement element, SlideShow ppt)
        throws IOException {

        // add first slide
        Slide slide = ppt.createSlide();

        // define position of the drawing in the slide
        Dimension pageSize = ppt.getPageSize();
        Dimension chartSize = new Dimension(
            (int) (pageSize.getWidth() - 72),
            (int) (pageSize.getHeight() - 183));
        Rectangle bounds = new com.google.code.appengine.awt.Rectangle(
            new Point(36, 126), chartSize);

        ShapeGroup group = new ShapeGroup();
        group.setAnchor(bounds);

        slide.addShape(group);
        Graphics2D graphics = new PPGraphics2D(group);

        ChartRendererJC jc = new ChartRendererJC();
        jc.render(element, false, graphics, (int) chartSize.getWidth(),
            (int) chartSize.getHeight(), 72);

    }

}

/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
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

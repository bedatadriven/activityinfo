/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.activityinfo.server.report.renderer.ppt;

import com.google.code.appengine.awt.Dimension;
import com.google.code.appengine.awt.Graphics2D;
import com.google.code.appengine.awt.Point;
import com.google.code.appengine.awt.Rectangle;
import java.io.IOException;
import java.io.OutputStream;

import org.activityinfo.server.report.renderer.ChartRendererJC;
import org.activityinfo.shared.report.model.PivotChartReportElement;
import org.apache.poi.hslf.model.PPGraphics2D;
import org.apache.poi.hslf.model.ShapeGroup;
import org.apache.poi.hslf.model.Slide;
import org.apache.poi.hslf.usermodel.SlideShow;

/**
 * @author Alex Bertram
 */
public class PPTChartRenderer {


    public void render(PivotChartReportElement element, OutputStream stream) throws IOException {
        //create a new empty slide show
        SlideShow ppt = new SlideShow();
        Dimension pageSize  = new Dimension(720, 540); // Onscreen Show (4:5)
        ppt.setPageSize(pageSize);

        render(element, ppt);

        // write to stream
        ppt.write(stream);

    }
    public void render(PivotChartReportElement element, SlideShow ppt) throws IOException {

        //add first slide
        Slide slide = ppt.createSlide();

        //define position of the drawing in the slide
        Dimension pageSize = ppt.getPageSize();
        Dimension chartSize = new Dimension(
                (int)(pageSize.getWidth() - 72),
                (int)(pageSize.getHeight() - 183));
        Rectangle bounds = new com.google.code.appengine.awt.Rectangle(
                new Point(36, 126), chartSize);

        ShapeGroup group = new ShapeGroup();
        group.setAnchor(bounds);

        slide.addShape(group);
        Graphics2D graphics = new PPGraphics2D(group);

        ChartRendererJC jc = new ChartRendererJC();
        jc.render(element, false, graphics, (int)chartSize.getWidth(), (int)chartSize.getHeight(), 72);

    }


}

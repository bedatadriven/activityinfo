/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.activityinfo.server.report.renderer;

import org.activityinfo.server.report.renderer.excel.ExcelMapDataExporter;
import org.activityinfo.server.report.renderer.excel.ExcelReportRenderer;
import org.activityinfo.server.report.renderer.image.ImageReportRenderer;
import org.activityinfo.server.report.renderer.itext.HtmlReportRenderer;
import org.activityinfo.server.report.renderer.itext.PdfReportRenderer;
import org.activityinfo.server.report.renderer.itext.RtfReportRenderer;
import org.activityinfo.server.report.renderer.ppt.PPTRenderer;
import org.activityinfo.shared.command.RenderElement;

import com.google.inject.Inject;
import com.google.inject.Injector;

/**
 * @author Alex Bertram
 */
public class RendererFactory {

    private final Injector injector;

    @Inject
    public RendererFactory(Injector injector) {
        this.injector = injector;
    }

    public Renderer get(RenderElement.Format format) {

        switch(format) {
            case PowerPoint:
                return injector.getInstance(PPTRenderer.class);
            case Word:
                return injector.getInstance(RtfReportRenderer.class);
            case Excel:
                return injector.getInstance(ExcelReportRenderer.class);
            case PDF:
                return injector.getInstance(PdfReportRenderer.class);
            case PNG:
                return injector.getInstance(ImageReportRenderer.class);
            case Excel_Data:
                return injector.getInstance(ExcelMapDataExporter.class);
            case HTML:
            	return injector.getInstance(HtmlReportRenderer.class);
        }

        throw new IllegalArgumentException();
    }
}

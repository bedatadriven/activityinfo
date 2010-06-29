/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.server.report.renderer;

import com.google.inject.Inject;
import com.google.inject.Injector;
import org.sigmah.server.report.renderer.excel.ExcelMapDataExporter;
import org.sigmah.server.report.renderer.excel.ExcelReportRenderer;
import org.sigmah.server.report.renderer.image.ImageReportRenderer;
import org.sigmah.server.report.renderer.itext.PdfReportRenderer;
import org.sigmah.server.report.renderer.itext.RtfReportRenderer;
import org.sigmah.server.report.renderer.ppt.PPTRenderer;
import org.sigmah.shared.command.RenderElement;

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
        }

        throw new IllegalArgumentException();
    }
}

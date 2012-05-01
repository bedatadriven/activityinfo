/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.server.report.renderer.ppt;

import java.io.IOException;
import java.io.OutputStream;

import org.apache.poi.hslf.usermodel.SlideShow;
import org.sigmah.server.report.renderer.Renderer;
import org.sigmah.shared.report.model.MapReportElement;
import org.sigmah.shared.report.model.PivotChartReportElement;
import org.sigmah.shared.report.model.Report;
import org.sigmah.shared.report.model.ReportElement;

import com.google.inject.Inject;

/**
 * @author Alex Bertram
 */
public class PPTRenderer implements Renderer {

    private final PPTMapRenderer mapRenderer;
    private final PPTChartRenderer chartRenderer;

    @Inject
    public PPTRenderer(PPTMapRenderer mapRenderer, PPTChartRenderer chartRenderer) {
        this.mapRenderer = mapRenderer;
        this.chartRenderer = chartRenderer;
    }

    public void render(ReportElement element, OutputStream os) throws IOException {

        if(element instanceof Report) {
            SlideShow ppt = new SlideShow();
            Report report = (Report) element;
            for(ReportElement child : report.getElements()) {
                if(child instanceof MapReportElement) {
                    mapRenderer.render((MapReportElement) child, ppt);
                } else if(child instanceof PivotChartReportElement) {
                    chartRenderer.render((PivotChartReportElement) child, ppt);
                }
            }
            ppt.write(os);

        } else if(element instanceof MapReportElement) {
            mapRenderer.render((MapReportElement) element, os);
        } else if(element instanceof PivotChartReportElement) {
        	chartRenderer.render((PivotChartReportElement)element, os);
        }
    }

    @Override
    public String getMimeType() {
        return "application/vnd.ms-powerpoint";
    }

    @Override
    public String getFileSuffix() {
        return ".ppt";
    }
}

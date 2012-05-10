/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.activityinfo.server.report.renderer.image;

import java.io.IOException;
import java.io.OutputStream;

import org.activityinfo.server.report.renderer.ChartRendererJC;
import org.activityinfo.server.report.renderer.Renderer;
import org.activityinfo.shared.report.model.MapReportElement;
import org.activityinfo.shared.report.model.PivotChartReportElement;
import org.activityinfo.shared.report.model.ReportElement;

import com.google.inject.Inject;

/*
 * @author Alex Bertram
 */
public class ImageReportRenderer implements Renderer {


    private final ImageMapRenderer mapRenderer;
    private final ChartRendererJC chartRenderer;

    @Inject
    public ImageReportRenderer(ImageMapRenderer renderer, ChartRendererJC chartRendererJC) {
        this.mapRenderer = renderer;
        this.chartRenderer = chartRendererJC;
    }

    public void render(ReportElement element, OutputStream os) throws IOException {
        // TODO: support for other types?

        if(element instanceof MapReportElement) {
            mapRenderer.render((MapReportElement) element, os);
        } else if(element instanceof PivotChartReportElement) {
        	chartRenderer.render((PivotChartReportElement)element, os);
        }

    }

    @Override
    public String getMimeType() {
        return "image/png";
    }

    @Override
    public String getFileSuffix() {
        return ".png";
    }
}

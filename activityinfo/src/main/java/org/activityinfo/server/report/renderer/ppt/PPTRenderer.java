package org.activityinfo.server.report.renderer.ppt;

import com.google.inject.Inject;
import org.activityinfo.server.report.renderer.Renderer;
import org.activityinfo.shared.report.model.MapElement;
import org.activityinfo.shared.report.model.PivotChartElement;
import org.activityinfo.shared.report.model.Report;
import org.activityinfo.shared.report.model.ReportElement;
import org.apache.poi.hslf.usermodel.SlideShow;

import java.io.IOException;
import java.io.OutputStream;

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
                if(child instanceof MapElement) {
                    mapRenderer.render((MapElement) child, ppt);
                } else if(child instanceof PivotChartElement) {
                    chartRenderer.render((PivotChartElement) child, ppt);
                }
            }
            ppt.write(os);

        } else if(element instanceof MapElement) {
            mapRenderer.render((MapElement) element, os);
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

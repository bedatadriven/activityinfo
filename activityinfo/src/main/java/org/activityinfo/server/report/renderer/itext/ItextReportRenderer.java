package org.activityinfo.server.report.renderer.itext;

import com.google.inject.Inject;
import com.lowagie.text.DocWriter;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import org.activityinfo.server.report.renderer.Renderer;
import org.activityinfo.shared.report.model.*;

import java.io.IOException;
import java.io.OutputStream;
/*
 * @author Alex Bertram
 */

public abstract class ItextReportRenderer implements Renderer {

    private final ItextPivotTableRenderer pivotTableRenderer;
    private final ItextChartRenderer chartRenderer;
    private final ItextMapRenderer mapRenderer;

    @Inject
    public ItextReportRenderer(ItextPivotTableRenderer pivotTableRenderer, ItextChartRenderer chartRenderer, ItextMapRenderer mapRenderer) {
        this.pivotTableRenderer = pivotTableRenderer;
        this.chartRenderer = chartRenderer;
        this.mapRenderer = mapRenderer;
    }

    private void renderElement(DocWriter writer, ReportElement element, Document document) {

        if(element instanceof PivotTableElement) {
            pivotTableRenderer.render(writer, (PivotTableElement) element, document);
        } else if(element instanceof PivotChartElement) {
            chartRenderer.render(writer, (PivotChartElement) element, document);
        } else if(element instanceof MapElement) {
            mapRenderer.render(writer, (MapElement) element, document);
        }
    }

    public void render(ReportElement element, OutputStream os) throws IOException {

        try {
            Document document = new Document();
            DocWriter writer = createWriter(document, os);
            document.open();

            if(element instanceof Report) {
                Report report = (Report) element;
                document.add(ThemeHelper.reportTitle(report.getTitle()));

                for(ReportElement childElement : report.getElements()) {
                    renderElement(writer, childElement, document);
                }
            }

            document.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    protected abstract DocWriter createWriter(Document document, OutputStream os) throws DocumentException;
}

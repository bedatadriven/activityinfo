package org.activityinfo.server.report.renderer.itext;

import com.google.inject.Inject;
import com.lowagie.text.DocWriter;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.pdf.PdfWriter;

import java.io.OutputStream;
/*
 * @author Alex Bertram
 */

public class PdfReportRenderer extends ItextReportRenderer {

    @Inject
    public PdfReportRenderer(ItextPivotTableRenderer pivotTableRenderer,
                             ItextChartRenderer chartRenderer, ItextMapRenderer mapRenderer) {
        super(pivotTableRenderer, chartRenderer, mapRenderer);
    }

    @Override
    protected DocWriter createWriter(Document document, OutputStream os) throws DocumentException {
        PdfWriter writer = PdfWriter.getInstance(document, os);
        writer.setStrictImageSequence(true);

        return writer;
    }

    @Override
    public String getMimeType() {
        return "application/pdf";
    }

    @Override
    public String getFileSuffix() {
        return ".pdf";
    }
}

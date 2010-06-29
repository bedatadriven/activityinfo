package org.sigmah.server.report.renderer.itext;

import com.google.inject.Inject;
import com.lowagie.text.DocWriter;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.pdf.PdfWriter;

import java.io.OutputStream;


/**
 * iText ReportRenderer targeting PDF output
 *
 * @author Alex Bertram
 */
public class PdfReportRenderer extends ItextReportRenderer {

    @Inject
    public PdfReportRenderer(ItextPivotTableRenderer pivotTableRenderer,
                             ItextChartRenderer chartRenderer, ItextMapRenderer mapRenderer,
                             ItextTableRenderer tableRenderer) {
        super(pivotTableRenderer, chartRenderer, mapRenderer, tableRenderer);
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

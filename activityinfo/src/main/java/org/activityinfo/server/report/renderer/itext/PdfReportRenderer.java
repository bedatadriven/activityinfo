package org.activityinfo.server.report.renderer.itext;

import org.activityinfo.shared.report.model.ReportElement;

import java.io.OutputStream;
import java.io.IOException;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.DocWriter;
import com.lowagie.text.pdf.PdfWriter;
import com.google.inject.Inject;
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


}

package org.activityinfo.server.report.renderer.itext;

import org.activityinfo.server.report.renderer.Renderer;
import org.activityinfo.shared.report.model.*;
import com.google.inject.Inject;
import com.lowagie.text.Document;
import com.lowagie.text.Paragraph;
import com.lowagie.text.PageSize;
import com.lowagie.text.DocWriter;
import com.lowagie.text.rtf.RtfWriter2;
import com.lowagie.text.pdf.PdfWriter;

import java.io.OutputStream;
import java.io.IOException;
/*
 * @author Alex Bertram
 */

public class RtfReportRenderer extends ItextReportRenderer {


    @Inject
    public RtfReportRenderer(ItextPivotTableRenderer pivotTableRenderer,
                             ItextChartRenderer chartRenderer, ItextMapRenderer mapRenderer) {
        super(pivotTableRenderer, chartRenderer, mapRenderer);
    }


    @Override
    protected DocWriter createWriter(Document document, OutputStream os) {
        return RtfWriter2.getInstance(document, os);
    }
}

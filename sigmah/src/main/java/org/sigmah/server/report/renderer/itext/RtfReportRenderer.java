/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.server.report.renderer.itext;

import com.google.inject.Inject;
import com.lowagie.text.DocWriter;
import com.lowagie.text.Document;
import com.lowagie.text.rtf.RtfWriter2;

import java.io.OutputStream;

/**
 * iText ReportRenderer targeting Rich Text Format (RTF) output
 *
 * @author Alex Bertram
 */
public class RtfReportRenderer extends ItextReportRenderer {

    @Inject
    public RtfReportRenderer(ItextPivotTableRenderer pivotTableRenderer,
                             ItextChartRenderer chartRenderer, ItextMapRenderer mapRenderer,
                             ItextTableRenderer tableRenderer,
                             ItextStaticRenderer staticRenderer) {
        super(pivotTableRenderer, chartRenderer, mapRenderer, tableRenderer, staticRenderer);
    }

    @Override
    protected DocWriter createWriter(Document document, OutputStream os) {
        return RtfWriter2.getInstance(document, os);
    }

    @Override
    public String getMimeType() {
        return "application/rtf";
    }

    @Override
    public String getFileSuffix() {
        return ".rtf";
    }
}

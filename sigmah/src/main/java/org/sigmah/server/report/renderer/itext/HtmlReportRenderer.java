/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.server.report.renderer.itext;

import com.google.inject.Inject;
import com.lowagie.text.DocWriter;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.html.HtmlWriter;

import java.io.OutputStream;

public class HtmlReportRenderer extends ItextReportRenderer {

    @Inject
    public HtmlReportRenderer(ItextPivotTableRenderer pivotTableRenderer, ItextChartRenderer chartRenderer, ItextMapRenderer mapRenderer, ItextTableRenderer tableRenderer) {
        super(pivotTableRenderer, chartRenderer, mapRenderer, tableRenderer);
    }

    @Override
    protected DocWriter createWriter(Document document, OutputStream os) throws DocumentException {
        HtmlWriter writer = HtmlWriter.getInstance(document, os);
        writer.setImagepath("c:\\temp");

        return writer;
    }

    @Override
    public String getMimeType() {
        return "text/html";
    }

    @Override
    public String getFileSuffix() {
        return ".html";
    }
}

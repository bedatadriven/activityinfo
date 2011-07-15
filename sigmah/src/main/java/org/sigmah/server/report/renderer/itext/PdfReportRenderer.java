/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.server.report.renderer.itext;

import java.io.OutputStream;

import org.sigmah.server.report.generator.MapIconPath;
import org.sigmah.server.report.renderer.ChartRendererJC;

import com.google.inject.Inject;
import com.lowagie.text.DocWriter;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.HeaderFooter;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfWriter;


/**
 * iText ReportRenderer targeting PDF output
 *
 * @author Alex Bertram
 */
public class PdfReportRenderer extends ItextReportRenderer {

    @Inject
    public PdfReportRenderer(@MapIconPath String mapIconPath) {
        super(new ItextRtfChartRenderer(new ChartRendererJC()), mapIconPath);
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

	@Override
	protected void renderFooter(Document document) {
		HeaderFooter footer = new HeaderFooter(new Phrase("Page ", ThemeHelper.footerFont()), true);
		document.setFooter(footer);		
	}
    
    
}

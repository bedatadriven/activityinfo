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
import com.lowagie.text.HeaderFooter;
import com.lowagie.text.Phrase;
import com.lowagie.text.rtf.RtfWriter2;

/**
 * iText ReportRenderer targeting Rich Text Format (RTF) output
 *
 * @author Alex Bertram
 */
public class RtfReportRenderer extends ItextReportRenderer {

    @Inject
    public RtfReportRenderer(@MapIconPath String mapIconPath) {
        super(new ItextRtfChartRenderer(new ChartRendererJC()), mapIconPath);
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

	@Override
	protected void renderFooter(Document document) {
		HeaderFooter footer = new HeaderFooter(new Phrase("Page", ThemeHelper.footerFont()), true);
		document.setFooter(footer);		
	}
    
    
}

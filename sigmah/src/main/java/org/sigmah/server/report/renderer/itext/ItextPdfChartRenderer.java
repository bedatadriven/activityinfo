/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.server.report.renderer.itext;

import java.awt.Graphics2D;

import org.sigmah.server.report.renderer.ChartRendererJC;
import org.sigmah.shared.report.model.PivotChartReportElement;

import com.google.inject.Inject;
import com.lowagie.text.DocWriter;
import com.lowagie.text.Document;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfWriter;

/**
 * Renders {@link org.sigmah.shared.report.model.PivotChartElement PivotChartElement}s into
 * an iText document.
 *
 * @author Alex Bertram
 */
public class ItextPdfChartRenderer extends ItextChartRenderer {

	private final ChartRendererJC chartRenderer;

	@Inject
	public ItextPdfChartRenderer(ChartRendererJC chartRenderer) {
		this.chartRenderer = chartRenderer;
	}

	@Override
	protected void renderImage(DocWriter writer, Document doc, PivotChartReportElement element,
			float width, float height) {
		
		// We can render the chart directly as vector graphics
		// in the PDF file
		writer.flush();
		PdfWriter pdfWriter = (PdfWriter)writer;
		PdfContentByte cb = pdfWriter.getDirectContent();
		Graphics2D g2d = cb.createGraphics(width, height, true, .75f);
		chartRenderer.render(element, false, g2d, (int)width, (int)height, 72);
		g2d.dispose();
	}
}

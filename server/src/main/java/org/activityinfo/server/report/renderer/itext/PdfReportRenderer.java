/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.activityinfo.server.report.renderer.itext;

import java.awt.Graphics2D;
import java.io.OutputStream;

import org.activityinfo.server.report.generator.MapIconPath;
import org.activityinfo.server.report.renderer.image.ImageCreator;

import com.google.inject.Inject;
import com.lowagie.text.BadElementException;
import com.lowagie.text.DocWriter;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.HeaderFooter;
import com.lowagie.text.Image;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfTemplate;
import com.lowagie.text.pdf.PdfWriter;


/**
 * iText ReportRenderer targeting PDF output
 *
 */
public class PdfReportRenderer extends ItextReportRenderer {

	private PdfWriter writer;

	@Inject
	public PdfReportRenderer(@MapIconPath String mapIconPath) {
		super(mapIconPath);
	}

	@Override
	protected DocWriter createWriter(Document document, OutputStream os) throws DocumentException {
		writer = PdfWriter.getInstance(document, os);
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

	@Override
	protected ImageCreator<? extends ItextImageResult> getImageCreator() {
		return new PdfVectorImageCreator();
	}

	private class PdfVectorImageCreator implements ImageCreator<PdfVectorImage> {
		@Override
		public PdfVectorImage create(int width, int height) {
			PdfTemplate template = PdfTemplate.createTemplate(writer, width, height);
			return new PdfVectorImage(template, template.createGraphics(width, height));
		}
	}

	private static class PdfVectorImage implements ItextImageResult {
		private PdfTemplate template;
		private Graphics2D g2d;

		public PdfVectorImage(PdfTemplate template, Graphics2D g2d) {
			super();
			this.template = template;
			this.g2d = g2d;
		}

		@Override
		public Graphics2D getGraphics() {
			return g2d;
		}

		@Override
		public Image toItextImage() throws BadElementException {
			g2d.dispose();
			Image image = Image.getInstance(template);
			image.scalePercent(72f/92f*100f);

			return image;
		}
	}
}

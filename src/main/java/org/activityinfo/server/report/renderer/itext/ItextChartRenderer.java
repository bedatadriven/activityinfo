
package org.activityinfo.server.report.renderer.itext;

import org.activityinfo.server.report.renderer.ChartRendererJC;
import org.activityinfo.server.report.renderer.image.ImageCreator;
import org.activityinfo.server.report.renderer.image.ItextGraphic;
import org.activityinfo.shared.report.model.PivotChartReportElement;

import com.google.code.appengine.awt.Color;
import com.google.code.appengine.awt.Graphics2D;
import com.lowagie.text.BadElementException;
import com.lowagie.text.DocWriter;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Font;
import com.lowagie.text.ImgTemplate;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfTemplate;
import com.lowagie.text.pdf.PdfWriter;

public class ItextChartRenderer implements ItextRenderer<PivotChartReportElement>{

	private final ImageCreator imageCreator;
	private final ChartRendererJC chartRenderer = new ChartRendererJC();

	public ItextChartRenderer(
			ImageCreator imageCreator) {
		super();
		this.imageCreator = imageCreator;
	}

	@Override
	public void render(DocWriter writer, Document doc, PivotChartReportElement element) throws DocumentException {

		doc.add(ThemeHelper.elementTitle(element.getTitle()));
		ItextRendererHelper.addFilterDescription(doc, element.getContent().getFilterDescriptions());
		ItextRendererHelper.addDateFilterDescription(doc, element.getFilter().getDateRange());

		if(element.getContent().getData().isEmpty()) {
			Paragraph para = new Paragraph("Aucune Données");
			para.setFont(new Font(Font.HELVETICA, 12, Font.NORMAL, new Color(0, 0, 0)));
			doc.add(para);

		} else {
			float width = doc.getPageSize().getWidth() - doc.rightMargin() - doc.leftMargin();
			float height = (doc.getPageSize().getHeight() - doc.topMargin() - doc.bottomMargin()) / 3f;

			renderImage(writer, doc, element, width, height);
		}
	}

	protected void renderImage(DocWriter writer, Document doc, PivotChartReportElement element, float width, float height) 
			throws BadElementException, DocumentException {


		ItextGraphic image = imageCreator.create((int)width, (int)height);

		chartRenderer.render(element, false, image.getGraphics(), (int)width, (int)height, 72);

		doc.add(image.toItextImage());

	}

}



package org.sigmah.server.report.renderer.itext;

import java.awt.Color;

import org.sigmah.server.report.renderer.ChartRendererJC;
import org.sigmah.server.report.renderer.image.ImageCreator;
import org.sigmah.shared.report.model.PivotChartReportElement;

import com.lowagie.text.BadElementException;
import com.lowagie.text.DocWriter;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Font;
import com.lowagie.text.Paragraph;

public class ItextChartRenderer implements ItextRenderer<PivotChartReportElement>{
	
	private final ImageCreator<? extends ItextImageResult> imageCreator;
	private final ChartRendererJC chartRenderer = new ChartRendererJC();
	
	public ItextChartRenderer(
			ImageCreator<? extends ItextImageResult> imageCreator) {
		super();
		this.imageCreator = imageCreator;
	}

	@Override
	public void render(DocWriter writer, Document doc, PivotChartReportElement element) {


		try {
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

				renderImage(doc, element, width, height);
			
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	protected void renderImage(Document doc, PivotChartReportElement element, float width, float height) 
			throws BadElementException, DocumentException {
		ItextImageResult image = imageCreator.create((int)width, (int)height);
		
		chartRenderer.render(element, false, image.getGraphics(), (int)width, (int)height, 72);
		
		doc.add(image.toItextImage());
	}
}


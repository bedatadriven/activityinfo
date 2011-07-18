
package org.sigmah.server.report.renderer.itext;

import java.awt.Color;

import org.sigmah.shared.report.model.PivotChartReportElement;

import com.lowagie.text.DocWriter;
import com.lowagie.text.Document;
import com.lowagie.text.Font;
import com.lowagie.text.Paragraph;

public abstract class ItextChartRenderer implements ItextRenderer<PivotChartReportElement>{
	

	public void render(DocWriter writer, Document doc, PivotChartReportElement element) {


		try {
			doc.add(ThemeHelper.elementTitle(element.getTitle()));
			ItextRendererHelper.addFilterDescription(doc, element.getContent().getFilterDescriptions());

			if(element.getContent().getData().isEmpty()) {
				Paragraph para = new Paragraph("Aucune Données");
				para.setFont(new Font(Font.HELVETICA, 12, Font.NORMAL, new Color(0, 0, 0)));
				doc.add(para);

			} else {
				float width = doc.getPageSize().getWidth() - doc.rightMargin() - doc.leftMargin();
				float height = (doc.getPageSize().getHeight() - doc.topMargin() - doc.bottomMargin()) / 3f;

				renderImage(writer, doc, element, width, height);
			
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	protected abstract void renderImage(DocWriter writer, Document doc, PivotChartReportElement element, float width, float height)
		throws Exception;
}


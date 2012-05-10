package org.sigmah.server.report.renderer.itext;

import org.activityinfo.shared.report.model.TextReportElement;

import com.lowagie.text.DocWriter;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Paragraph;

/**
 *  Render {@link TextReportElement} for iText documents
 * 
 */
public class ItextTextRenderer implements ItextRenderer<TextReportElement> {

	@Override
	public void render(DocWriter writer, Document doc, TextReportElement element)
			throws DocumentException {
		
	    doc.add(ThemeHelper.elementTitle(element.getTitle()));
	    if (element.getText() != null) {
	    	doc.add(new Paragraph(element.getText()));
	    }
	}
}

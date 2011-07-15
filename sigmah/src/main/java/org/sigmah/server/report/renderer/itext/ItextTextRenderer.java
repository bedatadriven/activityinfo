package org.sigmah.server.report.renderer.itext;

import org.sigmah.shared.report.model.TextElement;

import com.lowagie.text.DocWriter;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Paragraph;

/**
 *  Render {@link TextElement} for iText documents
 * 
 */
public class ItextTextRenderer implements ItextRenderer<TextElement> {

	@Override
	public void render(DocWriter writer, Document doc, TextElement element)
			throws DocumentException {
		
	    doc.add(ThemeHelper.elementTitle(element.getTitle()));
	    if (element.getText() != null) {
	    	doc.add(new Paragraph(element.getText()));
	    }
	}
}

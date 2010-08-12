package org.sigmah.server.report.renderer.itext;

import java.io.IOException;
import java.net.MalformedURLException;

import org.sigmah.shared.report.model.StaticElement;

import com.lowagie.text.DocWriter;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Image;
import com.lowagie.text.Paragraph;

/**
*  Render static elements for iText documents
* 
*/

public class ItextStaticRenderer implements ItextRenderer < StaticElement > {

	@Override
	public void render(DocWriter writer, Document doc, StaticElement element)
			throws DocumentException {
		
	    doc.add(ThemeHelper.elementTitle(element.getTitle()));
	    if (element.getText() != null) {
	    	doc.add(new Paragraph(element.getText()));
	    }

    	if (element.getImg() != null) {	
    		Image image = null;
			try {
				image = Image.getInstance(element.getImg());
				doc.add(image);
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
    	}
	}
}

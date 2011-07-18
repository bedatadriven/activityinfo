package org.sigmah.server.report.renderer.itext;

import java.io.IOException;
import java.net.MalformedURLException;

import org.sigmah.shared.report.model.ImageReportElement;

import com.lowagie.text.DocWriter;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Image;

/**
 * Renders an external image into the document
 */
public class ItextImageRenderer implements ItextRenderer<ImageReportElement>{

	@Override
	public void render(DocWriter writer, Document doc, ImageReportElement element)
			throws DocumentException {

    	if (element.getUrl() != null) {	
    		Image image = null;
			try {
				image = Image.getInstance(element.getUrl());
				doc.add(image);
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
    	}		
	}
}

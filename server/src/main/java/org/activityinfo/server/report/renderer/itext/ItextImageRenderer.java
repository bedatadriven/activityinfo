package org.activityinfo.server.report.renderer.itext;

import java.io.IOException;
import java.net.MalformedURLException;

import org.activityinfo.shared.report.model.ImageReportElement;
import org.apache.log4j.Logger;

import com.lowagie.text.DocWriter;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Image;

/**
 * Renders an external image into the document
 */
public class ItextImageRenderer implements ItextRenderer<ImageReportElement>{

	private static final Logger LOGGER = Logger.getLogger(ItextImageRenderer.class);
	
	@Override
	public void render(DocWriter writer, Document doc, ImageReportElement element)
			throws DocumentException {

    	if (element.getUrl() != null) {	
    		Image image = null;
			try {
				image = Image.getInstance(element.getUrl());
				doc.add(image);
			} catch (MalformedURLException e) {
				LOGGER.debug("Error rendering image", e);
			} catch (IOException e) {
				LOGGER.debug("Error rendering image", e);
			}
    	}		
	}
}

package org.activityinfo.server.report.renderer.itext;

/*
 * #%L
 * ActivityInfo Server
 * %%
 * Copyright (C) 2009 - 2013 UNICEF
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the 
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public 
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */


import java.io.IOException;
import java.net.MalformedURLException;

import org.activityinfo.shared.report.model.ImageReportElement;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.lowagie.text.DocWriter;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Image;

/**
 * Renders an external image into the document
 */
public class ItextImageRenderer implements ItextRenderer<ImageReportElement>{

	private static final Logger LOGGER = Logger.getLogger(ItextImageRenderer.class.getName());
	
	@Override
	public void render(DocWriter writer, Document doc, ImageReportElement element)
			throws DocumentException {

    	if (element.getUrl() != null) {	
    		Image image = null;
			try {
				image = Image.getInstance(element.getUrl());
				doc.add(image);
			} catch (MalformedURLException e) {
				LOGGER.log(Level.WARNING, "Error rendering image", e);
			} catch (IOException e) {
				LOGGER.log(Level.WARNING, "Error rendering image", e);
			}
    	}		
	}
}

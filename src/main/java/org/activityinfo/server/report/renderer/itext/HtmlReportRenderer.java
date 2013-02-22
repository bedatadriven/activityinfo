
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
import java.io.OutputStream;
import java.io.Writer;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;

import org.activityinfo.server.geo.AdminGeometryProvider;
import org.activityinfo.server.report.generator.MapIconPath;
import org.activityinfo.server.report.output.TempStorage;
import org.activityinfo.server.report.output.StorageProvider;
import org.activityinfo.server.report.renderer.image.ImageCreator;
import org.activityinfo.server.report.renderer.image.ItextGraphic;
import org.activityinfo.shared.report.model.ReportElement;

import com.google.code.appengine.awt.Color;
import com.google.code.appengine.awt.Graphics2D;
import com.google.code.appengine.awt.color.ColorSpace;
import com.google.code.appengine.awt.image.BufferedImage;
import com.google.code.appengine.imageio.ImageIO;
import com.google.inject.Inject;
import com.lowagie.text.BadElementException;
import com.lowagie.text.DocWriter;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Image;
import com.lowagie.text.html.HtmlWriter;


/**
 * iText ReportRenderer targeting HTML output
 *
 */
public class HtmlReportRenderer extends ItextReportRenderer {

	private final StorageProvider imageStorageProvider;

	@Inject
	public HtmlReportRenderer(AdminGeometryProvider geometryProvider, @MapIconPath String mapIconPath, StorageProvider imageStorageProvider) {
		super(geometryProvider, mapIconPath);
		this.imageStorageProvider = imageStorageProvider;
	}

	@Override
	protected DocWriter createWriter(Document document, OutputStream os) throws DocumentException {
		return HtmlWriter.getInstance(document, os);
	}


	@Override
	public String getMimeType() {
		return "text/html";
	}

	@Override
	public String getFileSuffix() {
		return ".html";
	}

	public void render(ReportElement element, final Writer writer) throws IOException {
		// The HtmlWriter encodes everything as ISO-8859-1
		// so we can be safely naive here about encoding
		final Charset charset = Charset.forName("ISO-8859-1");
		render(element, new OutputStream() {

			@Override
			public void write(int b) throws IOException {
				writer.append((char)b);
			}

			@Override
			public void write(byte[] b, int off, int len) throws IOException {
				writer.append(new String(b,off,len,charset));
			}
		});
	}    

	@Override
	protected void renderFooter(Document document) {
		// no footer for HTML
	}

	@Override
	protected ImageCreator getImageCreator() {
		return new HtmlImageCreator();
	}

	private class HtmlImageCreator implements ImageCreator {
		@Override
		public HtmlImage create(int width, int height) {
			BufferedImage image = new BufferedImage(width, height, ColorSpace.TYPE_RGB);
			Graphics2D g2d = image.createGraphics();
			g2d.setPaint(Color.WHITE);
			g2d.fillRect(0,0,width, height);

			return new HtmlImage(image, g2d);
		}

		@Override
		public ItextGraphic createMap(int width, int height) {
			return create(width, height);
		}
	}
	
	public static class MyImage extends Image {
		private int width;
		private int height;
		
		public MyImage(URL url, int width, int height) {
			super(url);
			this.width=width;
			this.height=height;
		}
		public MyImage(com.lowagie.text.Image im) {
			super(im);
		}
		@Override
		public int type() {
			return Element.IMGTEMPLATE;
		}

		@Override
		public float getScaledWidth() {
			return width;
		}

		@Override
		public float getScaledHeight() {
			return height;
		}		
	}

	private class HtmlImage implements ItextGraphic {
		private final BufferedImage image;
		private final Graphics2D g2d;

		public HtmlImage(BufferedImage image, Graphics2D g2d) {
			super();
			this.image = image;
			this.g2d = g2d;
		}

		@Override
		public Graphics2D getGraphics() {
			return g2d;
		}
		
		@Override
		public Image toItextImage() throws BadElementException {
			try {
				TempStorage storage = imageStorageProvider.allocateTemporaryFile("image/png", "activityinfo.png");
				ImageIO.write(image, "PNG", storage.getOutputStream());
				storage.getOutputStream().close();
				
				return new MyImage(new URL(storage.getUrl()), image.getWidth(), image.getHeight());
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}

		@Override
		public void addImage(String imageUrl, int x, int y, int width,
				int height) {
			BufferedImage img;
			try {
				img = ImageIO.read(new URL(imageUrl));
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
			g2d.drawImage(img, x, y, null);
		}	
	}
}

/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.server.report.renderer.itext;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;
import java.net.URL;
import java.nio.charset.Charset;

import javax.imageio.ImageIO;

import org.sigmah.server.report.generator.MapIconPath;
import org.sigmah.server.report.renderer.html.ImageStorage;
import org.sigmah.server.report.renderer.html.ImageStorageProvider;
import org.sigmah.server.report.renderer.image.ImageCreator;
import org.sigmah.shared.report.model.ReportElement;

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

	private final ImageStorageProvider imageStorageProvider;

	@Inject
	public HtmlReportRenderer(@MapIconPath String mapIconPath, ImageStorageProvider imageStorageProvider) {
		super(mapIconPath);
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
		// The HtmlWriter encodes everythings as ISO-8859-1
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
	protected ImageCreator<? extends ItextImageResult> getImageCreator() {
		return new HtmlImageCreator();
	}

	private class HtmlImageCreator implements ImageCreator<HtmlImage> {

		@Override
		public HtmlImage create(int width, int height) {
			BufferedImage image = new BufferedImage(width, height, ColorSpace.TYPE_RGB);
			Graphics2D g2d = image.createGraphics();
			g2d.setPaint(Color.WHITE);
			g2d.fillRect(0,0,width, height);

			try {
				return new HtmlImage(image, g2d, imageStorageProvider.getImageUrl(".png"));
			} catch (IOException e) {
				throw new RuntimeException();
			}
		}
	}
	
	private static class MyImage extends Image {
		private int width;
		private int height;
		public MyImage(URL url, int width, int height) {
			super(url);
			this.width=width;
			this.height=height;
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


	private static class HtmlImage implements ItextImageResult {
		private final BufferedImage image;
		private final Graphics2D g2d;
		private final ImageStorage storage;

		public HtmlImage(BufferedImage image, Graphics2D g2d, ImageStorage storage) {
			super();
			this.image = image;
			this.g2d = g2d;
			this.storage = storage;
		}

		@Override
		public Graphics2D getGraphics() {
			return g2d;
		}
		

		@Override
		public Image toItextImage() throws BadElementException {
			try {
				ImageIO.write(image, "PNG", storage.getOutputStream());
				return new MyImage(new URL(storage.getUrl()), image.getWidth(), image.getHeight());
				//return Image.getInstance(new URL(storage.getUrl()));
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}	
	}


}

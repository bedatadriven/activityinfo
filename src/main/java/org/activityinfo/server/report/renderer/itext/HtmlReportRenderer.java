/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.activityinfo.server.report.renderer.itext;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;

import org.activityinfo.server.report.generator.MapIconPath;
import org.activityinfo.server.report.output.ImageStorage;
import org.activityinfo.server.report.output.ImageStorageProvider;
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

			try {
				return new HtmlImage(image, g2d, imageStorageProvider.getImageUrl(null, ".png"));
			} catch (IOException e) {
				throw new RuntimeException();
			}
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

	private static class HtmlImage implements ItextGraphic {
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
			g2d.drawImage(img, x, image.getHeight() - y - height, null);
		}	
	}
}

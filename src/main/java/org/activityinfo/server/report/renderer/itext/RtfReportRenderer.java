/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.activityinfo.server.report.renderer.itext;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;

import org.activityinfo.server.report.generator.MapIconPath;
import org.activityinfo.server.report.renderer.image.ImageCreator;
import org.activityinfo.server.report.renderer.image.ItextGraphic;
import org.freehep.graphicsio.emf.EMFGraphics2D;

import com.google.code.appengine.awt.Dimension;
import com.google.code.appengine.awt.Graphics2D;
import com.google.code.appengine.awt.image.BufferedImage;
import com.google.code.appengine.imageio.ImageIO;
import com.google.inject.Inject;
import com.lowagie.text.BadElementException;
import com.lowagie.text.DocWriter;
import com.lowagie.text.Document;
import com.lowagie.text.HeaderFooter;
import com.lowagie.text.Image;
import com.lowagie.text.ImgEMF;
import com.lowagie.text.Phrase;
import com.lowagie.text.rtf.RtfWriter2;

/**
 * iText ReportRenderer targeting Rich Text Format (RTF) output
 */
public class RtfReportRenderer extends ItextReportRenderer {

    @Inject
    public RtfReportRenderer(@MapIconPath String mapIconPath) {
        super(mapIconPath);
    }

    @Override
    protected DocWriter createWriter(Document document, OutputStream os) {
        return RtfWriter2.getInstance(document, os);
    }

    @Override
    public String getMimeType() {
        return "application/rtf";
    }

    @Override
    public String getFileSuffix() {
        return ".rtf";
    }

	@Override
	protected void renderFooter(Document document) {
		HeaderFooter footer = new HeaderFooter(new Phrase("Page", ThemeHelper.footerFont()), true);
		document.setFooter(footer);		
	}

	@Override
	protected ImageCreator getImageCreator() {
		return new RtfImageCreator();
	}
    
	private static class RtfImageCreator implements ImageCreator  {

		@Override
		public EmfImage create(int width, int height) {
			return new EmfImage(width, height);
		}
		
		public PngImage createMap(int width, int height) {
			return new PngImage(width, height);
		}
	}
	
	private static class PngImage implements ItextGraphic {

		private final BufferedImage image;
		private final Graphics2D g2d;
		
		public PngImage(int width, int height) {
			image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
			g2d = image.createGraphics();
		}

		@Override
		public Graphics2D getGraphics() {
			return g2d;
		}

		@Override
		public Image toItextImage() throws BadElementException {
			try {
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				ImageIO.write(image, "png", baos);
				Image itextImage = Image.getInstance(baos.toByteArray());
				itextImage.scalePercent(72f/92f*100f);

//				itextImage.setDpi(dpiX, dpiY)
				return itextImage;
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}

		@Override
		public void addImage(String imageUrl, int x, int y, int width,
				int height) {
			BufferedImage image;
			try {
				image = ImageIO.read(new URL(imageUrl));
				g2d.drawImage(image, x, y, null);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
	}
	
	private static class EmfImage implements ItextGraphic {
		private int width;
		private int height;
		private final EMFGraphics2D g2d;
		private ByteArrayOutputStream baos;
		
		public EmfImage(int width, int height) {
			this.width = width;
			this.height = height;
			
			baos = new ByteArrayOutputStream();
			g2d = new EMFGraphics2D(baos, new Dimension(width, height));
			g2d.startExport();
		}

		@Override
		public Graphics2D getGraphics() {
			return g2d;
		}

		@Override
		public Image toItextImage() throws BadElementException {
			g2d.endExport();
			try {
				return new ImgEMF(baos.toByteArray(), width, height);
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}

		@Override
		public void addImage(String imageUrl, int x, int y, int width,
				int height) {
			throw new UnsupportedOperationException();
		}
	}
}

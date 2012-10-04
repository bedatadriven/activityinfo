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

import com.google.code.appengine.awt.Color;
import com.google.code.appengine.awt.Dimension;
import com.google.code.appengine.awt.Graphics2D;
import com.google.code.appengine.awt.color.ColorSpace;
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
		public RtfImage create(int width, int height) {
			return new RtfImage(width, height);
		}
	}
	
	private static class RtfImage implements ItextGraphic {
		private int width;
		private int height;
		private final EMFGraphics2D g2d;
		private ByteArrayOutputStream baos;
		
		public RtfImage(int width, int height) {
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

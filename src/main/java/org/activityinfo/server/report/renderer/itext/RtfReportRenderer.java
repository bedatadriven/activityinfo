/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.activityinfo.server.report.renderer.itext;

import com.google.code.appengine.awt.Color;
import com.google.code.appengine.awt.Graphics2D;
import com.google.code.appengine.awt.color.ColorSpace;
import com.google.code.appengine.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;

import com.google.code.appengine.imageio.ImageIO;

import org.activityinfo.server.report.generator.MapIconPath;
import org.activityinfo.server.report.renderer.image.ImageCreator;

import com.google.inject.Inject;
import com.lowagie.text.BadElementException;
import com.lowagie.text.DocWriter;
import com.lowagie.text.Document;
import com.lowagie.text.HeaderFooter;
import com.lowagie.text.Image;
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
	protected ImageCreator<? extends ItextImageResult> getImageCreator() {
		return new RtfImageCreator();
	}
    
	private static class RtfImageCreator implements ImageCreator<RtfImage>  {

		@Override
		public RtfImage create(int width, int height) {
			BufferedImage image = new BufferedImage(width, height, ColorSpace.TYPE_RGB);
			Graphics2D g2d = image.createGraphics();
			g2d.setPaint(Color.WHITE);
			g2d.fillRect(0,0,width, height);
			
			return new RtfImage(image, g2d);
		}
	}
	
	private static class RtfImage implements ItextImageResult {
		private final BufferedImage image;
		private final Graphics2D g2d;
		
		public RtfImage(BufferedImage image, Graphics2D g2d) {
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
				ByteArrayOutputStream baos = new ByteArrayOutputStream();            
				ImageIO.write(image,"JPEG", baos);

				Image imageElement = Image.getInstance(baos.toByteArray());
				imageElement.scalePercent(72f/92f*100f);
				return imageElement;
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
	}
}

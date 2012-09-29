package org.activityinfo.server.report.renderer.itext;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import org.activityinfo.server.report.renderer.image.TileHandler;

import com.google.code.appengine.awt.geom.AffineTransform;
import com.lowagie.text.BadElementException;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Image;
import com.lowagie.text.pdf.PdfTemplate;

public class PdfTileHandler implements TileHandler {

	private PdfTemplate template;
	private int height;

	public PdfTileHandler(PdfTemplate template, int height) {
		this.template = template;
		this.height = height;
	}

	@Override
	public void addTile(String tileUrl, int x, int y, int width, int height) {
		Image image;
		try {
			image = Image.getInstance(new URL(tileUrl));
		} catch (Exception e) {
			// ignore missing tiles
			return;
		}
		try {
			image.setAbsolutePosition(x, height - y);
			template.addImage(image, false);

		} catch (DocumentException e) {
			throw new RuntimeException(e);
		}
	}
}

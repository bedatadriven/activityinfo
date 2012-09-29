package org.activityinfo.server.report.renderer.ppt;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import org.activityinfo.server.report.renderer.image.TileHandler;
import org.apache.poi.hslf.model.Picture;
import org.apache.poi.hslf.model.Slide;
import org.apache.poi.hslf.usermodel.SlideShow;

import com.google.code.appengine.awt.Color;
import com.google.code.appengine.awt.Rectangle;
import com.google.common.io.Resources;

public class PPTTileHandler implements TileHandler{

	private SlideShow ppt;
	private Slide slide;

	public PPTTileHandler(SlideShow ppt, Slide slide) {
		this.ppt = ppt;
		this.slide = slide;
	}

	@Override
	public void addTile(String tileUrl, int x, int y, int width, int height) {
		try {
			byte[] imageBytes = Resources.toByteArray(new URL(tileUrl));
	        int pictureIndex = ppt.addPicture(imageBytes, Picture.PNG);
	        Picture basemap = new Picture(pictureIndex);
	        basemap.setAnchor(new Rectangle(x, y, width, height));
	        basemap.setLineWidth(0);
	        slide.addShape(basemap);
		} catch(MalformedURLException e) {
			throw new RuntimeException("Bad tile URL", e);
		} catch(IOException e) {
			// ignore missing tiles
		}		
	}

}

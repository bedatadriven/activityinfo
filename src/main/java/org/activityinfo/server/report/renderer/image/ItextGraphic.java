package org.activityinfo.server.report.renderer.image;

import com.google.code.appengine.awt.Graphics2D;
import com.lowagie.text.BadElementException;
import com.lowagie.text.Image;

public interface ItextGraphic {
	
	void addImage(String imageUrl, int x, int y, int width, int height);
	
	Graphics2D getGraphics();
	
	Image toItextImage() throws BadElementException;

}

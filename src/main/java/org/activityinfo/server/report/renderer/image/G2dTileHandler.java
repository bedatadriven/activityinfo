package org.activityinfo.server.report.renderer.image;

import java.io.IOException;
import java.net.URL;

import com.google.code.appengine.awt.Graphics2D;
import com.google.code.appengine.awt.image.BufferedImage;
import com.google.code.appengine.imageio.ImageIO;

public class G2dTileHandler implements TileHandler {

	private Graphics2D g2d;
	
	public G2dTileHandler(Graphics2D g2d) {
		this.g2d = g2d;
	}
	
	@Override
	public void addTile(String tileUrl, int x, int y, int width, int height) {
		try {
			BufferedImage image = ImageIO.read(new URL(tileUrl));
			if(image != null) {
				g2d.drawImage(image, x, y, image.getWidth(), image.getHeight(), null);
			}
		} catch(IOException e) {
			throw new RuntimeException("Exception drawing tile at " + tileUrl);
		}
	}
}

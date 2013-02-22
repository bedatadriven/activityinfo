package org.activityinfo.server.report.renderer.image;

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

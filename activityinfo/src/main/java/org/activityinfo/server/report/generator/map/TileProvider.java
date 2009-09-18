package org.activityinfo.server.report.generator.map;

import java.awt.Image;
import java.io.IOException;

public interface TileProvider {

	Image getImage(int zoom, int tileX, int tileY);
	
}

package org.sigmah.server.report.generator.map;

import java.awt.*;

public interface TileProvider {

	Image getImage(int zoom, int tileX, int tileY);
	
}

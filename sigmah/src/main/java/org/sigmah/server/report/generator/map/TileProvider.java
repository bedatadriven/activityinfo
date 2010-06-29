/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.server.report.generator.map;

import java.awt.*;

public interface TileProvider {

	Image getImage(int zoom, int tileX, int tileY);
	
}

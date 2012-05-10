/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.activityinfo.server.report.generator.map;

import java.awt.Image;

public interface TileDrawer {

	public void drawImage(Image image, int x, int y);	
}

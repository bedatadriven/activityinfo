/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.activityinfo.server.report.generator.map;

import java.net.URL;

public interface TileProvider {

	String getImageUrl(int zoom, int tileX, int tileY);
	
}

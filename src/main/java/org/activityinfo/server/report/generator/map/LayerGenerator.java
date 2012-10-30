/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.activityinfo.server.report.generator.map;

import org.activityinfo.server.command.DispatcherSync;
import org.activityinfo.shared.command.Filter;
import org.activityinfo.shared.report.content.MapContent;
import org.activityinfo.shared.util.mapping.Extents;

public interface LayerGenerator {

	void query(DispatcherSync dispatcher, Filter effectiveFilter);

	
    /**
     * Calculates the geographic extents for this layer
     *
     * @param sites
     * @return A geographic bounding box
     */
    Extents calculateExtents();

    /**
     * Calculates the maximum potential margins (in pixels)
     * outside of the geographic bounds.
     *
     * @return
     */
    Margins calculateMargins();

    void generate(TiledMap map, MapContent content);
}

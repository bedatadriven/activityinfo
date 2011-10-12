/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.server.report.generator.map;

import java.util.List;

import org.sigmah.shared.report.content.MapContent;
import org.sigmah.shared.report.model.SiteData;
import org.sigmah.shared.util.mapping.Extents;

/*
 * @author Alex Bertram
 */

public interface LayerGenerator {

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

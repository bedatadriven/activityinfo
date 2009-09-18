package org.activityinfo.server.report.generator.map;

import org.activityinfo.shared.report.content.Extents;
import org.activityinfo.shared.report.content.SiteData;
import org.activityinfo.shared.report.content.MapContent;

import java.util.List;

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
    Extents calculateExtents(List<SiteData> sites);


    /**
     * Calculates the maximum potential margins (in pixels)
     * outside of the geographic bounds.
     *
     * @return
     */
    Margins calculateMargins();


    void generate(List<SiteData> sites, TiledMap map, MapContent content);



}

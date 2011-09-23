package org.sigmah.server.report.generator.map;

import java.util.List;

import org.sigmah.server.report.generator.map.cluster.Clusterer;
import org.sigmah.server.report.generator.map.cluster.genetic.MarkerGraph.IntersectionCalculator;
import org.sigmah.shared.report.content.MapContent;
import org.sigmah.shared.report.model.SiteData;
import org.sigmah.shared.report.model.clustering.AutomaticClustering;
import org.sigmah.shared.report.model.clustering.Clustering;
import org.sigmah.shared.report.model.clustering.NoClustering;
import org.sigmah.shared.util.mapping.Extents;

public abstract class AbstractLayerGenerator implements LayerGenerator {

    protected boolean hasValue(SiteData site, List<Integer> indicatorIds) {

        // if no indicators are specified, we count sites
        if(indicatorIds.size() == 0) {
            return true;
        }

        for(Integer indicatorId : indicatorIds) {
            Double indicatorValue = site.getIndicatorValue(indicatorId);
            if(indicatorValue != null) {
                return true;
            }
        }
        return false;
    }

    protected Double getValue(SiteData site, List<Integer> indicatorIds) {

        // if no indicators are specified, we count sites.
        if(indicatorIds.size() == 0) {
            return 1.0;
        }

        Double value = null;
        for(Integer indicatorId : indicatorIds) {
            Double indicatorValue = site.getIndicatorValue(indicatorId);
            if(indicatorValue != null) {
                if(value == null) {
                    value = indicatorValue;
                } else {
                    value += indicatorValue;
                }
            }
        }
        return value;
    }
    

}

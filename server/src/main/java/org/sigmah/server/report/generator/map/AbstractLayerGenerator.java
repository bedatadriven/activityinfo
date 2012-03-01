package org.sigmah.server.report.generator.map;

import java.util.List;

import org.sigmah.shared.dto.SiteDTO;

public abstract class AbstractLayerGenerator implements LayerGenerator {

    protected boolean hasValue(SiteDTO site, List<Integer> indicatorIds) {

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

    protected Double getValue(SiteDTO site, List<Integer> indicatorIds) {

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

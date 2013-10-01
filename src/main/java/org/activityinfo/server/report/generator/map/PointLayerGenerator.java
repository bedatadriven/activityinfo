package org.activityinfo.server.report.generator.map;

/*
 * #%L
 * ActivityInfo Server
 * %%
 * Copyright (C) 2009 - 2013 UNICEF
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the 
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public 
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */

import java.util.List;
import java.util.Map;

import org.activityinfo.server.command.DispatcherSync;
import org.activityinfo.server.database.hibernate.entity.Indicator;
import org.activityinfo.shared.command.Filter;
import org.activityinfo.shared.command.GetSites;
import org.activityinfo.shared.dto.IndicatorDTO;
import org.activityinfo.shared.dto.SiteDTO;
import org.activityinfo.shared.report.model.DimensionType;
import org.activityinfo.shared.report.model.clustering.AdministrativeLevelClustering;
import org.activityinfo.shared.report.model.layers.PointMapLayer;

import com.google.common.collect.Lists;

public abstract class PointLayerGenerator<T extends PointMapLayer> implements
    LayerGenerator {

    protected T layer;
    protected List<SiteDTO> sites;
    private Map<Integer, Indicator> indicators;

    protected PointLayerGenerator(T layer, Map<Integer, Indicator> indicators) {
        this.layer = layer;
        this.indicators = indicators;
    }

    @Override
    public void query(DispatcherSync dispatcher, Filter effectiveFilter) {
        GetSites query = queryFor(effectiveFilter, layer);
        this.sites = dispatcher.execute(query).getData();
    }

    /**
     * For testing
     * 
     * @param sites
     */
    public void setSites(List<SiteDTO> sites) {
        this.sites = sites;
    }

    private GetSites queryFor(Filter effectiveFilter, PointMapLayer layer) {
        Filter layerFilter = new Filter(effectiveFilter, layer.getFilter());
        for(int id : layer.getIndicatorIds()) {
            Indicator indicator = indicators.get(id);
            if(indicator.getAggregation() == IndicatorDTO.AGGREGATE_SITE_COUNT) {
                layerFilter.addRestriction(DimensionType.Activity, indicator.getActivity().getId());
            } else {
                layerFilter.addRestriction(DimensionType.Indicator, indicator.getId());
            }
        }
        
        layerFilter.addRestriction(DimensionType.Indicator, physicalIndicators(layer));
        
        GetSites query = new GetSites();
        query.setFilter(layerFilter);
        query.setFetchAttributes(false);
        query.setFetchAdminEntities(layer.getClustering() instanceof AdministrativeLevelClustering);
        query.setFetchAllIndicators(false);
        query.setFetchIndicators(physicalIndicators(layer));

        return query;
    }

    protected List<Integer> physicalIndicators(PointMapLayer layer) {
        List<Integer> ids = Lists.newArrayList();
        for(int id : layer.getIndicatorIds()) {
            Indicator indicator = indicators.get(id);
            if(indicator.getAggregation() != IndicatorDTO.AGGREGATE_SITE_COUNT) {
                ids.add(id);
            }
        }
        return ids;
    }

    protected boolean hasValue(SiteDTO site, List<Integer> indicatorIds) {

        // if no indicators are specified, we count sites
        if (indicatorIds.size() == 0) {
            return true;
        }
     
        for (Integer indicatorId : indicatorIds) {
            Double indicatorValue = indicatorValue(site, indicatorId);
            if (indicatorValue != null) {
                return true;
            }
        }
        return false;
    }

    protected Double indicatorValue(SiteDTO site, Integer indicatorId) {
        Indicator indicator = indicators.get(indicatorId);
        if(indicator != null && indicator.getAggregation() == IndicatorDTO.AGGREGATE_SITE_COUNT) {
            return 1d;
        }
        return site.getIndicatorValue(indicatorId);
    }

    protected Double getValue(SiteDTO site, List<Integer> indicatorIds) {

        // if no indicators are specified, we count sites.
        if (indicatorIds.size() == 0) {
            return 1.0;
        }

        Double value = null;
        for (Integer indicatorId : indicatorIds) {
            Double indicatorValue = indicatorValue(site, indicatorId);
            if (indicatorValue != null) {
                if (value == null) {
                    value = indicatorValue;
                } else {
                    value += indicatorValue;
                }
            }
        }
        return value;
    }

}

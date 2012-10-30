package org.activityinfo.server.report.generator.map;

import java.util.List;

import org.activityinfo.server.command.DispatcherSync;
import org.activityinfo.shared.command.Filter;
import org.activityinfo.shared.command.GetSites;
import org.activityinfo.shared.dto.SiteDTO;
import org.activityinfo.shared.report.model.DimensionType;
import org.activityinfo.shared.report.model.clustering.AdministrativeLevelClustering;
import org.activityinfo.shared.report.model.layers.PointMapLayer;

public abstract class PointLayerGenerator<T extends PointMapLayer> implements LayerGenerator {

	protected T layer;
	protected List<SiteDTO> sites;
	
	protected PointLayerGenerator(T layer) {
		this.layer = layer;
	}
	
	public void query(DispatcherSync dispatcher, Filter effectiveFilter) {

		GetSites query = queryFor(effectiveFilter, layer);
		this.sites = dispatcher.execute(query).getData();
		
	}

	/**
	 * For testing
	 * @param sites
	 */
	public void setSites(List<SiteDTO> sites) {
		this.sites = sites;
	}
	
	private GetSites queryFor(Filter effectiveFilter, PointMapLayer layer) {
		Filter layerFilter = new Filter(effectiveFilter, layer.getFilter());
		layerFilter.addRestriction(DimensionType.Indicator, layer.getIndicatorIds());
		GetSites query = new GetSites();
		query.setFilter(layerFilter);
		query.setFetchAttributes(false);
		query.setFetchAdminEntities(layer.getClustering() instanceof AdministrativeLevelClustering);
		query.setFetchAllIndicators(false);
		query.setFetchIndicators(layer.getIndicatorIds());
		
		return query;
	}
	
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

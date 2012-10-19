package org.activityinfo.server.report.generator.map;

import org.activityinfo.server.command.DispatcherSync;
import org.activityinfo.shared.command.Filter;
import org.activityinfo.shared.command.GetAdminEntities;
import org.activityinfo.shared.command.PivotSites;
import org.activityinfo.shared.command.PivotSites.PivotResult;
import org.activityinfo.shared.command.result.AdminEntityResult;
import org.activityinfo.shared.command.result.Bucket;
import org.activityinfo.shared.dto.AdminEntityDTO;
import org.activityinfo.shared.report.content.AdminOverlay;
import org.activityinfo.shared.report.content.EntityCategory;
import org.activityinfo.shared.report.content.MapContent;
import org.activityinfo.shared.report.content.Polygon;
import org.activityinfo.shared.report.model.AdminDimension;
import org.activityinfo.shared.report.model.DimensionType;
import org.activityinfo.shared.report.model.layers.PolygonMapLayer;
import org.activityinfo.shared.util.mapping.Extents;


public class PolygonLayerGenerator implements LayerGenerator {

	private PolygonMapLayer layer;
	private PivotResult buckets;
	
	private AdminOverlay overlay;
	
	public PolygonLayerGenerator(PolygonMapLayer layer) {
		super();
		this.layer = layer;
		this.overlay = new AdminOverlay(layer.getAdminLevelId());
	}

	@Override
	public void query(DispatcherSync dispatcher, Filter effectiveFilter) {
		Filter layerFilter = new Filter(effectiveFilter, layer.getFilter());
		layerFilter.addRestriction(DimensionType.Indicator, layer.getIndicatorIds());

		queryBounds(dispatcher, layerFilter);
		queryBuckets(dispatcher, layerFilter);	
	}

	private void queryBounds(DispatcherSync dispatcher, Filter layerFilter) {
		GetAdminEntities query = new GetAdminEntities();
		query.setLevelId(layer.getAdminLevelId());
		
		AdminEntityResult entities = dispatcher.execute(query);
		for(AdminEntityDTO entity : entities.getData()) {
			overlay.addPolygon(new Polygon(entity));
		}
	
	}

	private void queryBuckets(DispatcherSync dispatcher, Filter layerFilter) {
		PivotSites query = new PivotSites();
		query.setFilter(layerFilter);
		AdminDimension adminDimension = new AdminDimension(layer.getAdminLevelId());
		query.setDimensions(adminDimension);
		
		this.buckets = dispatcher.execute(query);
		for(Bucket bucket : buckets.getBuckets()) {
			EntityCategory category = (EntityCategory) bucket.getCategory(adminDimension);
			int adminEntityId = category.getId(); 
			overlay.getPolygon(adminEntityId).setValue(bucket.doubleValue());
		}
	}

	@Override
	public Extents calculateExtents() {
		Extents extents = Extents.emptyExtents();
		for(Polygon polygon : overlay.getPolygons()) {
			if(polygon.hasValue()) {
				
			}
		}
		return extents;
	}

	@Override
	public Margins calculateMargins() {
		return new Margins(0);
	}

	@Override
	public void generate(TiledMap map, MapContent content) {
		
	}
}

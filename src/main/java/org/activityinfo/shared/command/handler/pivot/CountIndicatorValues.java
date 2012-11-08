package org.activityinfo.shared.command.handler.pivot;

import java.util.Map;

import org.activityinfo.shared.command.PivotSites;
import org.activityinfo.shared.command.PivotSites.ValueType;
import org.activityinfo.shared.command.handler.pivot.bundler.Bundler;
import org.activityinfo.shared.db.Tables;
import org.activityinfo.shared.dto.IndicatorDTO;
import org.activityinfo.shared.report.content.DimensionCategory;
import org.activityinfo.shared.report.content.TargetCategory;
import org.activityinfo.shared.report.model.Dimension;
import org.activityinfo.shared.report.model.DimensionType;

import com.bedatadriven.rebar.sql.client.query.SqlQuery;
import com.google.common.collect.Maps;

public class CountIndicatorValues extends BaseTable {

	@Override
	public boolean accept(PivotSites command) {
		return command.getValueType() == ValueType.INDICATOR;
	}
	
	@Override
	public void setupQuery(PivotSites command, SqlQuery query) {
	    query.from(Tables.INDICATOR, "Indicator");
	    query.leftJoin(Tables.ACTIVITY, "Activity").on("Activity.ActivityId=Indicator.ActivityId");
	    query.leftJoin(Tables.SITE, "Site").on("Site.ActivityId=Activity.ActivityId");
	    query.leftJoin(Tables.USER_DATABASE, "UserDatabase").on("UserDatabase.DatabaseId=Activity.DatabaseId");
       	
	    query.appendColumn("COUNT(DISTINCT Site.SiteId)", ValueFields.COUNT);
	    query.appendColumn(Integer.toString(IndicatorDTO.AGGREGATE_SITE_COUNT), ValueFields.AGGREGATION);
	    
	    query.where("Indicator.Aggregation").equalTo(IndicatorDTO.AGGREGATE_SITE_COUNT);
	    query.where("Indicator.dateDeleted").isNull();
	    query.where("Site.dateDeleted").isNull();
	}

	@Override
	public String getDimensionIdColumn(DimensionType type) {
		switch(type) {
		case Partner:
			return "Site.PartnerId";
		case Activity:
			return "Site.ActivityId";
		case Site:
			return "Site.SiteId";
		case Project:
			return "Site.ProjectId";
		case Location:
			return "Site.LocationId";
		case Indicator:
			return "Indicator.IndicatorId";
		case Database:
			return "Activity.DatabaseId";
		}
		throw new UnsupportedOperationException(type.name());
	}
	

	@Override
	public String getDateCompleteColumn() {
		return "Site.Date2";
	}

	@Override
	public TargetCategory getTargetCategory() {
		return TargetCategory.REALIZED;
	}

}

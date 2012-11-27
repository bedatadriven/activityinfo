package org.activityinfo.shared.command.handler.pivot;

import java.util.Map;

import org.activityinfo.shared.command.PivotSites;
import org.activityinfo.shared.command.PivotSites.ValueType;
import org.activityinfo.shared.db.Tables;
import org.activityinfo.shared.dto.IndicatorDTO;
import org.activityinfo.shared.report.content.DimensionCategory;
import org.activityinfo.shared.report.content.TargetCategory;
import org.activityinfo.shared.report.model.Dimension;
import org.activityinfo.shared.report.model.DimensionType;

import com.bedatadriven.rebar.sql.client.query.SqlQuery;
import com.google.common.collect.Maps;

/**
 * Base table for counting the number of sites that match
 * a certain criteria
 */
public class SiteCounts extends BaseTable {

	
	@Override
	public boolean accept(PivotSites command) {
		return command.getValueType() == ValueType.TOTAL_SITES;
	}
	
	@Override
	public void setupQuery(PivotSites command, SqlQuery query) {
		if(command.getFilter().isRestricted(DimensionType.Indicator)) {
			// we only need to pull in indicator values if there is a 
			// filter on indicators
			query.from(Tables.INDICATOR_VALUE, "V");
			query.leftJoin(Tables.REPORTING_PERIOD, "RP").on("V.ReportingPeriodId = RP.ReportingPeriodId");
			query.leftJoin(Tables.SITE, "Site").on("RP.SiteId = Site.SiteId");

		} else {
		    query.from(Tables.SITE, "Site");
		}

		query.leftJoin(Tables.ACTIVITY, "Activity").on("Activity.ActivityId = Site.ActivityId");
		query.leftJoin(Tables.USER_DATABASE, "UserDatabase").on("Activity.DatabaseId = UserDatabase.DatabaseId");
		query.leftJoin(Tables.REPORTING_PERIOD, "Period").on("Period.SiteId = Site.SiteId");

	    query.appendColumn("COUNT(DISTINCT Site.SiteId)", ValueFields.COUNT);
	    query.appendColumn(Integer.toString(IndicatorDTO.AGGREGATE_SITE_COUNT), ValueFields.AGGREGATION);
	    
	}

	@Override
	public String getDimensionIdColumn(DimensionType type) {
		switch(type) {
		case Partner:
			return "Site.PartnerId";
		case Activity:
			return "Site.ActivityId";
		case Database:
			return "Activity.DatabaseId";
		case Site:
			return "Site.SiteId";
		case Project:
			return "Site.ProjectId";
		case Location:
			return "Site.LocationId";
		case Indicator:
			return "V.IndicatorId";
		}
		throw new UnsupportedOperationException(type.name());
	}
	
	@Override
	public String getDateCompleteColumn() {
		return "Period.Date2";
	}


	@Override
	public TargetCategory getTargetCategory() {
		return TargetCategory.REALIZED;
	}
}

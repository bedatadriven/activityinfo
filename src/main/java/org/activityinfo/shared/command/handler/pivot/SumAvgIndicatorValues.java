package org.activityinfo.shared.command.handler.pivot;

import org.activityinfo.shared.command.PivotSites;
import org.activityinfo.shared.command.PivotSites.ValueType;
import org.activityinfo.shared.db.Tables;
import org.activityinfo.shared.report.content.TargetCategory;
import org.activityinfo.shared.report.model.DimensionType;

import com.bedatadriven.rebar.sql.client.query.SqlQuery;

public class SumAvgIndicatorValues extends BaseTable {

	@Override
	public boolean accept(PivotSites command) {
		return command.getValueType() == ValueType.INDICATOR;
	}

	
	@Override
	public void setupQuery(PivotSites command, SqlQuery query) {
		query.from(Tables.INDICATOR_VALUE, "V");
    	query.leftJoin(Tables.REPORTING_PERIOD, "Period").on("Period.ReportingPeriodId = V.ReportingPeriodId");
    	query.leftJoin(Tables.SITE, "Site").on("Period.SiteId = Site.SiteId");
		query.leftJoin(Tables.INDICATOR, "Indicator").on("Indicator.IndicatorId = V.IndicatorId");
    	query.leftJoin(Tables.ACTIVITY, "Activity").on("Indicator.ActivityId = Activity.ActivityId");
    	query.leftJoin(Tables.USER_DATABASE, "UserDatabase").on("Activity.DatabaseId = UserDatabase.DatabaseId");
       	
    	query.where("Indicator.DateDeleted is NULL");
		query.where("Site.dateDeleted").isNull();
        query.where("Activity.dateDeleted").isNull();
        query.where("UserDatabase.dateDeleted").isNull();
        
    	/*
		 * Add the indicator to the query: we can't aggregate values from
		 * different indicators so this is a must
		 */
        query.appendColumn("Indicator.Aggregation", ValueFields.AGGREGATION);
        query.appendColumn("SUM(V.Value)", ValueFields.SUM);
        query.appendColumn("COUNT(V.Value)", ValueFields.COUNT);

		query.groupBy("Indicator.IndicatorId");
		query.groupBy("Indicator.Aggregation");
		query.whereTrue(" ((V.value <> 0 and Indicator.Aggregation=0) or Indicator.Aggregation=1) ");
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
		case Indicator:
			return "Indicator.IndicatorId";
		case Site:
			return "Site.SiteId";
		case Project: 
			return "Site.ProjectId";
		case Location:
			return "Site.LocationId";
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

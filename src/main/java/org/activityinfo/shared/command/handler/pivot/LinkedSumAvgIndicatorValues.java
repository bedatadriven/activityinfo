package org.activityinfo.shared.command.handler.pivot;

import org.activityinfo.shared.command.PivotSites;
import org.activityinfo.shared.command.PivotSites.ValueType;
import org.activityinfo.shared.db.Tables;
import org.activityinfo.shared.report.content.TargetCategory;
import org.activityinfo.shared.report.model.DimensionType;

import com.bedatadriven.rebar.sql.client.query.SqlQuery;

public class LinkedSumAvgIndicatorValues extends BaseTable {
	

	@Override
	public boolean accept(PivotSites command) {
		return command.getValueType() == ValueType.INDICATOR;
	}

	@Override
	public void setupQuery(PivotSites command, SqlQuery query) {
		
		query.from(Tables.INDICATOR_LINK, "IndicatorLink");
		query.leftJoin(Tables.INDICATOR_VALUE, "V").on("IndicatorLink.SourceIndicatorId=V.IndicatorId");
		query.leftJoin(Tables.INDICATOR, "Indicator").on("IndicatorLink.DestinationIndicatorId=Indicator.IndicatorId");
		query.leftJoin(Tables.ACTIVITY, "Activity").on("Activity.ActivityId=Indicator.ActivityId");
		query.leftJoin(Tables.USER_DATABASE, "UserDatabase").on("UserDatabase.DatabaseId=Activity.DatabaseId");
		
		query.leftJoin(Tables.REPORTING_PERIOD, "Period").on("Period.ReportingPeriodId=V.ReportingPeriodId");
		query.leftJoin(Tables.SITE, "Site").on("Site.SiteId=Period.SiteId");
		
        query.appendColumn("Indicator.Aggregation", ValueFields.AGGREGATION);
        query.appendColumn("SUM(V.Value)", ValueFields.SUM);
        query.appendColumn("COUNT(V.Value)", ValueFields.COUNT);

		query.groupBy("Indicator.IndicatorId");
		query.groupBy("Indicator.Aggregation");
		query.whereTrue(" ((V.value <> 0 and Indicator.Aggregation=0) or Indicator.Aggregation=1) ");

		query.where("Site.dateDeleted").isNull();
        query.where("Activity.dateDeleted").isNull();
        query.where("UserDatabase.dateDeleted").isNull();
		
	}
	
	private boolean requires(PivotSites command, DimensionType... types) {
		for(DimensionType type : types) {
			if(command.isPivotedBy(type) || command.getFilter().isRestricted(type)) {
				return true;
			}
		}
		return false;
	}
	
	@Override
	public String getDimensionIdColumn(DimensionType type) {
		switch(type) {

		case Partner:
			return "Site.PartnerId";
		case Activity:
			return "Indicator.ActivityId";
		case Database:
			return "Activity.DatabaseId";
		case Indicator:
			return "IndicatorLink.DestinationIndicatorId";
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

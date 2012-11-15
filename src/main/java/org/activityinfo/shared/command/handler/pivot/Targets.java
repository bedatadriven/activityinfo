package org.activityinfo.shared.command.handler.pivot;

import java.util.Map;

import org.activityinfo.shared.command.PivotSites;
import org.activityinfo.shared.command.PivotSites.ValueType;
import org.activityinfo.shared.db.Tables;
import org.activityinfo.shared.report.content.TargetCategory;
import org.activityinfo.shared.report.model.DimensionType;

import com.bedatadriven.rebar.sql.client.query.SqlQuery;
import com.google.common.collect.Maps;

public class Targets extends BaseTable {

	private Map<DimensionType, String> fieldNames;
	
	public Targets() {
		fieldNames = Maps.newHashMap();
		fieldNames.put(DimensionType.Partner, "Target.PartnerId");
		fieldNames.put(DimensionType.Activity, "Indicator.ActivityId");
		fieldNames.put(DimensionType.Indicator, "Indicator.IndicatorId");
		fieldNames.put(DimensionType.Project, "Target.ProjectId");
		fieldNames.put(DimensionType.Database, "Activity.DatabaseId");
	}
	

	@Override
	public boolean accept(PivotSites command) {
		if(command.getValueType() != ValueType.INDICATOR) {
			return false;
		}
		if(!command.getDimensionTypes().contains(DimensionType.Target)) {
			return false;
		}
		if(!fieldNames.keySet().containsAll(command.getDimensionTypes())) {
			return false;
		}
		if(!fieldNames.keySet().containsAll(command.getFilter().getRestrictedDimensions())) {
			return false;
		}
		
		return true;
	}
	

	@Override
	public TargetCategory getTargetCategory() {
		return TargetCategory.TARGETED;
	}

	@Override
	public void setupQuery(PivotSites command, SqlQuery query) {
		query.from(Tables.TARGET_VALUE, "V");
		query.leftJoin(Tables.TARGET, "Target").on("V.TargetId=Target.TargetId");
		query.leftJoin(Tables.INDICATOR, "Indicator").on("V.IndicatorId=Indicator.IndicatorId");
		query.leftJoin(Tables.ACTIVITY, "Activity").on("Activity.ActivityId=Indicator.ActivityId");
		query.leftJoin(Tables.USER_DATABASE, "UserDatabase").on("UserDatabase.DatabaseId=Activity.DatabaseId");

        query.appendColumn("Indicator.Aggregation", ValueFields.AGGREGATION);
        query.appendColumn("SUM(V.Value)", ValueFields.SUM);
        query.appendColumn("COUNT(V.Value)", ValueFields.COUNT);

	}

	@Override
	public String getDimensionIdColumn(DimensionType type) {
		if(!fieldNames.containsKey(type)) {
			throw new UnsupportedOperationException("type: " + type);
		}
		return fieldNames.get(type);
	}

	@Override
	public String getDateCompleteColumn() {
		return "Target.Date2";
	}

}

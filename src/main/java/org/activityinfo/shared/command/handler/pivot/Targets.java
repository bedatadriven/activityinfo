package org.activityinfo.shared.command.handler.pivot;

import java.util.Map;

import org.activityinfo.shared.command.PivotSites;
import org.activityinfo.shared.command.PivotSites.ValueType;
import org.activityinfo.shared.db.Tables;
import org.activityinfo.shared.report.content.DimensionCategory;
import org.activityinfo.shared.report.content.TargetCategory;
import org.activityinfo.shared.report.model.AdminDimension;
import org.activityinfo.shared.report.model.Dimension;
import org.activityinfo.shared.report.model.DimensionType;

import com.bedatadriven.rebar.sql.client.query.SqlQuery;
import com.google.common.collect.Maps;

public class Targets extends BaseTable {


	@Override
	public boolean accept(PivotSites command) {
		if(command.getValueType() != ValueType.INDICATOR) {
			return false;
		}
		boolean containsTarget = false;
		for (Dimension dimension : command.getDimensions()) {
			if (dimension instanceof AdminDimension || dimension.getType() == DimensionType.Location) {
				return false;
			}
			if (dimension.equals(Dimension.TARGET)) {
				containsTarget = true;
			}
		}
		return containsTarget;
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
		switch(type) {
		case Partner:
			return "Target.PartnerId";
		case Activity:
			return "Indicator.ActivityId";
		case Indicator:
			return "Indicator.IndicatorId";
		case Project: 
			return "Target.ProjectId";
		case Database:
			return "Activity.DatabaseId";
		}
		throw new UnsupportedOperationException();
	}

	@Override
	public String getDateCompleteColumn() {
		return "Target.Date2";
	}

}

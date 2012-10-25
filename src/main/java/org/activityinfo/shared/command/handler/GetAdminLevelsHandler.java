package org.activityinfo.shared.command.handler;

import java.util.List;

import org.activityinfo.shared.command.GetAdminLevels;
import org.activityinfo.shared.command.result.AdminLevelResult;
import org.activityinfo.shared.db.Tables;
import org.activityinfo.shared.dto.AdminLevelDTO;

import com.bedatadriven.rebar.sql.client.SqlResultCallback;
import com.bedatadriven.rebar.sql.client.SqlResultSet;
import com.bedatadriven.rebar.sql.client.SqlResultSetRow;
import com.bedatadriven.rebar.sql.client.SqlTransaction;
import com.bedatadriven.rebar.sql.client.query.SqlQuery;
import com.google.common.collect.Lists;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class GetAdminLevelsHandler implements CommandHandlerAsync<GetAdminLevels, AdminLevelResult> {

	@Override
	public void execute(GetAdminLevels command, ExecutionContext context,
			final AsyncCallback<AdminLevelResult> callback) {
		
		SqlQuery.select()
			.appendColumn("level.adminlevelId", "id")
			.appendColumn("level.name", "name")
			.appendColumn("level.polygons", "polygons")
			//.from(Tables.INDICATOR_LINK, "il")
			//.leftJoin(Tables.INDICATOR_VALUE, "iv").on("il.destinationIndicatorId=iv.indicatorid")
			.from(Tables.INDICATOR_VALUE, "iv")
			.leftJoin(Tables.REPORTING_PERIOD, "rp").on("rp.reportingperiodid=iv.reportingperiodid")
			.leftJoin(Tables.SITE, "s").on("s.siteId=rp.siteid")
			.leftJoin(Tables.LOCATION_ADMIN_LINK, "la").on("s.locationid=la.locationid")
			.leftJoin(Tables.ADMIN_ENTITY, "e").on("e.adminentityid=la.adminentityid")
			.leftJoin(Tables.ADMIN_LEVEL, "level").on("e.adminlevelid=level.adminlevelid")
			.where("iv.indicatorId").in(command.getIndicatorIds())
			.groupBy("level.adminlevelid")
			.groupBy("level.name")
			.execute(context.getTransaction(), new SqlResultCallback() {
				
				@Override
				public void onSuccess(SqlTransaction tx, SqlResultSet results) {
					List<AdminLevelDTO> levels = Lists.newArrayList();
					for(SqlResultSetRow row : results.getRows()) {
						AdminLevelDTO level = new AdminLevelDTO();
						level.setId(row.getInt("id"));
						level.setName(row.getString("name"));
						level.setPolygons(row.getBoolean("polygons"));
						levels.add(level);
					}
					
					callback.onSuccess(new AdminLevelResult(levels));
				}
			});
				
	}

}

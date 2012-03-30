package org.sigmah.shared.command.handler;

import java.util.List;

import org.sigmah.shared.command.GetReportVisibility;
import org.sigmah.shared.command.result.ReportVisibilityResult;
import org.sigmah.shared.dto.ReportVisibilityDTO;

import com.bedatadriven.rebar.sql.client.SqlResultCallback;
import com.bedatadriven.rebar.sql.client.SqlResultSet;
import com.bedatadriven.rebar.sql.client.SqlResultSetRow;
import com.bedatadriven.rebar.sql.client.SqlTransaction;
import com.bedatadriven.rebar.sql.client.query.SqlQuery;
import com.google.common.collect.Lists;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class GetReportVisibilityHandler implements CommandHandlerAsync<GetReportVisibility, ReportVisibilityResult> {

	@Override
	public void execute(GetReportVisibility command, ExecutionContext context,
			final AsyncCallback<ReportVisibilityResult> callback) {
		SqlQuery.select()
			.appendColumn("v.databaseId", "databaseId")
			.appendColumn("d.name", "name")
			.appendColumn("v.defaultDashboard", "defaultDashboard")
			.from("reportvisibility", "v")
			.leftJoin("userdatabase", "d").on("v.databaseId=d.databaseId")
			.where("v.reportId").equalTo(command.getReportId())
			.execute(context.getTransaction(), new SqlResultCallback() {
				
				@Override
				public void onSuccess(SqlTransaction tx, SqlResultSet results) {
					List<ReportVisibilityDTO> list = Lists.newArrayList();
					for(SqlResultSetRow row : results.getRows()) {
						ReportVisibilityDTO dto = new ReportVisibilityDTO();
						dto.setDatabaseId(row.getInt("databaseId"));
						dto.setDatabaseName(row.getString("name"));
						dto.setDefaultDashboard(row.getBoolean("defaultDashboard"));
						dto.setVisible(true);
						list.add(dto);
					}
					callback.onSuccess(new ReportVisibilityResult(list));
				}
			});
	}

}

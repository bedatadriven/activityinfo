package org.activityinfo.shared.command.handler;

/*
 * #%L
 * ActivityInfo Server
 * %%
 * Copyright (C) 2009 - 2013 UNICEF
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the 
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public 
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */

import java.util.List;

import org.activityinfo.shared.command.GetReportVisibility;
import org.activityinfo.shared.command.result.ReportVisibilityResult;
import org.activityinfo.shared.db.Tables;
import org.activityinfo.shared.dto.ReportVisibilityDTO;

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
			.from(Tables.REPORT_VISIBILITY, "v")
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

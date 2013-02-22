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

import org.activityinfo.shared.command.GetSiteHistory;
import org.activityinfo.shared.command.GetSiteHistory.GetSiteHistoryResult;
import org.activityinfo.shared.dto.SiteHistoryDTO;

import com.bedatadriven.rebar.sql.client.SqlResultCallback;
import com.bedatadriven.rebar.sql.client.SqlResultSet;
import com.bedatadriven.rebar.sql.client.SqlResultSetRow;
import com.bedatadriven.rebar.sql.client.SqlTransaction;
import com.bedatadriven.rebar.sql.client.query.SqlQuery;
import com.google.common.collect.Lists;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class GetSiteHistoryHandler implements CommandHandlerAsync<GetSiteHistory, GetSiteHistoryResult> {

	@Override
	public void execute(final GetSiteHistory command, ExecutionContext context, final AsyncCallback<GetSiteHistoryResult> callback) {
		
		SqlQuery.select()
				.appendColumn("h.id", "id")
				.appendColumn("h.timecreated", "timecreated")
				.appendColumn("h.initial", "initial")
				.appendColumn("h.json", "json")
				.appendColumn("u.name", "username")
				.appendColumn("u.email", "useremail")
				.from("sitehistory", "h")
				.innerJoin("userlogin", "u").on("h.userid = u.userid")
				
				.where("h.siteid").equalTo(command.getSiteId())
				.orderBy("h.timecreated")
				
				.execute(context.getTransaction(), new SqlResultCallback() {
					@Override
					public void onSuccess(SqlTransaction tx, SqlResultSet results) {
						List<SiteHistoryDTO> siteHistories = Lists.newArrayList();
						for (SqlResultSetRow row : results.getRows()) {
							SiteHistoryDTO siteHistory = new SiteHistoryDTO();
							siteHistory.setId(row.getInt("id"));
							siteHistory.setTimeCreated(row.getDouble("timecreated"));
							siteHistory.setInitial(row.getBoolean("initial"));
							siteHistory.setJson(row.getString("json"));
							siteHistory.setUserName(row.getString("username"));
							siteHistory.setUserEmail(row.getString("useremail"));
							siteHistories.add(siteHistory);
						}
						callback.onSuccess(new GetSiteHistoryResult(siteHistories));
					}
				});
	}
}

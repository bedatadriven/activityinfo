package org.activityinfo.shared.command.handler;

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

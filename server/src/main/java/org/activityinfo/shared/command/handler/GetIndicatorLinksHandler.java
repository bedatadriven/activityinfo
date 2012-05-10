package org.activityinfo.shared.command.handler;

import java.util.List;

import org.activityinfo.shared.command.GetIndicatorLinks;
import org.activityinfo.shared.command.result.IndicatorLink;
import org.activityinfo.shared.command.result.IndicatorLinkResult;

import com.bedatadriven.rebar.sql.client.SqlResultCallback;
import com.bedatadriven.rebar.sql.client.SqlResultSet;
import com.bedatadriven.rebar.sql.client.SqlResultSetRow;
import com.bedatadriven.rebar.sql.client.SqlTransaction;
import com.bedatadriven.rebar.sql.client.query.SqlQuery;
import com.google.common.collect.Lists;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class GetIndicatorLinksHandler implements CommandHandlerAsync<GetIndicatorLinks, IndicatorLinkResult>{

	@Override
	public void execute(GetIndicatorLinks command, ExecutionContext context,
			final AsyncCallback<IndicatorLinkResult> callback) {
		
		SqlQuery.select()
			.appendColumn("L.SourceIndicatorId", "SourceIndicatorId")
			.appendColumn("SDB.DatabaseId", "SourceDatabaseId")
			.appendColumn("L.DestinationIndicatorId", "DestinationIndicatorId")
			.appendColumn("DDB.DatabaseId", "DestinationDatabaseId")
			.from("IndicatorLink", "L")
			.innerJoin("Indicator", "SI").on("SI.IndicatorId=L.SourceIndicatorId")
			.innerJoin("Activity", "SA").on("SA.ActivityId=SI.ActivityId")
			.innerJoin("UserDatabase", "SDB").on("SDB.DatabaseId=SA.DatabaseId")
			.innerJoin("Indicator", "DI").on("DI.IndicatorId=L.DestinationIndicatorId")
			.innerJoin("Activity", "DA").on("DA.ActivityId=DI.ActivityId")
			.innerJoin("UserDatabase", "DDB").on("DDB.DatabaseId=DA.DatabaseId")
			.execute(context.getTransaction(), new SqlResultCallback() {
				
				@Override
				public void onSuccess(SqlTransaction tx, SqlResultSet results) {
					List<IndicatorLink> links = Lists.newArrayList();
					for(SqlResultSetRow row : results.getRows()) {
						IndicatorLink link = new IndicatorLink();
						link.setSourceDatabaseId(row.getInt("SourceDatabaseId"));
						link.setSourceIndicatorId(row.getInt("SourceIndicatorId"));
						link.setDestinationDatabaseId(row.getInt("DestinationDatabaseId"));
						link.setDestinationIndicatorId(row.getInt("DestinationIndicatorId"));
						links.add(link);
					}
					callback.onSuccess(new IndicatorLinkResult(links));
				}
		});
		
	}

}

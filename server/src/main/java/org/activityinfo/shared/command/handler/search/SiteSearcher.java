package org.activityinfo.shared.command.handler.search;

import java.util.ArrayList;
import java.util.List;

import org.activityinfo.shared.report.model.DimensionType;

import com.bedatadriven.rebar.sql.client.SqlResultCallback;
import com.bedatadriven.rebar.sql.client.SqlResultSet;
import com.bedatadriven.rebar.sql.client.SqlResultSetRow;
import com.bedatadriven.rebar.sql.client.SqlTransaction;
import com.bedatadriven.rebar.sql.client.query.SqlQuery;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class SiteSearcher implements Searcher {

	@Override
	public void search(List<String> testQuery, SqlTransaction tx,
			final AsyncCallback<List<Integer>> callback) {
		
		final String primaryKey = "SiteId";
		String tableName="Site";
		String columnToSearch = "Comments";
		
		SqlQuery
				.select(primaryKey)
				.from(tableName)
				.whereLikes(columnToSearch)
				.likeMany(testQuery)
				
				.execute(tx, new SqlResultCallback() {
					@Override
					public void onSuccess(SqlTransaction tx, SqlResultSet results) {
						List<Integer> ids = new ArrayList<Integer>();
						for (SqlResultSetRow row : results.getRows()) {
							ids.add(row.getInt(primaryKey));
						}
						callback.onSuccess(ids);
					}
				});
	}

	@Override
	public DimensionType getDimensionType() {
		return DimensionType.Site;
	}

}

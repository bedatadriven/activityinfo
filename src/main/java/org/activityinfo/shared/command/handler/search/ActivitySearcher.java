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

public class ActivitySearcher implements Searcher {


	@Override
	public void search(List<String> searchTerms, SqlTransaction tx, final AsyncCallback<List<Integer>> callback) {
		SqlQuery.select("ActivityId")
				.from("activity")
				.whereLikes("Name")
				.likeMany(searchTerms)
				.or()
				.whereLikes("Category")
				.likeMany(searchTerms)
				
				.execute(tx, new SqlResultCallback() {
					
					@Override
					public void onSuccess(SqlTransaction tx, SqlResultSet results) {
						final List<Integer> activityIds = new ArrayList<Integer>();
						for (SqlResultSetRow result : results.getRows()) {
							activityIds.add(result.getInt("ActivityId"));
						}
						callback.onSuccess(activityIds);
					}
				});
	}

	@Override
	public DimensionType getDimensionType() {
		return DimensionType.Activity;
	}
}

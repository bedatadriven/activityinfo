package org.sigmah.shared.search;

import java.util.ArrayList;
import java.util.List;

import org.sigmah.shared.domain.Activity;
import org.sigmah.shared.report.model.DimensionType;

import com.bedatadriven.rebar.sql.client.SqlResultCallback;
import com.bedatadriven.rebar.sql.client.SqlResultSet;
import com.bedatadriven.rebar.sql.client.SqlResultSetRow;
import com.bedatadriven.rebar.sql.client.SqlTransaction;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class ActivitySearcher extends AbstractSearcher<Activity> implements Searcher<Activity> {


	@Override
	public void search(String testQuery, SqlTransaction tx, final AsyncCallback<List<Integer>> callback) {
		String query = likeify(testQuery);
		final List<Integer> activityIds = new ArrayList<Integer>();
		tx.executeSql("select activityid from activity where name like ? or category like ?", new Object[]{query}, new SqlResultCallback() {
			@Override
			public void onSuccess(SqlTransaction tx, SqlResultSet results) {
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

package org.activityinfo.shared.command.handler.search;

import java.util.ArrayList;
import java.util.List;

import org.activityinfo.shared.report.model.DimensionType;

import com.bedatadriven.rebar.sql.client.SqlException;
import com.bedatadriven.rebar.sql.client.SqlResultCallback;
import com.bedatadriven.rebar.sql.client.SqlResultSet;
import com.bedatadriven.rebar.sql.client.SqlResultSetRow;
import com.bedatadriven.rebar.sql.client.SqlTransaction;
import com.bedatadriven.rebar.sql.client.query.SqlQuery;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class GenericSearcher implements Searcher {
	private DimensionType dimension;
	
	public GenericSearcher(DimensionType dimension) {
		this.dimension = dimension;
	}

	@Override
	public void search(List<String> testQuery, SqlTransaction tx,
			final AsyncCallback<List<Integer>> callback) {
		final List<Integer> ids = new ArrayList<Integer>();
		String tableName = dimension.toString();
		final String primaryKey = tableName + "Id";
		
		SqlQuery.select(primaryKey)
				.from(tableName)
				.whereLikes("Name")
				.likeMany(testQuery)
				
				.execute(tx, new SqlResultCallback() {
					@Override
					public void onSuccess(SqlTransaction tx, SqlResultSet results) {
						for (SqlResultSetRow row : results.getRows()) {
							ids.add(row.getInt(primaryKey));
						}
						callback.onSuccess(ids);
					}
					
					@Override
					public boolean onFailure(SqlException e) {
						return super.onFailure(e);
					}
				});
	}

	@Override
	public DimensionType getDimensionType() {
		return dimension;
	}

}

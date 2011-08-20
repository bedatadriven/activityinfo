package org.sigmah.shared.search;

import java.util.ArrayList;
import java.util.List;

import org.sigmah.shared.domain.Site;
import org.sigmah.shared.report.model.DimensionType;

import com.bedatadriven.rebar.sql.client.SqlException;
import com.bedatadriven.rebar.sql.client.SqlResultCallback;
import com.bedatadriven.rebar.sql.client.SqlResultSet;
import com.bedatadriven.rebar.sql.client.SqlResultSetRow;
import com.bedatadriven.rebar.sql.client.SqlTransaction;
import com.bedatadriven.rebar.sql.client.query.SqlQuery;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class SiteSearcher implements Searcher<Site> {

	@Override
	public void search(String testQuery, SqlTransaction tx,
			final AsyncCallback<List<Integer>> callback) {
		
		final String primaryKey = "SiteId";
		String tableName="Site";
		String ColumnToSearch = "Comments";
		
		SqlQuery
				.select(primaryKey)
				.from(tableName)
				.where(ColumnToSearch)
				.like()
				.appendLikeParameter(testQuery)
				
				.execute(tx, new SqlResultCallback() {
					List<Integer> ids = new ArrayList<Integer>();
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
		return DimensionType.Site;
	}

}

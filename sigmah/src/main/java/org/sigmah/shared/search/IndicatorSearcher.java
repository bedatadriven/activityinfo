package org.sigmah.shared.search;

import java.util.ArrayList;
import java.util.List;

import org.sigmah.shared.domain.Indicator;
import org.sigmah.shared.report.model.DimensionType;

import com.bedatadriven.rebar.sql.client.SqlException;
import com.bedatadriven.rebar.sql.client.SqlResultCallback;
import com.bedatadriven.rebar.sql.client.SqlResultSet;
import com.bedatadriven.rebar.sql.client.SqlResultSetRow;
import com.bedatadriven.rebar.sql.client.SqlTransaction;
import com.bedatadriven.rebar.sql.client.query.SqlQuery;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class IndicatorSearcher implements Searcher<Indicator> {

	@Override
	public void search(String testQuery, SqlTransaction tx,
			final AsyncCallback<List<Integer>> callback) {
		
		final String primaryKey = "IndicatorId";
		String tableName="Indicator";
		String ColumnToSearch = "description";
		
		SqlQuery
				.select(primaryKey)
				.from(tableName)
				.onlyWhere(ColumnToSearch)
				.like(testQuery)
				.or()
				.onlyWhere("Name")
				.like(testQuery)
				
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
		return DimensionType.Indicator;
	}

}

package org.sigmah.shared.search;

import java.util.ArrayList;
import java.util.List;

import org.sigmah.shared.domain.Location;
import org.sigmah.shared.report.model.DimensionType;

import com.bedatadriven.rebar.sql.client.SqlException;
import com.bedatadriven.rebar.sql.client.SqlResultCallback;
import com.bedatadriven.rebar.sql.client.SqlResultSet;
import com.bedatadriven.rebar.sql.client.SqlResultSetRow;
import com.bedatadriven.rebar.sql.client.SqlTransaction;
import com.bedatadriven.rebar.sql.client.query.SqlQuery;
import com.google.gwt.user.client.rpc.AsyncCallback;

/*
 * LocationId is LocationID (note two capitals at Id)
 */
public class LocationSearcher implements
		Searcher<Location> {

	@Override
	public void search(List<String> testQuery, SqlTransaction tx,
			final AsyncCallback<List<Integer>> callback) {
		final List<Integer> ids = new ArrayList<Integer>();
		String tableName = "Location";
		final String primaryKey = "LocationID";
		
		SqlQuery
				.select(primaryKey)
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
		return DimensionType.Location;
	}

}

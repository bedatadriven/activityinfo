package org.sigmah.shared.search;

import java.util.ArrayList;
import java.util.List;

import org.sigmah.shared.domain.AdminEntity;
import org.sigmah.shared.report.model.DimensionType;

import com.bedatadriven.rebar.sql.client.SqlResultCallback;
import com.bedatadriven.rebar.sql.client.SqlResultSet;
import com.bedatadriven.rebar.sql.client.SqlResultSetRow;
import com.bedatadriven.rebar.sql.client.SqlTransaction;
import com.bedatadriven.rebar.sql.client.query.SqlQuery;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class AdminEntitySearcher implements Searcher<AdminEntity> {
	
	@Override
	public void search(final String testQuery, SqlTransaction tx, final AsyncCallback<List<Integer>> callback) {
		SqlQuery.select("AdminEntityId")
		.from("AdminEntity")
		.onlyWhere("Name")
		.like(testQuery)
		
		.execute(tx, new SqlResultCallback() {
			final List<Integer> adminEntityIds = new ArrayList<Integer>();
			
			@Override
			public void onSuccess(SqlTransaction tx, SqlResultSet results) {
				for (SqlResultSetRow result : results.getRows()) {
					adminEntityIds.add(result.getInt("AdminEntityId"));
				}
				callback.onSuccess(adminEntityIds);
			}
		});
	}

	@Override
	public DimensionType getDimensionType() {
		return DimensionType.AdminLevel;
	}
}

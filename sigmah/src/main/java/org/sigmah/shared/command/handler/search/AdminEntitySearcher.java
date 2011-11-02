package org.sigmah.shared.command.handler.search;

import java.util.ArrayList;
import java.util.List;

import org.sigmah.server.database.hibernate.entity.AdminEntity;
import org.sigmah.shared.dto.AdminEntityDTO;
import org.sigmah.shared.report.model.DimensionType;

import com.bedatadriven.rebar.sql.client.SqlResultCallback;
import com.bedatadriven.rebar.sql.client.SqlResultSet;
import com.bedatadriven.rebar.sql.client.SqlResultSetRow;
import com.bedatadriven.rebar.sql.client.SqlTransaction;
import com.bedatadriven.rebar.sql.client.query.SqlQuery;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class AdminEntitySearcher implements Searcher {
	
	@Override
	public void search(final List<String> testQuery, SqlTransaction tx, final AsyncCallback<List<Integer>> callback) {
		SqlQuery.select("AdminEntityId")
		.from("AdminEntity")
		.whereLikes("Name")
		.likeMany(testQuery)
		
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

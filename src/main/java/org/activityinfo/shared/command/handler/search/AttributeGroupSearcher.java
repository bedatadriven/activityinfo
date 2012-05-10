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

public class AttributeGroupSearcher implements Searcher {

	@Override
	public void search(List<String> testQuery, SqlTransaction tx, final AsyncCallback<List<Integer>> callback) {
		final List<Integer> attributeGroupIds = new ArrayList<Integer>();

		SqlQuery
				.select("AttributeGroupId")
				.from("AttributeGroup")
				.whereLikes("Name")
				.likeMany(testQuery)
				
				.execute(tx, new SqlResultCallback() {
					@Override
					public void onSuccess(SqlTransaction tx, SqlResultSet results) {
						for (SqlResultSetRow row : results.getRows()) {
							attributeGroupIds.add(row.getInt("AttributeGroupId"));
						}
						callback.onSuccess(attributeGroupIds);
					}
					
					@Override
					public boolean onFailure(SqlException e) {
						return super.onFailure(e);
					}
				});
	}

	@Override
	public DimensionType getDimensionType() {
		return DimensionType.AttributeGroup;
	}

}

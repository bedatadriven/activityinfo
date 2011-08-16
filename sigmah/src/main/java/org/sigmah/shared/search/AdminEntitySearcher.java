package org.sigmah.shared.search;

import java.util.ArrayList;
import java.util.List;

import org.sigmah.shared.domain.AdminEntity;
import org.sigmah.shared.report.model.DimensionType;

import com.bedatadriven.rebar.sql.client.SqlException;
import com.bedatadriven.rebar.sql.client.SqlResultCallback;
import com.bedatadriven.rebar.sql.client.SqlResultSet;
import com.bedatadriven.rebar.sql.client.SqlResultSetRow;
import com.bedatadriven.rebar.sql.client.SqlTransaction;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class AdminEntitySearcher extends AbstractSearcher<AdminEntity> implements Searcher<AdminEntity> {

	@Override
	public void search(final String testQuery, SqlTransaction tx, final AsyncCallback<List<Integer>> callback) {
		String query = likeify(testQuery);
		final List<Integer> adminEntityIds = new ArrayList<Integer>();
		tx.executeSql("select AdminEntityId from adminentity where name like ?", new Object[]{query}, new SqlResultCallback() {
			@Override
			public void onSuccess(SqlTransaction tx, SqlResultSet results) {
				for (SqlResultSetRow result : results.getRows()) {
					adminEntityIds.add(result.getInt("AdminEntityId"));
				}
				callback.onSuccess(adminEntityIds);
			}

			@Override
			public boolean onFailure(SqlException e) {
				return super.onFailure(e);
			}
		});
	}

	@Override
	public DimensionType getDimensionType() {
		return DimensionType.AdminLevel;
	}
}

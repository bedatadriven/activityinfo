package org.sigmah.shared.search;

import java.util.ArrayList;
import java.util.List;

import org.sigmah.shared.domain.Project;
import org.sigmah.shared.report.model.DimensionType;

import com.bedatadriven.rebar.sql.client.SqlException;
import com.bedatadriven.rebar.sql.client.SqlResultCallback;
import com.bedatadriven.rebar.sql.client.SqlResultSet;
import com.bedatadriven.rebar.sql.client.SqlResultSetRow;
import com.bedatadriven.rebar.sql.client.SqlTransaction;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class ProjectSearcher extends AbstractSearcher<Project> implements Searcher<Project> {

	@Override
	public void search(String testQuery, SqlTransaction tx,
			final AsyncCallback<List<Integer>> callback) {
		final String query = likeify(testQuery);
		tx.executeSql("select ProjectId from project where name like ?", new Object[]{query}, new SqlResultCallback() {
			List<Integer> projectIds = new ArrayList<Integer>();
			
			@Override
			public void onSuccess(SqlTransaction tx, SqlResultSet results) {
				for (SqlResultSetRow row : results.getRows()) {
					projectIds.add(row.getInt("PartnerId"));
				}
				callback.onSuccess(projectIds);
			}

			@Override
			public boolean onFailure(SqlException e) {
				return super.onFailure(e);
			}
		});
	}

	@Override
	public DimensionType getDimensionType() {
		return DimensionType.Project;
	}

}

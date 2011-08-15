package org.sigmah.shared.search;

import java.util.ArrayList;
import java.util.List;

import org.sigmah.shared.domain.AdminEntity;
import org.sigmah.shared.report.model.DimensionType;

import com.bedatadriven.rebar.sql.client.SqlDatabase;
import com.bedatadriven.rebar.sql.client.SqlResultCallback;
import com.bedatadriven.rebar.sql.client.SqlResultSet;
import com.bedatadriven.rebar.sql.client.SqlResultSetRow;
import com.bedatadriven.rebar.sql.client.SqlTransaction;
import com.bedatadriven.rebar.sql.client.SqlTransactionCallback;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;

public class AdminEntitySearcher extends AbstractSearcher<AdminEntity> implements Searcher<AdminEntity> {
	@Inject
	public AdminEntitySearcher(SqlDatabase db) {
		super(db);
	}

	@Override
	public void search(final String testQuery, final AsyncCallback<List<Integer>> callback) {
		query = likeify(testQuery);
		db.transaction(new SqlTransactionCallback() {
			List<Integer> adminEntityIds = new ArrayList<Integer>();
			@Override
			public void begin(SqlTransaction tx) {
				tx.executeSql("select adminentityid from adminentity where name like ?", new Object[]{query}, new SqlResultCallback() {
					
					@Override
					public void onSuccess(SqlTransaction tx, SqlResultSet results) {
						for (SqlResultSetRow result : results.getRows()) {
							adminEntityIds.add(result.getInt("AdminEntityId"));
						}
						callback.onSuccess(adminEntityIds);
					}
				});
			}
		});

	}

	@Override
	public DimensionType getDimensionType() {
		return DimensionType.AdminLevel;
	}
}

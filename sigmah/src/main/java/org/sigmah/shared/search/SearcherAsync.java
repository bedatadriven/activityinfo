package org.sigmah.shared.search;

import org.sigmah.shared.dao.Filter;
import org.sigmah.shared.report.model.DimensionType;

import com.bedatadriven.rebar.sql.client.SqlDatabase;
import com.bedatadriven.rebar.sql.client.SqlResultCallback;
import com.bedatadriven.rebar.sql.client.SqlResultSet;
import com.bedatadriven.rebar.sql.client.SqlResultSetRow;
import com.bedatadriven.rebar.sql.client.SqlTransaction;
import com.bedatadriven.rebar.sql.client.SqlTransactionCallback;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;

public class SearcherAsync implements Searcher {

	SqlDatabase db;
	
	@Inject
	public SearcherAsync(SqlDatabase db) {
		this.db = db;
	}

	@Override
	public void search(final String testQuery, final AsyncCallback<Filter> callback) {
		final Filter resultFilter = new Filter();

		db.transaction(new SqlTransactionCallback() {
			@Override
			public void begin(SqlTransaction tx) {
				String query = "%" + testQuery + "%";
				tx.executeSql("select adminentityid from adminentity where name like '?'", new Object[]{query}, new SqlResultCallback() {
					
					@Override
					public void onSuccess(SqlTransaction tx, SqlResultSet results) {
						for (SqlResultSetRow result : results.getRows()) {
							resultFilter.addRestriction(DimensionType.AdminLevel, result.getInt("AdminEntityId"));
						}
						callback.onSuccess(resultFilter);
					}
				});
			}
			
		});
	}

}

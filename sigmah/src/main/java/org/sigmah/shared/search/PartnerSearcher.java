package org.sigmah.shared.search;

import java.util.ArrayList;
import java.util.List;

import org.sigmah.shared.dao.Filter;
import org.sigmah.shared.domain.OrgUnit;
import org.sigmah.shared.report.model.DimensionType;

import com.bedatadriven.rebar.sql.client.SqlDatabase;
import com.bedatadriven.rebar.sql.client.SqlException;
import com.bedatadriven.rebar.sql.client.SqlResultCallback;
import com.bedatadriven.rebar.sql.client.SqlResultSet;
import com.bedatadriven.rebar.sql.client.SqlResultSetRow;
import com.bedatadriven.rebar.sql.client.SqlTransaction;
import com.bedatadriven.rebar.sql.client.SqlTransactionCallback;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;

public class PartnerSearcher extends AbstractSearcher<OrgUnit> implements Searcher<OrgUnit> {

	@Inject
	public PartnerSearcher(SqlDatabase db) {
		super(db);
	}
	
	@Override
	public void search(String testQuery, final AsyncCallback<List<Integer>> callback, SqlTransaction tx) {
		final Filter resultFilter = new Filter();
		final String query = likeify(testQuery);
		
		db.transaction(new SqlTransactionCallback() {
			
			@Override
			public void begin(SqlTransaction tx) {
				tx.executeSql("select PartnerId from partner where name like ?", new Object[]{query}, new SqlResultCallback() {
					List<Integer> partnerIds = new ArrayList<Integer>();
					
					@Override
					public void onSuccess(SqlTransaction tx, SqlResultSet results) {
						for (SqlResultSetRow row : results.getRows()) {
							partnerIds.add(row.getInt("PartnerId"));
						}
						callback.onSuccess(partnerIds);
					}
				});
			}

			@Override
			public void onError(SqlException e) {
				super.onError(e);
			}
			
			
		});
	}

	@Override
	public DimensionType getDimensionType() {
		return DimensionType.Partner;
	}

}

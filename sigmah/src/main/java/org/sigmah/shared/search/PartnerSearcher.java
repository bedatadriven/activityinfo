package org.sigmah.shared.search;

import java.util.ArrayList;
import java.util.List;

import org.sigmah.shared.domain.OrgUnit;
import org.sigmah.shared.report.model.DimensionType;

import com.bedatadriven.rebar.sql.client.SqlException;
import com.bedatadriven.rebar.sql.client.SqlResultCallback;
import com.bedatadriven.rebar.sql.client.SqlResultSet;
import com.bedatadriven.rebar.sql.client.SqlResultSetRow;
import com.bedatadriven.rebar.sql.client.SqlTransaction;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class PartnerSearcher extends AbstractSearcher<OrgUnit> implements Searcher<OrgUnit> {

	@Override
	public void search(String testQuery, SqlTransaction tx, final AsyncCallback<List<Integer>> callback) {
		final String query = likeify(testQuery);
		tx.executeSql("select PartnerId from partner where name like ?", new Object[]{query}, new SqlResultCallback() {
			List<Integer> partnerIds = new ArrayList<Integer>();
			
			@Override
			public void onSuccess(SqlTransaction tx, SqlResultSet results) {
				for (SqlResultSetRow row : results.getRows()) {
					partnerIds.add(row.getInt("PartnerId"));
				}
				callback.onSuccess(partnerIds);
			}

			@Override
			public boolean onFailure(SqlException e) {
				// TODO Auto-generated method stub
				return super.onFailure(e);
			}
		});
	}

	@Override
	public DimensionType getDimensionType() {
		return DimensionType.Partner;
	}

}

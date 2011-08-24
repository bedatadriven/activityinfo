package org.sigmah.shared.search;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.sigmah.shared.dao.Filter;
import org.sigmah.shared.report.model.DimensionType;

import com.bedatadriven.rebar.sql.client.SqlDatabase;
import com.bedatadriven.rebar.sql.client.SqlException;
import com.bedatadriven.rebar.sql.client.SqlTransaction;
import com.bedatadriven.rebar.sql.client.SqlTransactionCallback;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class AllSearcher {
	private static List<Searcher> searchers = new ArrayList<Searcher>();
	private List<Searcher> failedSearchers = new ArrayList<Searcher>();
	private Filter filter = new Filter();
	SqlDatabase db;
	
	static {
		searchers.add(new GenericSearcher(DimensionType.Partner));
		searchers.add(new GenericSearcher(DimensionType.Project));
		searchers.add(new GenericSearcher(DimensionType.AttributeGroup));
		searchers.add(new LocationSearcher());
		searchers.add(new AdminEntitySearcher());
		//searchers.add(new SiteSearcher());
		searchers.add(new IndicatorSearcher());
	}
	
	public AllSearcher(SqlDatabase db) {
		this.db=db;
	}
	
	public void searchAll (final String query, final AsyncCallback<Filter> callback) {
		db.transaction(new SqlTransactionCallback() {
			@Override
			public void begin(SqlTransaction tx) {
				searchNext(query, searchers.iterator(), tx, new AsyncCallback<Filter>() {
					@Override
					public void onFailure(Throwable caught) {
						callback.onFailure(caught);
					}

					@Override
					public void onSuccess(Filter result) {
						callback.onSuccess(filter);
					}
				});
			}

			@Override
			public void onError(SqlException e) {
				callback.onFailure(e);
			}
		});
	}
	
	public void searchNext(final String q, final Iterator<Searcher> it,
			final SqlTransaction tx, final AsyncCallback<Filter> callback) {

		Filter filter = new Filter();
		final Searcher<?> searcher = it.next();
		
		searcher.search(q, tx, new AsyncCallback<List<Integer>>() {

			@Override
			public void onFailure(Throwable caught) {
				failedSearchers.add(searcher);
				System.out.println("Failed searcher: ");
				
				AllSearcher.this.continueOrYieldFilter(q, it, tx, callback);
			}

			@Override
			public void onSuccess(List<Integer> result) {
				addRestrictions(result);
				
				AllSearcher.this.continueOrYieldFilter(q, it, tx, callback);
			}
			
			private void addRestrictions(List<Integer> result) {
				for (Integer resultId : result) {
					AllSearcher.this.filter.addRestriction(searcher.getDimensionType(), resultId);
				}
			}
		});
	}
	
	private void continueOrYieldFilter(final String q,
			final Iterator<Searcher> it,
			final SqlTransaction tx,
			final AsyncCallback<Filter> callback) {
		
		if (it.hasNext()) {
			searchNext(q, it, tx, callback);
		} else {
			callback.onSuccess(filter);
		}
	}

}

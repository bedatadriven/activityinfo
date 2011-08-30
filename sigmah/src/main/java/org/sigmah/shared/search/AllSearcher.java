package org.sigmah.shared.search;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.sigmah.shared.dao.Filter;
import org.sigmah.shared.report.model.DimensionType;

import com.bedatadriven.rebar.sql.client.SqlTransaction;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class AllSearcher {
	private static List<Searcher<?>> searchers = new ArrayList<Searcher<?>>();
	private List<Searcher<?>> failedSearchers = new ArrayList<Searcher<?>>();
	private Filter filter = new Filter();
	SqlTransaction tx;
	
	static {
		searchers.add(new GenericSearcher(DimensionType.Partner));
		searchers.add(new GenericSearcher(DimensionType.Project));
		searchers.add(new GenericSearcher(DimensionType.AttributeGroup));
		searchers.add(new LocationSearcher());
		searchers.add(new AdminEntitySearcher());
		//searchers.add(new SiteSearcher());
		searchers.add(new IndicatorSearcher());
	}
	
	public AllSearcher(SqlTransaction tx) {
		this.tx=tx;
	}
	
	public static List<Searcher<?>> supportedSearchers() {
		return Collections.unmodifiableList(searchers);
	}
	
	public void searchAll (final String query, final AsyncCallback<Filter> callback) {
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
	
	public void searchNext(final String q, final Iterator<Searcher<?>> iterator,
			final SqlTransaction tx, final AsyncCallback<Filter> callback) {

		Filter filter = new Filter();
		final Searcher<?> searcher = iterator.next();
		
		searcher.search(q, tx, new AsyncCallback<List<Integer>>() {

			@Override
			public void onFailure(Throwable caught) {
				failedSearchers.add(searcher);
				System.out.println("Failed searcher: ");
				
				AllSearcher.this.continueOrYieldFilter(q, iterator, tx, callback);
			}

			@Override
			public void onSuccess(List<Integer> result) {
				addRestrictions(result);
				
				AllSearcher.this.continueOrYieldFilter(q, iterator, tx, callback);
			}
			
			private void addRestrictions(List<Integer> result) {
				for (Integer resultId : result) {
					AllSearcher.this.filter.addRestriction(searcher.getDimensionType(), resultId);
				}
			}
		});
	}
	
	private void continueOrYieldFilter(final String q,
			final Iterator<Searcher<?>> iterator,
			final SqlTransaction tx,
			final AsyncCallback<Filter> callback) {
		
		if (iterator.hasNext()) {
			searchNext(q, iterator, tx, callback);
		} else {
			callback.onSuccess(filter);
		}
	}

}

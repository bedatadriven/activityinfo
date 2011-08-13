package org.sigmah.shared.search;

import java.util.ArrayList;
import java.util.List;

import org.sigmah.shared.dao.Filter;

import com.bedatadriven.rebar.sql.client.SqlDatabase;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;

public class AllSearcher {
	private List<Searcher> searchers = new ArrayList<Searcher>();
	private List<Searcher> failedSearchers = new ArrayList<Searcher>();
	private SqlDatabase db;
	private String searchQuery;
	private Filter filter = new Filter();
	
	@Inject
	public AllSearcher(SqlDatabase db) {
		this.db=db;
	}
	
	public void searchAll(String searchQuery) {
		this.searchQuery=searchQuery;
		
		initSearchers();
		
		searchNext();
	}


	private void searchNext() {
		// Fail fast
		if (searchers.isEmpty()) {
			return;
		}
		
		final Searcher<?> searcher = searchers.get(0);
		searcher.search(searchQuery, new AsyncCallback<List<Integer>>() {

			@Override
			public void onFailure(Throwable caught) {
				failedSearchers.add(searcher);
				
				AllSearcher.this.searchers.remove(searcher);
				searchNext();
			}

			@Override
			public void onSuccess(List<Integer> result) {
				for (Integer resultId : result) {
					AllSearcher.this.getFilter().addRestriction(searcher.getDimensionType(), resultId);
				}
				
				AllSearcher.this.searchers.remove(searcher);
				searchNext();
			}
		});
	}

	private void initSearchers() {
		searchers.add(new PartnerSearcher(db));
		searchers.add(new AdminEntitySearcher(db));
	}

	public void setFilter(Filter filter) {
		this.filter = filter;
	}

	public Filter getFilter() {
		return filter;
	}
}

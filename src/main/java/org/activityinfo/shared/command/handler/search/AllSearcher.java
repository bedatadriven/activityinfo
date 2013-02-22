package org.activityinfo.shared.command.handler.search;

/*
 * #%L
 * ActivityInfo Server
 * %%
 * Copyright (C) 2009 - 2013 UNICEF
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the 
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public 
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.activityinfo.shared.command.Filter;
import org.activityinfo.shared.report.model.DimensionType;

import org.activityinfo.client.Log;
import com.bedatadriven.rebar.sql.client.SqlTransaction;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class AllSearcher {
	private static Map<DimensionType, Searcher> searchers = new HashMap<DimensionType, Searcher>();
	private List<Searcher> failedSearchers = new ArrayList<Searcher>();
	private Filter filter = new Filter(true); // create lenient filter - we want to have a wide search
	
	private final SqlTransaction tx;
	
	static {
		searchers.put(DimensionType.Partner, new GenericSearcher(DimensionType.Partner));
		searchers.put(DimensionType.Project, new GenericSearcher(DimensionType.Project));
		// searchers.put(DimensionType.AttributeGroup, new GenericSearcher(DimensionType.AttributeGroup));
		searchers.put(DimensionType.Location, new LocationSearcher());
		searchers.put(DimensionType.AdminLevel, new AdminEntitySearcher());
		// searchers.add(new SiteSearcher());
		searchers.put(DimensionType.Indicator, new IndicatorSearcher());
	}
	
	public AllSearcher(SqlTransaction tx) {
		this.tx=tx;
	}
	
	public static List<Searcher> supportedSearchers() {
		return Collections.unmodifiableList(new ArrayList<Searcher>(searchers.values()));
	}
	
	public void searchDimensions(Map<DimensionType, List<String>> searchTerms, final AsyncCallback<Filter> callback) {
		searchNextDimension(searchTerms.entrySet().iterator(), tx, new AsyncCallback<Filter>() {
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
	
	public void searchAll (final List<String> query, final AsyncCallback<Filter> callback) {
		searchNext(query, searchers.values().iterator(), tx, new AsyncCallback<Filter>() {
			@Override
			public void onFailure(Throwable caught) {
				callback.onFailure(caught);
			}

			@Override
			public void onSuccess(Filter result) {
				// TODO possible bug
				callback.onSuccess(filter);
			}
		});
	}
	
	public void searchNextDimension(final Iterator<Entry<DimensionType, List<String>>> iterator,
			final SqlTransaction tx, final AsyncCallback<Filter> callback) {

		final Entry<DimensionType, List<String>> entry = iterator.next();
		final Searcher searcher = searchers.get(entry.getKey());
		
		searcher.search(entry.getValue(), tx, new AsyncCallback<List<Integer>>() {
			@Override
			public void onFailure(Throwable caught) {
				failedSearchers.add(searcher);
				Log.trace("Failed searcher: ");
				AllSearcher.this.continueOrYieldFilterSpecific(iterator, tx, callback);
			}

			@Override
			public void onSuccess(List<Integer> result) {
				addRestrictions(result);
				AllSearcher.this.continueOrYieldFilterSpecific(iterator, tx, callback);
			}
			
			private void addRestrictions(List<Integer> result) {
				for (Integer resultId : result) {
					AllSearcher.this.filter.addRestriction(searcher.getDimensionType(), resultId);
				}
			}
		});
	}
	
	private void continueOrYieldFilterSpecific(final Iterator<Entry<DimensionType, List<String>>> iterator,
			final SqlTransaction tx,
			final AsyncCallback<Filter> callback) {
		
		if (iterator.hasNext()) {
			searchNextDimension(iterator, tx, callback);
		} else {
			callback.onSuccess(filter);
		}
	}
	
	public void searchNext(final List<String> q, final Iterator<Searcher> iterator,
			final SqlTransaction tx, final AsyncCallback<Filter> callback) {

		final Searcher searcher = iterator.next();
		searcher.search(q, tx, new AsyncCallback<List<Integer>>() {
			@Override
			public void onFailure(Throwable caught) {
				failedSearchers.add(searcher);
				Log.trace("Failed searcher: ");
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
	
	private void continueOrYieldFilter(final List<String> q,
			final Iterator<Searcher> iterator,
			final SqlTransaction tx,
			final AsyncCallback<Filter> callback) {
		
		if (iterator.hasNext()) {
			searchNext(q, iterator, tx, callback);
		} else {
			callback.onSuccess(filter);
		}
	}

}

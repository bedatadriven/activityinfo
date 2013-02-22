package org.activityinfo.client.page.search;

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

import java.util.List;
import java.util.Map;

import org.activityinfo.client.mvp.ListView;
import org.activityinfo.client.page.search.SearchPresenter.RecentSiteModel;
import org.activityinfo.shared.command.result.SearchResult;
import org.activityinfo.shared.command.result.SitePointList;
import org.activityinfo.shared.dto.SearchHitDTO;
import org.activityinfo.shared.report.content.PivotContent;
import org.activityinfo.shared.report.model.DimensionType;

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.inject.ImplementedBy;

@ImplementedBy(SearchResultsPage.class)
public interface SearchView extends ListView<SearchHitDTO, SearchResult> {
	HandlerRegistration addSearchHandler(SearchHandler handler);
	void setSearchResults(PivotContent pivotTabelData);
	void setSearchQuery(String query);
	void setFilter(Map<DimensionType, List<SearchResultEntity>> affectedEntities);

	void setSitePoints(SitePointList sitePoints);
	void setSites(List<RecentSiteModel> sites);
	
	// Since View<T> extends TakesValue<T>, the value does not need to be encapsulated
	public class SearchEvent extends GwtEvent<SearchHandler> {
		public static final Type<SearchHandler> TYPE = new Type<SearchHandler>(); 
		private String query;
		
		public SearchEvent(String query) {
			this.query = query;
		}

		public String getQuery() {
			return query;
		}

		public void setQuery(String query) {
			this.query = query;
		}

		@Override
		public Type<SearchHandler> getAssociatedType() {
			return TYPE;
		}

		@Override
		protected void dispatch(SearchHandler handler) {
			handler.onSearch(this);
		}
	}
	
	interface SearchHandler extends com.google.gwt.event.shared.EventHandler {
		void onSearch(SearchEvent searchEvent);
	}
}
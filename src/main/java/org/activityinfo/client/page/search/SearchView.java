package org.activityinfo.client.page.search;

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
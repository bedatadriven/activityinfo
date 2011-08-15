package org.sigmah.client.page.search;

import java.util.List;

import org.sigmah.client.mvp.ListView;
import org.sigmah.shared.command.result.SearchResult;
import org.sigmah.shared.dto.SearchHitDTO;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.inject.ImplementedBy;

@ImplementedBy(SearchResultsPage.class)
public interface SearchView extends ListView<SearchHitDTO, SearchResult> {

	void setLatestAdditions(List<SearchHitDTO> latestAdditions);

	HandlerRegistration addSearchHandler(SearchHandler handler);
	
	// Since View<T> extends TakesValue<T>, the value does not need to be encapsulated
	public class SearchEvent extends GwtEvent<SearchHandler> {
		public static Type TYPE = new Type<SearchHandler>(); 
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
	
	public interface SearchHandler extends EventHandler {
		void onSearch(SearchEvent searchEvent);
	}
}
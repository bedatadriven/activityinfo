package org.sigmah.client.page.search;

import org.sigmah.client.EventBus;
import org.sigmah.client.dispatch.Dispatcher;
import org.sigmah.client.page.NavigationCallback;
import org.sigmah.client.page.Page;
import org.sigmah.client.page.PageId;
import org.sigmah.client.page.PageState;
import org.sigmah.client.page.search.SearchView.SearchEvent;
import org.sigmah.shared.command.Search;
import org.sigmah.shared.command.result.SearchResult;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;

public class SearchPresenter implements SearchView.SearchHandler, Page {

	public static final PageId Search = new PageId("search");
	protected final Dispatcher service;
	protected final EventBus eventBus;
	protected final SearchView view;
	
	@Inject
	public SearchPresenter(Dispatcher service, EventBus eventBus,
			SearchView view) {
		this.service=service;
		this.eventBus=eventBus;
		this.view=view;
		
		view.addSearchHandler(this);
	}
	
	@Override
	public void onSearch(SearchEvent searchEvent) {
		view.setSearchQuery(searchEvent.getQuery());
		view.getLoadingMonitor().beforeRequest();
		
		service.execute(new Search(searchEvent.getQuery()), null, new AsyncCallback<SearchResult>() {
			@Override
			public void onFailure(Throwable caught) {
				// TODO handle failure
				view.getLoadingMonitor().onServerError();
			}

			@Override
			public void onSuccess(SearchResult result) {
				view.setSearchResults(result.getPivotTabelData());
				view.setFilter(result.getPivotTabelData().getEffectiveFilter());
				view.getLoadingMonitor().onCompleted();
			}
		});
	}

	@Override
	public void shutdown() {
	}

	@Override
	public PageId getPageId() {
		return Search;
	}

	@Override
	public Object getWidget() {
		return view.asWidget();
	}

	@Override
	public void requestToNavigateAway(PageState place,
			NavigationCallback callback) {
		callback.onDecided(true);
	}

	@Override
	public String beforeWindowCloses() {
		return null;
	}

	@Override
	public boolean navigate(PageState place) {
		if (place instanceof SearchPageState) {
			SearchPageState search = (SearchPageState)place;
			onSearch(new SearchEvent(search.getSearchQuery()));
		}
		return true;
	}
}

package org.activityinfo.client.page.search;

import org.activityinfo.client.inject.AppInjector;
import org.activityinfo.client.page.NavigationHandler;
import org.activityinfo.client.page.Page;
import org.activityinfo.client.page.PageId;
import org.activityinfo.client.page.PageLoader;
import org.activityinfo.client.page.PageState;
import org.activityinfo.client.page.PageStateSerializer;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import com.google.inject.Provider;

public class SearchLoader implements PageLoader {
	private Provider<SearchPresenter> searchPageProvider;
    
	@Inject
    public SearchLoader(
    		Provider<SearchPresenter> searchPage, 
    		NavigationHandler pageManager, 
    		PageStateSerializer placeSerializer) {
        this.searchPageProvider = searchPage;

        pageManager.registerPageLoader(SearchPresenter.SEARCH_PAGE_ID, this);
        placeSerializer.registerParser(SearchPresenter.SEARCH_PAGE_ID, new SearchPageState.Parser());
    }
	
	@Override
	public void load(final PageId pageId, final PageState pageState,
			final AsyncCallback<Page> callback) {
		
		GWT.runAsync(new RunAsyncCallback() {
			@Override
			public void onSuccess() {
				if (pageState instanceof SearchPageState && SearchPresenter.SEARCH_PAGE_ID.equals(pageId)) {
					SearchPageState searchPageState = (SearchPageState)pageState;
					SearchPresenter searchPage =  searchPageProvider.get();
					searchPage.setQuery(searchPageState.getSearchQuery());
                    callback.onSuccess(searchPage);
                }
			}
			
			@Override
			public void onFailure(Throwable reason) {
                callback.onFailure(reason);
			}
		});
	}
}

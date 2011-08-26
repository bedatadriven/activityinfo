package org.sigmah.client.page.search;

import org.sigmah.client.inject.AppInjector;
import org.sigmah.client.page.NavigationHandler;
import org.sigmah.client.page.Page;
import org.sigmah.client.page.PageId;
import org.sigmah.client.page.PageLoader;
import org.sigmah.client.page.PageState;
import org.sigmah.client.page.PageStateSerializer;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;

public class SearchLoader implements PageLoader {

	
	
    private AppInjector injector;
    
	@Inject
    public SearchLoader(AppInjector injector, NavigationHandler pageManager, PageStateSerializer placeSerializer) {
        this.injector = injector;

        pageManager.registerPageLoader(SearchPresenter.Search, this);
        placeSerializer.registerParser(SearchPresenter.Search, new SearchPageState.Parser());
    }
	
	@Override
	public void load(final PageId pageId, final PageState pageState,
			final AsyncCallback<Page> callback) {
		
		GWT.runAsync(new RunAsyncCallback() {
			
			@Override
			public void onSuccess() {
				if (pageState instanceof SearchPageState && SearchPresenter.Search.equals(pageId)) {
					SearchPageState searchPageState = (SearchPageState)pageState;
					SearchPresenter searchPage =  injector.getSearchPage();
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

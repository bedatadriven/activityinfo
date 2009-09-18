package org.activityinfo.client.page;

import org.activityinfo.client.Place;
import org.activityinfo.client.page.PagePresenter;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface PageLoader {

	/**
	 * Loads the PagePresenter for the given pageId, potentially asynchronously.
     *
     * @param pageId The ID of the page for which to load the presenter
     * @param place The place that should be loaded
     * @param callback 
     *
     * Note: PageLoaders are difficult to test so minimize the logic within this class.
     *
	 */
	public void load(PageId pageId, Place place, AsyncCallback<PagePresenter> callback);
	
}

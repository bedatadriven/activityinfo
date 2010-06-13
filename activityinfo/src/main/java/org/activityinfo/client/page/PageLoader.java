package org.activityinfo.client.page;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * Component responsible for
 */
public interface PageLoader {

	/**
	 * Loads the Page for the given pageId, potentially asynchronously.
     *
     * @param pageId The ID of the page for which to load the presenter
     * @param pageState A PageState object describing the requested state of the Page at load
     * @param callback 
     *
     * Note: PageLoaders are difficult to test so minimize the logic within this class.
     *
	 */
	public void load(PageId pageId, PageState pageState, AsyncCallback<Page> callback);
	
}

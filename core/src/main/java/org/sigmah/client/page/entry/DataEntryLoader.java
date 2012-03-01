/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.client.page.entry;

import org.sigmah.client.page.NavigationHandler;
import org.sigmah.client.page.Page;
import org.sigmah.client.page.PageId;
import org.sigmah.client.page.PageLoader;
import org.sigmah.client.page.PageState;
import org.sigmah.client.page.PageStateSerializer;
import org.sigmah.client.page.entry.place.DataEntryPlaceParser;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import com.google.inject.Provider;

public class DataEntryLoader implements PageLoader {
    private final Provider<DataEntryPage> dataEntryPageProvider;
    
    @Inject
    public DataEntryLoader(
    		NavigationHandler pageManager, 
    		PageStateSerializer placeSerializer,
    		Provider<DataEntryPage> dataEntryPageProvider) {
    	
        this.dataEntryPageProvider = dataEntryPageProvider;
        
        pageManager.registerPageLoader(DataEntryPage.PAGE_ID, this);       
        placeSerializer.registerParser(DataEntryPage.PAGE_ID, new DataEntryPlaceParser());
    }

    @Override
    public void load(final PageId pageId, final PageState pageState, final AsyncCallback<Page> callback) {
        GWT.runAsync(new RunAsyncCallback() {
            @Override
            public void onSuccess() {
                DataEntryPage dataEntryPage = dataEntryPageProvider.get();
                dataEntryPage.navigate(pageState);
				callback.onSuccess(dataEntryPage);
            }

            @Override
            public void onFailure(Throwable throwable) {
                callback.onFailure(throwable);
            }
        });
    }

}

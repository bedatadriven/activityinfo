

package org.activityinfo.client.page.entry;

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

import org.activityinfo.client.page.NavigationHandler;
import org.activityinfo.client.page.Page;
import org.activityinfo.client.page.PageId;
import org.activityinfo.client.page.PageLoader;
import org.activityinfo.client.page.PageState;
import org.activityinfo.client.page.PageStateSerializer;
import org.activityinfo.client.page.entry.place.DataEntryPlaceParser;

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

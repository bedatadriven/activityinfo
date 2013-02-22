

package org.activityinfo.client.page.report;

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

import org.activityinfo.client.dispatch.Dispatcher;
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

/**
 * Loader for the Report pages
 *
 * @author Alex Bertram
 */
public class ReportLoader implements PageLoader {

	private Provider<ReportsPage> reportsPage;
	private Provider<ReportDesignPage> reportDesignPage;

    @Inject
    public ReportLoader(Dispatcher service, NavigationHandler pageManager,
                        PageStateSerializer placeSerializer,
                        Provider<ReportsPage> reportsPage,
                        Provider<ReportDesignPage> reportDesignPage) {
        this.reportsPage = reportsPage;
        this.reportDesignPage = reportDesignPage;

        pageManager.registerPageLoader(ReportsPage.PAGE_ID, this);
        placeSerializer.registerStatelessPlace(ReportsPage.PAGE_ID, new ReportsPlace());
        
        pageManager.registerPageLoader(ReportDesignPage.PAGE_ID, this);
        placeSerializer.registerParser(ReportDesignPage.PAGE_ID, new ReportDesignPageState.Parser());
    }

    public void load(final PageId pageId, final PageState pageState, final AsyncCallback<Page> callback) {

        GWT.runAsync(new RunAsyncCallback() {
            @Override
            public void onFailure(Throwable reason) {
                callback.onFailure(reason);
            }

            @Override
            public void onSuccess() {
                if (ReportsPage.PAGE_ID.equals(pageId)) {
                    callback.onSuccess(reportsPage.get());

                } else if(ReportDesignPage.PAGE_ID.equals(pageId)) {
                	ReportDesignPage page = reportDesignPage.get();
                	page.navigate(pageState);
					callback.onSuccess(page);
					
                } else {
                    GWT.log("ReportLoader received a request it didn't know how to handle: " +
                            pageState.toString(), null);
                }
            }
        });
    }
}

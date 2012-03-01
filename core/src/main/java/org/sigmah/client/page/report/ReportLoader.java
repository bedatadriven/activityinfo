/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.client.page.report;

import org.sigmah.client.dispatch.Dispatcher;
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
        placeSerializer.registerStatelessPlace(ReportsPage.PAGE_ID, new ReportListPageState());
        
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

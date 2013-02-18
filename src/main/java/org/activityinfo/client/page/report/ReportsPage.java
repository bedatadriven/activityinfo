/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.activityinfo.client.page.report;

import org.activityinfo.client.EventBus;
import org.activityinfo.client.dispatch.Dispatcher;
import org.activityinfo.client.page.NavigationCallback;
import org.activityinfo.client.page.Page;
import org.activityinfo.client.page.PageId;
import org.activityinfo.client.page.PageState;
import org.activityinfo.client.page.report.json.ReportSerializer;

import com.extjs.gxt.ui.client.Style.LayoutRegion;
import com.extjs.gxt.ui.client.util.Margins;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.layout.BorderLayout;
import com.extjs.gxt.ui.client.widget.layout.BorderLayoutData;
import com.google.inject.Inject;

/**
 * Page which presents the list of reports visible to the user
 *
 * @author Alex Bertram
 */
public class ReportsPage extends LayoutContainer implements Page {
    public static final PageId PAGE_ID = new PageId("reports");


    @Inject
    public ReportsPage(EventBus eventBus, Dispatcher dispatcher, ReportSerializer reportSerializer) {

    	setLayout(new BorderLayout());
    	
    	BorderLayoutData newLayout = new BorderLayoutData(LayoutRegion.EAST);
    	newLayout.setSize(0.40f);
    	newLayout.setMargins(new Margins(0, 5, 0, 0));
    	newLayout.setSplit(true);
    	newLayout.setCollapsible(true);
    	add(new NewReportPanel(eventBus, dispatcher, reportSerializer), newLayout);
    
    	add(new ReportGridPanel(eventBus, dispatcher), new BorderLayoutData(LayoutRegion.CENTER));
    	
    		
    }

    @Override
	public void shutdown() {

    }


    @Override
	public PageId getPageId() {
        return PAGE_ID;
    }

    @Override
	public Object getWidget() {
        return this;
    }

    @Override
	public void requestToNavigateAway(PageState place, NavigationCallback callback) {
        callback.onDecided(true);
    }

    @Override
	public String beforeWindowCloses() {
        return null;
    }


    @Override
	public boolean navigate(PageState place) {
        return true;
    }


}

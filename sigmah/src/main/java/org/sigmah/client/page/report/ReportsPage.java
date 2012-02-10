/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.client.page.report;

import org.sigmah.client.EventBus;
import org.sigmah.client.dispatch.Dispatcher;
import org.sigmah.client.page.NavigationCallback;
import org.sigmah.client.page.Page;
import org.sigmah.client.page.PageId;
import org.sigmah.client.page.PageState;

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
    public static final PageId REPORT_HOME_PAGE_ID = new PageId("reports");


    @Inject
    public ReportsPage(EventBus eventBus, Dispatcher dispatcher) {

    	setLayout(new BorderLayout());
    	
    	BorderLayoutData savedLayout = new BorderLayoutData(LayoutRegion.EAST);
    	savedLayout.setSize(0.33f);
    	savedLayout.setMargins(new Margins(0, 5, 0, 0));
    	add(new SavedReportsPanel(eventBus, dispatcher), savedLayout);
    	
    	ContentPanel templatesPanel = new ContentPanel();
    	templatesPanel.setHeading("Report Templates");
    	add(templatesPanel, new BorderLayoutData(LayoutRegion.CENTER));
    		
    }

    @Override
	public void shutdown() {

    }

//    public void onSelectionChanged(ReportDefinitionDTO selectedItem) {
//    }

    @Override
	public PageId getPageId() {
        return REPORT_HOME_PAGE_ID;
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

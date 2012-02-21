/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.client.page.welcome;

import org.sigmah.client.EventBus;
import org.sigmah.client.dispatch.Dispatcher;
import org.sigmah.client.page.NavigationCallback;
import org.sigmah.client.page.Page;
import org.sigmah.client.page.PageId;
import org.sigmah.client.page.PageState;
import org.sigmah.client.page.welcome.portlets.NewsPortlet;
import org.sigmah.client.page.welcome.portlets.ReportPortlet;
import org.sigmah.shared.command.GetReports;
import org.sigmah.shared.command.result.ReportsResult;
import org.sigmah.shared.dto.ReportMetadataDTO;

import com.extjs.gxt.ui.client.widget.custom.Portal;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;

public class DashboardPage extends Portal implements Page {


    public static final PageId PAGE_ID = new PageId("dashboard");
	private Dispatcher dispatcher;
	private EventBus eventBus;

    @Inject
    public DashboardPage(Dispatcher dispatcher, EventBus eventBus) {
    	super(2);
    	this.dispatcher = dispatcher;
    	this.eventBus = eventBus;
    	
        setBorders(true);  
        setStyleAttribute("backgroundColor", "white");  
        setColumnWidth(0, .63);  
        setColumnWidth(1, .33);  
      
        add(new NewsPortlet(), 1);
        
        loadReports();
        
    }
    
    private void loadReports() {
    	dispatcher.execute(new GetReports(), null, new AsyncCallback<ReportsResult>() {

			@Override
			public void onFailure(Throwable caught) {
				
			}

			@Override
			public void onSuccess(ReportsResult result) {
				for(ReportMetadataDTO report : result.getData()) {
					if(report.isDashboard()) {
						add(new ReportPortlet(dispatcher, eventBus, report), 0);
					}
				}
				layout();
			}
		});
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
	public void shutdown() {

    }

    @Override
	public boolean navigate(PageState place) {
        return true;
    }
}
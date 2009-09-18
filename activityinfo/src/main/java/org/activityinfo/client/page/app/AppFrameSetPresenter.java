package org.activityinfo.client.page.app;

import org.activityinfo.client.EventBus;
import org.activityinfo.client.Place;
import org.activityinfo.client.command.CommandService;
import org.activityinfo.client.command.callback.Got;
import org.activityinfo.client.command.monitor.AsyncMonitor;
import org.activityinfo.client.common.action.UIActions;
import org.activityinfo.client.event.NavigationEvent;
import org.activityinfo.client.page.*;
import org.activityinfo.client.page.charts.ChartPlace;
import org.activityinfo.client.page.charts.Charts;
import org.activityinfo.client.page.config.AccountPlace;
import org.activityinfo.client.page.entry.SiteGridPlace;
import org.activityinfo.client.page.map.MapHomePlace;
import org.activityinfo.client.page.map.Maps;
import org.activityinfo.client.page.report.ReportHomePlace;
import org.activityinfo.client.page.table.PivotPlace;
import org.activityinfo.client.page.welcome.WelcomePlace;
import org.activityinfo.shared.command.GetSchema;
import org.activityinfo.shared.dto.ActivityModel;
import org.activityinfo.shared.dto.Schema;

import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.Window;
import com.google.inject.Inject;
import com.google.inject.ImplementedBy;

public class AppFrameSetPresenter implements FrameSetPresenter {


    @ImplementedBy(AppFrameSet.class)
    public interface View {
		public void bindPresenter(AppFrameSetPresenter frameSetPresenter);
		public AsyncMonitor showLoadingPlaceHolderInTab(String tabId);
		public void showTab(String id, PagePresenter page);
		void noActivitiesDefined();
	}
	
	private final View view;
    private final EventBus eventBus;
    private final CommandService service;
    
    /**
     * Maps Page class to tab id
     */

    private PagePresenter activePage;
    

    @Inject
	public AppFrameSetPresenter(EventBus eventBus, CommandService service, View view) {
		super();
        this.eventBus = eventBus;
		this.view = view;
		this.service = service;
		this.view.bindPresenter(this);

	}

    @Override
    public PageId getPageId() {
        return Pages.AppFrameSet;
    }

    @Override
    public Object getWidget() {
        return view;
    }

    public void onTabClicked(String tabId) {

        if(tabId.equals(Pages.Welcome.toString())) {

            eventBus.fireEvent(new NavigationEvent(PageManager.NavigationRequested, new WelcomePlace()));

        } else if(tabId.equals(Pages.DataEntryFrameSet.toString())) {

            eventBus.fireEvent(new NavigationEvent(PageManager.NavigationRequested, new SiteGridPlace()));

        } else if(tabId.equals(Pages.ReportHome.toString())) {

            eventBus.fireEvent(new NavigationEvent(PageManager.NavigationRequested, new ReportHomePlace()));

        } else if(tabId.equals(Pages.ConfigFrameSet.toString())) {
            
            eventBus.fireEvent(new NavigationEvent(PageManager.NavigationRequested, new AccountPlace()));

        }  else if(tabId.equals(Maps.Home.toString())) {

            eventBus.fireEvent(new NavigationEvent(PageManager.NavigationRequested, new MapHomePlace()));
            
        }  else if(tabId.equals(Charts.Charts.toString())) {

            eventBus.fireEvent(new NavigationEvent(PageManager.NavigationRequested, new ChartPlace()));

        }  else if(tabId.equals(Pages.Pivot.toString())) {

            eventBus.fireEvent(new NavigationEvent(PageManager.NavigationRequested, new PivotPlace())); 
        }
	}

    public void onUIAction(String actionId) {
        if(UIActions.logout.equals(actionId)) {
            // TODO: move this elsewhere
            Cookies.removeCookie("authToken");
            Cookies.removeCookie("email");
            Window.Location.assign("/?logout");
        }
    }

    private String getTabId(PageId pageId) {
        if(pageId instanceof Charts.ChartPageId) {
            return Charts.Charts.toString();
        } else if(pageId instanceof Maps.MapId) {
            return Maps.Home.toString();
        } else if(pageId.equals(Pages.ReportPreview)) {
            return Pages.ReportHome.toString();
        } else {
            return pageId.toString();
        }
    }

    @Override
    public AsyncMonitor showLoadingPlaceHolder(int regionId, PageId pageId, Place loadingPlace) {
        return view.showLoadingPlaceHolderInTab(getTabId(pageId));
    }

    @Override
	public void setActivePage(int regionId, PagePresenter page) {

        view.showTab(getTabId(page.getPageId()), page);

		this.activePage = page;
	}

    @Override
    public PagePresenter getActivePage(int regionId) {
        return this.activePage;
    }

	protected void onDataEntryTabClicked() {
		
		service.execute(new GetSchema(), null, new Got<Schema>() {

			@Override
			public void got(Schema result) {
				
				ActivityModel activity = result.getFirstActivity();
				if(activity == null) {
					view.noActivitiesDefined();
				} else {
					eventBus.fireEvent(new NavigationEvent(PageManager.NavigationRequested,
							new SiteGridPlace(activity)));
				}
			}
		});
	}


    @Override
    public void requestToNavigateAway(Place place, NavigationCallback callback) {
        callback.onDecided(true);
    }

    @Override
    public String beforeWindowCloses() {
        return null;
    }

    public void shutdown() {

    }

    public boolean navigate(Place place) {
        return true;
    }
}

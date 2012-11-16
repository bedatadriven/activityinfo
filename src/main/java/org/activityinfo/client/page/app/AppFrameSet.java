/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.activityinfo.client.page.app;

import org.activityinfo.client.EventBus;
import org.activityinfo.client.dispatch.AsyncMonitor;
import org.activityinfo.client.event.NavigationEvent;
import org.activityinfo.client.offline.ui.SyncStatusBar;
import org.activityinfo.client.page.Frame;
import org.activityinfo.client.page.NavigationCallback;
import org.activityinfo.client.page.NavigationHandler;
import org.activityinfo.client.page.Page;
import org.activityinfo.client.page.PageId;
import org.activityinfo.client.page.PageState;
import org.activityinfo.client.page.config.DbListPageState;
import org.activityinfo.client.page.dashboard.DashboardPlace;
import org.activityinfo.client.page.entry.place.DataEntryPlace;
import org.activityinfo.client.page.report.ReportsPlace;
import org.activityinfo.client.page.search.SearchPageState;
import org.activityinfo.client.widget.LoadingPlaceHolder;
import org.activityinfo.login.shared.AuthenticatedUser;

import org.activityinfo.client.Log;
import com.extjs.gxt.ui.client.Style.LayoutRegion;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.widget.Viewport;
import com.extjs.gxt.ui.client.widget.layout.BorderLayout;
import com.extjs.gxt.ui.client.widget.layout.BorderLayoutData;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.inject.Singleton;



@Singleton
public class AppFrameSet implements Frame {

	private EventBus eventBus;
	private Viewport viewport;

	private Widget activeWidget;
	private Page activePage;
	private AppBar appBar;
	private SyncStatusBar statusBar;

	@Inject
	public AppFrameSet(EventBus eventBus, AuthenticatedUser auth, AppBar appBar, 
			SyncStatusBar statusBar) {

		Log.trace("AppFrameSet constructor starting");

		this.eventBus = eventBus;
		this.appBar = appBar;
		this.statusBar = statusBar;
		
		viewport = new Viewport();
		viewport.setLayout(new BorderLayout());

		setupTabs();
		setupStatus();

		Log.trace("AppFrameSet constructor finished, about to add to RootPanel");

		RootPanel.get().add(viewport);

		Log.trace("AppFrameSet now added to RootPanel");

	}

	private void setupTabs() {
		appBar.getSectionTabStrip().addSelectionHandler(new SelectionHandler<Section>() {

			@Override
			public void onSelection(SelectionEvent<Section> event) {
				onSectionClicked(event.getSelectedItem());
			}
		});
		eventBus.addListener(NavigationHandler.NavigationAgreed, new Listener<NavigationEvent>() {

			@Override
			public void handleEvent(NavigationEvent event) {
				appBar.getSectionTabStrip().setSelection(event.getPlace().getSection());				
			}
			
		});
		BorderLayoutData layout = new BorderLayoutData(LayoutRegion.NORTH);
		layout.setSize(AppBar.HEIGHT);
	
		viewport.add(appBar, layout);
	}
	
	private void setupStatus() {
		BorderLayoutData layout = new BorderLayoutData(LayoutRegion.SOUTH);
		layout.setSize(SyncStatusBar.HEIGHT);
				
		viewport.add(statusBar, layout);
	}

	private void onSectionClicked(Section selectedItem) {
		switch(selectedItem) {
		case HOME:
			eventBus.fireEvent(new NavigationEvent(NavigationHandler.NavigationRequested, new DashboardPlace()));
			break;
		case DATA_ENTRY:
			eventBus.fireEvent(new NavigationEvent(NavigationHandler.NavigationRequested, new DataEntryPlace()));
			break;
		case ANALYSIS:
			eventBus.fireEvent(new NavigationEvent(NavigationHandler.NavigationRequested, new ReportsPlace()));
			break;
		case DESIGN:
			eventBus.fireEvent(new NavigationEvent(NavigationHandler.NavigationRequested, new DbListPageState()));
			break;
		}

	}

	protected void search(String value) {
		eventBus.fireEvent(new NavigationEvent(NavigationHandler.NavigationRequested, new SearchPageState(value)));
	}

	public void setWidget(Widget widget) {

		if (activeWidget != null) {
			viewport.remove(activeWidget);
		}
		viewport.add(widget, new BorderLayoutData(LayoutRegion.CENTER));
		activeWidget = widget;
		viewport.layout();
	}

	@Override
	public void setActivePage(Page page) {
		setWidget((Widget) page.getWidget());
		activePage = page;
	}

	@Override
	public Page getActivePage() {
		return activePage;
	}

	@Override
	public AsyncMonitor showLoadingPlaceHolder(PageId pageId, PageState loadingPlace) {
		activePage = null;
		LoadingPlaceHolder placeHolder = new LoadingPlaceHolder();
		setWidget(placeHolder);
		return placeHolder;
	}

	@Override
	public PageId getPageId() {
		return null;
	}

	@Override
	public Object getWidget() {
		return viewport;
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
		appBar.getSectionTabStrip().setSelection(place.getSection());
		return true;
	}

	@Override
	public void shutdown() {

	}

}

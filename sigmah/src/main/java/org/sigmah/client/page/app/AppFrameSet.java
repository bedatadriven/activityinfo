/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.client.page.app;

import org.sigmah.client.EventBus;
import org.sigmah.client.dispatch.AsyncMonitor;
import org.sigmah.client.event.NavigationEvent;
import org.sigmah.client.offline.ui.OfflineView;
import org.sigmah.client.page.Frame;
import org.sigmah.client.page.NavigationCallback;
import org.sigmah.client.page.NavigationHandler;
import org.sigmah.client.page.Page;
import org.sigmah.client.page.PageId;
import org.sigmah.client.page.PageState;
import org.sigmah.client.page.common.SearchField;
import org.sigmah.client.page.common.widget.LoadingPlaceHolder;
import org.sigmah.client.page.config.DbListPageState;
import org.sigmah.client.page.dashboard.DashboardPlace;
import org.sigmah.client.page.entry.place.DataEntryPlace;
import org.sigmah.client.page.report.ReportListPageState;
import org.sigmah.client.page.search.SearchPageState;
import org.sigmah.shared.auth.AuthenticatedUser;

import com.allen_sauer.gwt.log.client.Log;
import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.FieldEvent;
import com.extjs.gxt.ui.client.event.KeyListener;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.widget.TabPanel;
import com.extjs.gxt.ui.client.widget.Viewport;
import com.extjs.gxt.ui.client.widget.layout.RowData;
import com.extjs.gxt.ui.client.widget.layout.RowLayout;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.inject.Singleton;



@Singleton
public class AppFrameSet implements Frame {

	private EventBus eventBus;
	private Viewport viewport;

	private TabPanel tabPanel;
	private AuthenticatedUser auth;
	private OfflineView offlineMenu;

	private Widget activeWidget;
	private Page activePage;
	private AppBar appBar;


	@Inject
	public AppFrameSet(EventBus eventBus, AuthenticatedUser auth, OfflineView offlineMenu) {

		Log.trace("AppFrameSet constructor starting");

		this.eventBus = eventBus;
		this.auth = auth;
		this.offlineMenu = offlineMenu;

		viewport = new Viewport();
		viewport.setLayout(new RowLayout());

		createToolBar();

		Log.trace("AppFrameSet constructor finished, about to add to RootPanel");

		RootPanel.get().add(viewport);

		Log.trace("AppFrameSet now added to RootPanel");

	}

	private void createToolBar() {
		appBar = new AppBar();
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
		viewport.add(appBar, new RowData(1.0, AppBar.HEIGHT));
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
			eventBus.fireEvent(new NavigationEvent(NavigationHandler.NavigationRequested, new ReportListPageState()));
			break;
		case DESIGN:
			eventBus.fireEvent(new NavigationEvent(NavigationHandler.NavigationRequested, new DbListPageState()));
			break;
		}

	}

	private void addSearchBox() {
		final SearchField searchBox = new SearchField();
		searchBox.addKeyListener(new KeyListener() {
			@Override
			public void componentKeyUp(ComponentEvent event) {
				super.componentKeyUp(event);
				if (event.getKeyCode() == KeyCodes.KEY_ENTER) {
					search(searchBox.getValue());
				}
			}
		});
		searchBox.addListener(Events.TriggerClick, new Listener<FieldEvent>() {
			@Override
			public void handleEvent(FieldEvent be) {
				search(searchBox.getValue());
			}
		});

		//topBar.add(searchBox);
	}

	protected void search(String value) {
		eventBus.fireEvent(new NavigationEvent(NavigationHandler.NavigationRequested, new SearchPageState(value)));
	}

	private void addNavLink(String text, AbstractImagePrototype icon, final PageState place) {
		//		TabItem tab = new TabItem(text);
		//		tab.setIcon(icon);
		//		tabPanel.add(tab);
		//		tab.add
		//        Button button = new Button(text, icon, new SelectionListener<ButtonEvent>() {
		//            @Overridelement.
		//            public void componentSelected(ButtonEvent ce) {
		//                eventBus.fireEvent(new NavigationEvent(NavigationHandler.NavigationRequested, place));
		//            }
		//        });
		//        topBar.add(button);
	}

	public void setWidget(Widget widget) {

		if (activeWidget != null) {
			viewport.remove(activeWidget);
		}
		viewport.add(widget, new RowData(1.0, 1.0));
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

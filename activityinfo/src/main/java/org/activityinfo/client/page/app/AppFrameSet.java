/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007, 2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package org.activityinfo.client.page.app;

import com.allen_sauer.gwt.log.client.Log;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.Viewport;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.layout.RowData;
import com.extjs.gxt.ui.client.widget.layout.RowLayout;
import com.extjs.gxt.ui.client.widget.toolbar.FillToolItem;
import com.extjs.gxt.ui.client.widget.toolbar.LabelToolItem;
import com.extjs.gxt.ui.client.widget.toolbar.SeparatorToolItem;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.activityinfo.client.Application;
import org.activityinfo.client.EventBus;
import org.activityinfo.client.Place;
import org.activityinfo.client.command.Authentication;
import org.activityinfo.client.command.monitor.AsyncMonitor;
import org.activityinfo.client.event.NavigationEvent;
import org.activityinfo.client.offline.ui.OfflineMenu;
import org.activityinfo.client.page.*;
import org.activityinfo.client.page.charts.ChartPlace;
import org.activityinfo.client.page.common.widget.LoadingPlaceHolder;
import org.activityinfo.client.page.config.DbListPlace;
import org.activityinfo.client.page.entry.SiteGridPlace;
import org.activityinfo.client.page.map.MapHomePlace;
import org.activityinfo.client.page.report.ReportHomePlace;
import org.activityinfo.client.page.table.PivotPlace;
import org.activityinfo.client.page.welcome.WelcomePlace;


@Singleton
public class AppFrameSet  implements FrameSetPresenter {

    private EventBus eventBus;
    private Viewport viewport;
//    private TabPanel tabPanel;
//    private TabItem dataEntryTab;
    private ToolBar topBar;
    private Authentication auth;
    private OfflineMenu offlineMenu;

    private Widget activeWidget;
    private PagePresenter activePage;
//
//    private boolean isUserTabSelection = true;

    @Inject
    public AppFrameSet(EventBus eventBus, Authentication auth, OfflineMenu offlineMenu) {

        Log.trace("AppFrameSet constructor starting");

        this.eventBus = eventBus;
        this.auth = auth;
        this.offlineMenu = offlineMenu;

        viewport = new Viewport();
        viewport.setLayout(new RowLayout());

        createToolBar();
        //createCenter();

        Log.trace("AppFrameSet constructor finished, about to add to RootPanel");

        RootPanel.get().add(viewport);

        Log.trace("AppFrameSet now added to RootPanel");

    }

    private void createToolBar() {

        topBar = new ToolBar();

        LabelToolItem appTitleItem = new LabelToolItem("ActivityInfo");
        appTitleItem.setStyleName("appTitle");
        topBar.add(appTitleItem);

        topBar.add(new SeparatorToolItem());

        addNavLink("Bienvenue", null, new WelcomePlace());
        addNavLink(Application.CONSTANTS.dataEntry(), Application.ICONS.dataEntry(), new SiteGridPlace());
        addNavLink(Application.CONSTANTS.reports(), Application.ICONS.report(), new ReportHomePlace());
        addNavLink("Graphiques", Application.ICONS.barChart(), new ChartPlace());
        addNavLink(Application.CONSTANTS.maps(), Application.ICONS.map(), new MapHomePlace());
        addNavLink("Tableaux", Application.ICONS.table(), new PivotPlace());
        addNavLink(Application.CONSTANTS.setup(), Application.ICONS.setup(), new DbListPlace());

        topBar.add(new FillToolItem());

        LabelToolItem emailLabel = new LabelToolItem(auth.getEmail());
        emailLabel.setStyleAttribute("fontWeight", "bold");
        topBar.add(emailLabel);

        topBar.add(offlineMenu);

        Button logoutTool = new Button(Application.CONSTANTS.logout(), new SelectionListener<ButtonEvent>() {
            @Override
            public void componentSelected(ButtonEvent ce) {
                // TODO: this needs to go elsewhere
                Cookies.removeCookie("authToken");
                Cookies.removeCookie("email");
                Window.Location.reload();
            }
        });
        topBar.add(logoutTool);

//        BorderLayoutData data = new BorderLayoutData(LayoutRegion.NORTH, 25);
//        data.setMargins(new Margins());
//
        viewport.add(topBar, new RowData(1.0, 30));
    }

    private void addNavLink(String text, AbstractImagePrototype icon, final Place place) {
        Button button = new Button(text, icon, new SelectionListener<ButtonEvent>() {
            @Override
            public void componentSelected(ButtonEvent ce) {
                eventBus.fireEvent(new NavigationEvent(PageManager.NavigationRequested, place));
            }
        });
        topBar.add(button);
    }
//
//    private void createCenter() {
//
//        tabPanel = new TabPanel();
//        viewport.add(tabPanel, new BorderLayoutData(LayoutRegion.CENTER));
//
//        Listener<ComponentEvent> clickListener = new Listener<ComponentEvent>() {
//            public void handleEvent(ComponentEvent be) {
//                if(be.getEventTypeInt() == Event.ONCLICK) {
//                    if(be.getComponent() == tabPanel.getSelectedItem()) {
//                        presenter.onTabClicked(be.getComponent().getItemId());
//                    }
//                }
//            }
//        };
//
//        TabItem welcomeTab = new TabItem("Welcome");
//        welcomeTab.setItemId(Pages.Welcome.toString());
//        tabPanel.add(welcomeTab);
//
//        dataEntryTab = new TabItem(Application.CONSTANTS.dataEntry());
//        dataEntryTab.setIcon(Application.ICONS.dataEntry());
//        dataEntryTab.setItemId(Pages.DataEntryFrameSet.toString());
//        tabPanel.add(dataEntryTab);
//
//        TabItem reportTab = new TabItem(Application.CONSTANTS.reports());
//        reportTab.setIcon(Application.ICONS.report());
//        reportTab.setItemId(Pages.ReportHome.toString());
//        tabPanel.add(reportTab);
//
//        TabItem chartTab = new TabItem("Graphiques");
//        chartTab.setIcon(Application.ICONS.barChart());
//        chartTab.setItemId(Charts.Charts.toString());
//        chartTab.addListener(Events.BrowserEvent, clickListener);
//        tabPanel.add(chartTab);
//
//        TabItem mapTab = new TabItem(Application.CONSTANTS.maps());
//        mapTab.setIcon(Application.ICONS.map());
//        mapTab.setItemId(Maps.Home.toString());
//        mapTab.addListener(Events.BrowserEvent, clickListener);
//        tabPanel.add(mapTab);
//
//        TabItem tableTab = new TabItem("Tableaux");
//        tableTab.setIcon(Application.ICONS.table());
//        tableTab.setItemId(Pages.Pivot.toString());
//        tabPanel.add(tableTab);
//
//        TabItem designTab = new TabItem(Application.CONSTANTS.setup());
//        designTab.setIcon(Application.ICONS.setup());
//        designTab.setItemId(Pages.ConfigFrameSet.toString());
//        tabPanel.add(designTab);
//
//        tabPanel.addListener(Events.BeforeSelect, new Listener<TabPanelEvent>() {
//            @Override
//            public void handleEvent(TabPanelEvent be) {
//
//                /*
//                 * We want to cancel the event here:
//                 * if it is ok to navigate away from the current tab then
//                 * the AppPresenter will call showTab()
//                 */
//
//                if(isUserTabSelection) {
//                    be.setCancelled(true);
//                    if(presenter!=null){
//                    	presenter.onTabClicked(be.getItem().getItemId());
//                    }
//                }
//            }
//        });
//    }

//    public String getActiveTabId() {
//        return tabPanel.getSelectedItem().getItemId();
//    }

//    public void setTabContent(String id, Widget widget) {
//        TabItem tab = tabPanel.getSelectedItem();
//        if(tab!=null)
//            tab.removeAll();
//
//        tab = tabPanel.getItemByItemId(id);
//        tab.setLayout(new FitLayout());
//        tab.add(widget);
//
//        isUserTabSelection = false;
//        tabPanel.setSelection(tabPanel.getItemByItemId(id));
//        isUserTabSelection = true;
//
//        tab.layout();
//    }

    public void setWidget(Widget widget) {

        if(activeWidget!=null) {
            viewport.remove(activeWidget);
        }
        viewport.add(widget, new RowData(1.0, 1.0));
        activeWidget = widget;
        viewport.layout();
    }

    @Override
    public void setActivePage(int regionId, PagePresenter page) {
        setWidget((Widget) page.getWidget());
        activePage = page;
    }

    @Override
    public PagePresenter getActivePage(int regionId) {
        return activePage;
    }

    @Override
    public AsyncMonitor showLoadingPlaceHolder(int regionId, PageId pageId, Place loadingPlace) {
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
    public void requestToNavigateAway(Place place, NavigationCallback callback) {
        callback.onDecided(true);
    }

    @Override
    public String beforeWindowCloses() {
        return null;
    }

    @Override
    public boolean navigate(Place place) {
        return true;
    }

    @Override
    public void shutdown() {

    }

}

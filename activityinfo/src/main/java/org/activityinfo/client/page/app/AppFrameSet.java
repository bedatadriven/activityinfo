/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007, 2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package org.activityinfo.client.page.app;

import com.extjs.gxt.ui.client.Style.LayoutRegion;
import com.extjs.gxt.ui.client.util.Margins;
import com.extjs.gxt.ui.client.widget.*;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.extjs.gxt.ui.client.widget.toolbar.LabelToolItem;
import com.extjs.gxt.ui.client.widget.toolbar.FillToolItem;
import com.extjs.gxt.ui.client.widget.layout.BorderLayoutData;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.layout.BorderLayout;
import com.extjs.gxt.ui.client.event.*;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.Event;
import com.google.inject.Singleton;
import com.google.inject.Inject;

import org.activityinfo.client.Application;
import org.activityinfo.client.EventBus;
import org.activityinfo.client.command.monitor.AsyncMonitor;
import org.activityinfo.client.command.Authentication;
import org.activityinfo.client.common.action.UIActions;
import org.activityinfo.client.page.PagePresenter;
import org.activityinfo.client.page.Pages;
import org.activityinfo.client.page.base.LoadingPlaceHolder;
import org.activityinfo.client.page.charts.Charts;
import org.activityinfo.client.page.map.Maps;


@Singleton
public class AppFrameSet extends BaseObservable implements AppFrameSetPresenter.View {

    private EventBus eventBus;
    private Viewport viewport;
    private TabPanel tabPanel;
    private TabItem dataEntryTab;
    private ToolBar topBar;
    private Authentication auth;
    //private OfflineMenuButton offlineMenuButton;
    
    private AppFrameSetPresenter presenter;

    private boolean isUserTabSelection = true;

    @Inject
    public AppFrameSet(EventBus eventBus, Authentication auth) {

        this.eventBus = eventBus;
        this.auth = auth;
   //     this.offlineMenuButton = offlineButton;

        viewport = new Viewport();
        viewport.setLayout(new BorderLayout());

        createNorth();
        createCenter();
        
        RootPanel.get().add(viewport);
    }

    @Override
    public void bindPresenter(AppFrameSetPresenter presenter) {
    	this.presenter = presenter;
    }

    private void createNorth() {

        topBar = new ToolBar();

        SelectionListener listener = new SelectionListener()  {
              @Override
              public void componentSelected(ComponentEvent ce) {
                  if(presenter!=null) {
                      presenter.onUIAction(ce.getComponent().getItemId());
                  }
              }
          };


        LabelToolItem appTitleItem = new LabelToolItem("ActivityInfo");
        appTitleItem.setStyleName("appTitle");
        topBar.add(appTitleItem);

        topBar.add(new FillToolItem());

        LabelToolItem emailLabel = new LabelToolItem(auth.getEmail());
        emailLabel.setStyleAttribute("fontWeight", "bold");
        topBar.add(emailLabel);

        //topBar.add(offlineMenuButton);

        Button logoutTool = new Button(Application.CONSTANTS.logout(),listener);
        logoutTool.setItemId(UIActions.logout);
        topBar.add(logoutTool);

        BorderLayoutData data = new BorderLayoutData(LayoutRegion.NORTH, 25);
        data.setMargins(new Margins());
        
        viewport.add(topBar, data);
    }


    private void createCenter() {

        tabPanel = new TabPanel();
        viewport.add(tabPanel, new BorderLayoutData(LayoutRegion.CENTER));

        Listener<ComponentEvent> clickListener = new Listener<ComponentEvent>() {
            public void handleEvent(ComponentEvent be) {
                if(be.getEventTypeInt() == Event.ONCLICK) {
                    if(be.getComponent() == tabPanel.getSelectedItem()) {
                        presenter.onTabClicked(be.getComponent().getItemId());
                    }
                }
            }
        };

        TabItem welcomeTab = new TabItem("Welcome");
        welcomeTab.setItemId(Pages.Welcome.toString());
        tabPanel.add(welcomeTab);

        dataEntryTab = new TabItem(Application.CONSTANTS.dataEntry());
        dataEntryTab.setIcon(Application.ICONS.dataEntry());
        dataEntryTab.setItemId(Pages.DataEntryFrameSet.toString());
        tabPanel.add(dataEntryTab);

        TabItem reportTab = new TabItem(Application.CONSTANTS.reports());
        reportTab.setIcon(Application.ICONS.report());
        reportTab.setItemId(Pages.ReportHome.toString());
        tabPanel.add(reportTab);

        TabItem chartTab = new TabItem("Graphiques");
        chartTab.setIcon(Application.ICONS.barChart());
        chartTab.setItemId(Charts.Charts.toString());
        chartTab.addListener(Events.BrowserEvent, clickListener);
        tabPanel.add(chartTab);                 

        TabItem mapTab = new TabItem(Application.CONSTANTS.maps());
        mapTab.setIcon(Application.ICONS.map());
        mapTab.setItemId(Maps.Home.toString());
        mapTab.addListener(Events.BrowserEvent, clickListener);
        tabPanel.add(mapTab);

        TabItem tableTab = new TabItem("Tableaux");
        tableTab.setIcon(Application.ICONS.table());
        tableTab.setItemId(Pages.Pivot.toString());
        tabPanel.add(tableTab);

        TabItem designTab = new TabItem(Application.CONSTANTS.setup());
        designTab.setIcon(Application.ICONS.setup());
        designTab.setItemId(Pages.ConfigFrameSet.toString());
        tabPanel.add(designTab);

        tabPanel.addListener(Events.BeforeSelect, new Listener<TabPanelEvent>() {
            @Override
            public void handleEvent(TabPanelEvent be) {

                /*
                 * We want to cancel the event here:
                 * if it is ok to navigate away from the current tab then
                 * the AppPresenter will call showTab()
                 */

                if(isUserTabSelection) {
                    be.setCancelled(true);
                    if(presenter!=null){
                    	presenter.onTabClicked(be.getItem().getItemId());
                    }
                }
            }
        });
    }

    public String getActiveTabId() {
        return tabPanel.getSelectedItem().getItemId();
    }

    public void setTabContent(String id, Widget widget) {
        TabItem tab = tabPanel.getSelectedItem();
        if(tab!=null)
            tab.removeAll();

        tab = tabPanel.getItemByItemId(id);
        tab.setLayout(new FitLayout());
        tab.add(widget);

        isUserTabSelection = false;
        tabPanel.setSelection(tabPanel.getItemByItemId(id));
        isUserTabSelection = true;

        tab.layout();
    }

	@Override
	public void showTab(String tabId, PagePresenter page) {

        setTabContent(tabId, (Widget)page.getWidget());
	}

    @Override
    public AsyncMonitor showLoadingPlaceHolderInTab(String tabId) {

        LoadingPlaceHolder placeHolder = new LoadingPlaceHolder();
        setTabContent(tabId, placeHolder);

        return placeHolder;
    }

    @Override
	public void noActivitiesDefined() {
		
		com.google.gwt.user.client.Window.alert("Vous n'avez pas encore defini des activités, et vous n'avez aucune accés a une base de données existent.");
		
	}



}

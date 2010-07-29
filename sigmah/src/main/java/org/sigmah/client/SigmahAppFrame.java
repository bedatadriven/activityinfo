/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.client;

import com.allen_sauer.gwt.log.client.Log;
import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.google.gwt.user.client.Element;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import org.sigmah.client.dispatch.AsyncMonitor;
import org.sigmah.client.dispatch.remote.Authentication;
import org.sigmah.client.event.NavigationEvent;
import org.sigmah.client.i18n.I18N;
import org.sigmah.client.offline.ui.OfflineMenu;
import org.sigmah.client.page.*;
import org.sigmah.client.page.common.widget.LoadingPlaceHolder;
import org.sigmah.client.page.dashboard.DashboardPresenter;
import org.sigmah.client.ui.SigmahViewport;
import org.sigmah.client.ui.TabBar;
import org.sigmah.client.ui.TabModel;

/**
 * Main frame of Sigmah.
 * @author rca
 */
public class SigmahAppFrame implements Frame {
    /**
     * Height of the page header.
     * TODO: Find a way to calculate this instead of using a constant.
     */
    private final static int CLUTTER_HEIGHT = 83;
    
    private Page activePage;

    private SigmahViewport view;
    private final TabModel tabModel;
    
    @Inject
    public SigmahAppFrame(EventBus eventBus, Authentication auth, OfflineMenu offlineMenu) {
        final DockPanel dockPanel = new DockPanel();
        dockPanel.setSize("100%", "100%");
        
        final HorizontalPanel header = new HorizontalPanel();
        header.setStyleName("header");
        
        // Initializing the main toolbar
        final HorizontalPanel toolbar = new HorizontalPanel();
        toolbar.setStyleName("toolbar");
        toolbar.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
        
        final Label label = new Label(auth.getEmail());
        toolbar.add(label);
        
        final Button reportButton = new Button(I18N.CONSTANTS.bugReport());
        toolbar.add(reportButton);
        
        final Button helpButton = new Button(I18N.CONSTANTS.help());
        toolbar.add(helpButton);
        
        final Button logoutButton = new Button(I18N.CONSTANTS.logout(), new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                Cookies.removeCookie("authToken");
                Cookies.removeCookie("email");
                Window.Location.reload();
            }
        });
        toolbar.add(logoutButton);
        
        header.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
        header.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        header.add(toolbar);
        
        header.setHeight("50px");
        dockPanel.add(header, DockPanel.NORTH);
        
        final DockPanel mainPanel = new DockPanel();
        mainPanel.setSize("100%", "100%");
        
        tabModel = new TabModel();
        final TabBar tabBar = new TabBar(tabModel);
        tabModel.add(I18N.CONSTANTS.dashboard(), DashboardPresenter.PAGE_ID.toString(), false);
        tabBar.setHeight("32px");
        mainPanel.add(tabBar, DockPanel.NORTH);
        
        this.view = new SigmahViewport(CLUTTER_HEIGHT);
        this.view.setLayout(new FitLayout());
        this.view.syncSize();
        
        mainPanel.add(this.view, DockPanel.CENTER);
        
        dockPanel.add(mainPanel, DockPanel.CENTER);
        
        RootPanel.get().add(dockPanel);
    }
    
    @Override
    public void setActivePage(Page page) {
        if(page instanceof TabPage)
            tabModel.add(((TabPage)page).getTabTitle(), page.getPageId().toString(), true);
        
        Log.debug("Page courante : " + page.getClass());
        
        final Widget widget = (Widget) page.getWidget();
        view.removeAll();
        view.add(widget);
        view.layout();
        
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
        return placeHolder;
    }

    @Override
    public PageId getPageId() {
        return null;
    }

    @Override
    public Object getWidget() {
        return view;
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

    @Override
    public void shutdown() {

    }

}

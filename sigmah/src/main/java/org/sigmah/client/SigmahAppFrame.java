/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.client;

import com.allen_sauer.gwt.log.client.Log;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
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
import org.sigmah.client.page.dashboard.DashboardPageState;
import org.sigmah.client.page.project.ProjectState;
import org.sigmah.client.ui.SigmahViewport;
import org.sigmah.client.ui.Tab;
import org.sigmah.client.ui.TabBar;
import org.sigmah.client.ui.TabModel;

/**
 * Main frame of Sigmah.
 * @author rca
 */
public class SigmahAppFrame implements Frame {
    private Page activePage;

    private SigmahViewport view;
    
    @Inject
    public SigmahAppFrame(EventBus eventBus, Authentication auth, OfflineMenu offlineMenu, final TabModel tabModel) {
        final RootPanel header = RootPanel.get("header");
        header.addStyleName("header");
        
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
        
        header.add(toolbar);
        
        final TabBar tabBar = new TabBar(tabModel, eventBus);
        tabModel.add(I18N.CONSTANTS.dashboard(), new DashboardPageState(), false);
        
        final RootPanel tabs = RootPanel.get("tabs");
        tabs.addStyleName("tab-bar");
        tabs.add(tabBar);
        
        eventBus.addListener(NavigationHandler.NavigationAgreed, new Listener<NavigationEvent>() {
            @Override
            public void handleEvent(NavigationEvent be) {
                final PageState state = be.getPlace();
                final String title;
                if(state instanceof TabPage)
                    title = ((TabPage) state).getTabTitle();
                else
                    title = I18N.CONSTANTS.title();
                
                final Tab tab = tabModel.add(title, be.getPlace(), true);
                
                if(state instanceof HasTab)
                    ((HasTab)state).setTab(tab);
            }
        });
        
        int clutterHeight = getDecorationHeight();
        
        this.view = new SigmahViewport(clutterHeight);
        this.view.setLayout(new FitLayout());
        this.view.syncSize();
        
        RootPanel.get("content").add(this.view);
    }
    
    private native int getDecorationHeight() /*-{
        var height = 0;

        var elements = $wnd.document.getElementsByClassName("decoration");
        for(var index = 0; index < elements.length; index++) {
            var style = $wnd.getComputedStyle(elements[index], null);
            height += parseInt(style.height) + 
                      parseInt(style.borderTopWidth) +
                      parseInt(style.borderBottomWidth) +
                      parseInt(style.marginTop) +
                      parseInt(style.marginBottom) +
                      parseInt(style.paddingTop) +
                      parseInt(style.paddingBottom);
        }

        return height;
    }-*/;
    
    @Override
    public void setActivePage(Page page) {
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

/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.client;

import org.sigmah.client.dispatch.AsyncMonitor;
import org.sigmah.client.dispatch.Dispatcher;
import org.sigmah.client.dispatch.remote.Authentication;
import org.sigmah.client.event.NavigationEvent;
import org.sigmah.client.i18n.I18N;
import org.sigmah.client.offline.ui.OfflineView;
import org.sigmah.client.page.Frame;
import org.sigmah.client.page.HasTab;
import org.sigmah.client.page.NavigationCallback;
import org.sigmah.client.page.NavigationHandler;
import org.sigmah.client.page.Page;
import org.sigmah.client.page.PageId;
import org.sigmah.client.page.PageState;
import org.sigmah.client.page.TabPage;
import org.sigmah.client.page.common.widget.LoadingPlaceHolder;
import org.sigmah.client.page.dashboard.DashboardPageState;
import org.sigmah.client.page.login.LoginView;
import org.sigmah.client.ui.SigmahViewport;
import org.sigmah.client.ui.Tab;
import org.sigmah.client.ui.TabBar;
import org.sigmah.client.ui.TabModel;
import org.sigmah.shared.command.GetUserInfo;
import org.sigmah.shared.dto.UserInfoDTO;
import org.sigmah.shared.dto.value.FileUploadUtils;

import com.allen_sauer.gwt.log.client.Log;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;

/**
 * Main frame of Sigmah.
 * 
 * @author Raphaël Calabro (rcalabro@ideia.fr)
 */
public class SigmahAppFrame implements Frame {
    private Page activePage;

    private SigmahViewport view;

    @Inject
    public SigmahAppFrame(EventBus eventBus, final Authentication auth, OfflineView offlineMenu,
            final TabModel tabModel, final Dispatcher dispatcher, final UserInfo info) {

        if (auth == null) {
            RootPanel.get().add(new LoginView());
            RootPanel.get("loading").getElement().removeFromParent();

        } else {
            // The user is already logged in
            RootPanel.get("username").add(new Label(auth.getEmail()));

            final Anchor reportButton = new Anchor(I18N.CONSTANTS.bugReport());
            RootPanel.get("bugreport").add(reportButton);

            final Anchor helpButton = new Anchor(I18N.CONSTANTS.help());
            RootPanel.get("help").add(helpButton);

            // Logout action
            final Anchor logoutButton = new Anchor(I18N.CONSTANTS.logout());
            logoutButton.addClickHandler(new ClickHandler() {
                @Override
                public void onClick(ClickEvent event) {
                    Cookies.removeCookie("authToken", "/");
                    Window.Location.reload();
                }
            });
            RootPanel.get("logout").add(logoutButton);

            // Tab bar
            final TabBar tabBar = new TabBar(tabModel, eventBus);
            final Tab dashboardTab = tabModel.add(I18N.CONSTANTS.dashboard(), new DashboardPageState(), false);
            tabBar.addTabStyleName(tabModel.indexOf(dashboardTab), "home");

            final RootPanel tabs = RootPanel.get("tabs");
            tabs.add(tabBar);

            eventBus.addListener(NavigationHandler.NavigationAgreed, new Listener<NavigationEvent>() {
                @Override
                public void handleEvent(NavigationEvent be) {
                    final PageState state = be.getPlace();
                    final String title;
                    if (state instanceof TabPage)
                        title = ((TabPage) state).getTabTitle();
                    else
                        title = I18N.CONSTANTS.title();

                    final Tab tab = tabModel.add(title, be.getPlace(), true);

                    if (state instanceof HasTab)
                        ((HasTab) state).setTab(tab);
                }
            });

            int clutterHeight = getDecorationHeight();

            // Configure Ext-GWT viewport
            this.view = new SigmahViewport(0, clutterHeight);
            this.view.setLayout(new FitLayout());
            this.view.syncSize();
            this.view.setBorders(true);

            RootPanel.get("content").add(this.view);

            // Gets user's info.
            dispatcher.execute(new GetUserInfo(auth.getUserId()), null, new AsyncCallback<UserInfoDTO>() {

                @Override
                public void onFailure(Throwable e) {
                    Log.error("[execute] Error while getting the organization for user #id " + auth.getUserId() + ".",
                            e);
                }

                @Override
                public void onSuccess(UserInfoDTO result) {

                    if (result != null) {

                        info.setUserInfo(result);

                        // Sets organization parameters.
                        RootPanel.get("orgname").getElement()
                                .setInnerHTML(result.getOrganization().getName().toUpperCase());
                        RootPanel
                                .get("orglogo")
                                .getElement()
                                .setAttribute(
                                        "style",
                                        "background-image: url(" + GWT.getModuleBaseURL() + "image-provider?"
                                                + FileUploadUtils.IMAGE_URL + "=" + result.getOrganization().getLogo()
                                                + ")");
                    }
                }
            });
        }
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

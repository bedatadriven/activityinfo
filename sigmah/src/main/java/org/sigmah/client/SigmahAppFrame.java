/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.client;

import com.allen_sauer.gwt.log.client.Log;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import org.sigmah.client.dispatch.AsyncMonitor;
import org.sigmah.client.dispatch.Dispatcher;
import org.sigmah.client.dispatch.remote.Authentication;
import org.sigmah.client.event.NavigationEvent;
import org.sigmah.client.i18n.I18N;
import org.sigmah.client.offline.ui.OfflineView;
import org.sigmah.client.page.*;
import org.sigmah.client.page.common.widget.LoadingPlaceHolder;
import org.sigmah.client.page.dashboard.DashboardPageState;
import org.sigmah.client.page.login.LoginView;
import org.sigmah.client.ui.SigmahViewport;
import org.sigmah.client.ui.Tab;
import org.sigmah.client.ui.TabBar;
import org.sigmah.client.ui.TabModel;
import org.sigmah.shared.command.GetOrganization;
import org.sigmah.shared.dto.OrganizationDTO;
import org.sigmah.shared.dto.value.FileUploadUtils;

/**
 * Main frame of Sigmah.
 * 
 * @author Raphaël Calabro (rcalabro@ideia.fr)
 */
public class SigmahAppFrame implements Frame {
    private Page activePage;

    private SigmahViewport view;

    @Inject
    public SigmahAppFrame(EventBus eventBus, Authentication auth, OfflineView offlineMenu, final TabModel tabModel,
            final Dispatcher dispatcher) {

        if(auth == null) {
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
                    try {
                        final RequestBuilder requestBuilder = new RequestBuilder(RequestBuilder.GET, "/SigmahAuthToken?remove");
                        requestBuilder.setCallback(new RequestCallback() {

                            @Override
                            public void onResponseReceived(Request request, Response response) {
                                Window.Location.reload();
                            }

                            @Override
                            public void onError(Request request, Throwable exception) {
                                MessageBox.alert(I18N.CONSTANTS.logoutErrorTitle(), exception.getMessage(), null);
                            }
                        });
                        
                        requestBuilder.send();
                    } catch (RequestException ex) {
                        MessageBox.alert(I18N.CONSTANTS.logoutErrorTitle(), ex.getMessage(), null);
                    }
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

            // Gets user's organization.
            final int userId = auth.getUserId();
            final GetOrganization command = new GetOrganization();
            command.setUserId(userId);
            dispatcher.execute(command, null, new AsyncCallback<OrganizationDTO>() {

                @Override
                public void onFailure(Throwable e) {
                    Log.error("[execute] Error while getting the organization for user #id " + userId + ".", e);
                }

                @Override
                public void onSuccess(OrganizationDTO r) {

                    if (r != null) {

                        // Sets organization parameters.
                        RootPanel.get("orgname").getElement().setInnerHTML(r.getName().toUpperCase());
                        RootPanel
                                .get("orglogo")
                                .getElement()
                                .setAttribute(
                                        "style",
                                        "background-image: url(" + GWT.getModuleBaseURL() + "image-provider?"
                                                + FileUploadUtils.IMAGE_URL + "=" + r.getLogo() + ")");
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

/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.client;

import com.allen_sauer.gwt.log.client.Log;
import com.extjs.gxt.ui.client.widget.Viewport;
import com.extjs.gxt.ui.client.widget.layout.RowData;
import com.extjs.gxt.ui.client.widget.layout.RowLayout;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import org.sigmah.client.dispatch.AsyncMonitor;
import org.sigmah.client.dispatch.remote.Authentication;
import org.sigmah.client.offline.ui.OfflineMenu;
import org.sigmah.client.page.*;
import org.sigmah.client.page.common.widget.LoadingPlaceHolder;

/**
 * Main frame of Sigmah.
 * @author rca
 */
public class SigmahAppFrame implements Frame {
    private EventBus eventBus;
    private Viewport viewport;

    private Widget activeWidget;
    private Page activePage;

    @Inject
    public SigmahAppFrame(EventBus eventBus, Authentication auth, OfflineMenu offlineMenu) {

        Log.trace("AppFrameSet constructor starting");

        this.eventBus = eventBus;

        viewport = new Viewport();
        viewport.setLayout(new RowLayout());

        viewport.addText(auth.getEmail());

        Log.trace("AppFrameSet constructor finished, about to add to RootPanel");

        RootPanel.get().add(viewport);

        Log.trace("AppFrameSet now added to RootPanel");
    }

    /**
     * Defines the current shown widget.
     * @param widget The widget to display.
     */
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
        return true;
    }

    @Override
    public void shutdown() {

    }

}

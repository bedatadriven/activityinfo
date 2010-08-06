/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.client.page.common.widget;

import com.extjs.gxt.ui.client.Style;
import com.extjs.gxt.ui.client.util.Margins;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.layout.BorderLayout;
import com.extjs.gxt.ui.client.widget.layout.BorderLayoutData;
import com.google.gwt.user.client.ui.Widget;
import org.sigmah.client.dispatch.AsyncMonitor;
import org.sigmah.client.page.*;
import org.sigmah.client.page.common.nav.NavigationPanel;

public class VSplitFrameSet implements Frame {

    protected final LayoutContainer container;
    private Page activePage;
    private Widget activeWidget;
    private NavigationPanel navPanel;
    private PageId pageId;
    private String tabTitle;

    public VSplitFrameSet(PageId pageId, NavigationPanel navPanel) {
        this.pageId = pageId;
        this.container = new LayoutContainer();
        this.navPanel = navPanel;
        container.setLayout(new BorderLayout());

        addNavigationPanel();
    }

    public void shutdown() {
        navPanel.shutdown();
    }

    protected void addNavigationPanel() {

        BorderLayoutData layoutData = new BorderLayoutData(Style.LayoutRegion.WEST);
        layoutData.setSplit(true);
        layoutData.setCollapsible(true);
        layoutData.setMargins(new Margins(0, 5, 0, 0));

        container.add(navPanel, layoutData);
    }

    @Override
    public Widget getWidget() {
        return container;
    }

    @Override
    public Page getActivePage() {
        return activePage;
    }

    private void setWidget(Widget widget) {

        if (activeWidget != null) {
            container.remove(activeWidget);
        }

        container.add(widget, new BorderLayoutData(Style.LayoutRegion.CENTER));
        activeWidget = widget;

        if (container.isRendered()) {
            container.layout();
        }
    }

    @Override
    public AsyncMonitor showLoadingPlaceHolder(PageId page, PageState loadingPlace) {

        LoadingPlaceHolder placeHolder = new LoadingPlaceHolder();
        setWidget(placeHolder);
        activePage = null;
        return placeHolder;
    }

    @Override
    public void setActivePage(Page page) {

        setWidget((Widget) page.getWidget());
        activePage = page;
    }

    @Override
    public void requestToNavigateAway(PageState place, NavigationCallback callback) {
        if (activePage == null) {
            callback.onDecided(true);
        } else {
            activePage.requestToNavigateAway(place, callback);
        }
    }

    @Override
    public String beforeWindowCloses() {
        return null;
    }

    public boolean navigate(PageState place) {
        return true;
    }

    public PageId getPageId() {
        return pageId;
    }
}

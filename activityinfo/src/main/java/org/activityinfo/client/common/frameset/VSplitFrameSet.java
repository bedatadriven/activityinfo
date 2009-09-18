package org.activityinfo.client.common.frameset;

import org.activityinfo.client.Place;
import org.activityinfo.client.ViewPath;
import org.activityinfo.client.command.monitor.AsyncMonitor;
import org.activityinfo.client.common.nav.NavigationPanel;
import org.activityinfo.client.page.FrameSetPresenter;
import org.activityinfo.client.page.NavigationCallback;
import org.activityinfo.client.page.PageId;
import org.activityinfo.client.page.PagePresenter;
import org.activityinfo.client.page.base.LoadingPlaceHolder;

import com.extjs.gxt.ui.client.Style;
import com.extjs.gxt.ui.client.util.Margins;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.layout.BorderLayout;
import com.extjs.gxt.ui.client.widget.layout.BorderLayoutData;
import com.google.gwt.user.client.ui.Widget;

import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;

public class VSplitFrameSet implements FrameSetPresenter {

    protected final LayoutContainer container;
    private PagePresenter activePage;
    private Widget activeWidget;
    private NavigationPanel navPanel;
    private PageId pageId;

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

    private Style.LayoutRegion getLayoutRegion(int regionId) {
        if(regionId == ViewPath.SideBar) {
            return Style.LayoutRegion.EAST;
        } else {
            return Style.LayoutRegion.CENTER;
        }
    }

    @Override
    public PagePresenter getActivePage(int regionId) {
        return activePage;
    }

    private void setWidget(Widget widget) {

        if(activeWidget!=null) {
            container.remove(activeWidget);
        }

        container.add(widget, new BorderLayoutData(Style.LayoutRegion.CENTER));
        activeWidget= widget;

        if(container.isRendered()) {
            container.layout();
        }
    }

    @Override
    public AsyncMonitor showLoadingPlaceHolder(int regionId, PageId page, Place loadingPlace) {

        LoadingPlaceHolder placeHolder = new LoadingPlaceHolder();
        setWidget(placeHolder);
        activePage = null;
        return placeHolder;
    }

    @Override
	public void setActivePage(int regionId, PagePresenter page) {

		setWidget((Widget)page.getWidget());
		activePage = page;
	}

    @Override
    public void requestToNavigateAway(Place place, NavigationCallback callback) {
       if(activePage ==null) {
            callback.onDecided(true);
       } else {
            activePage.requestToNavigateAway(place, callback);
       }
    }

    @Override
    public String beforeWindowCloses() {
        return null;
    }

    public boolean navigate(Place place) {
        return true;
    }

    public PageId getPageId() {
        return pageId;
    }
}

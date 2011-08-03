package org.sigmah.client.page.dashboard;

import org.sigmah.client.page.NavigationCallback;
import org.sigmah.client.page.Page;
import org.sigmah.client.page.PageId;
import org.sigmah.client.page.PageState;
import org.sigmah.client.page.dashboard.portlets.PortletPresenter;

import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.custom.Portal;

public class DashboardView extends LayoutContainer implements DashboardPresenter.View, Page {
	private Portal portal = new Portal(3);
	
	public DashboardView() {
		super();
		
		add(portal);
	}

	@Override
	public void shutdown() {
	}

	@Override
	public PageId getPageId() {
		return DashboardPresenter.Dashboard;
	}

	@Override
	public Object getWidget() {
		return this;
	}

	@Override
	public void requestToNavigateAway(PageState place,
			NavigationCallback callback) {
        callback.onDecided(true);
	}

	@Override
	public String beforeWindowCloses() {
		return null;
	}

	@Override
	public boolean navigate(PageState place) {
		return false;
	}

	@Override
	public void addPortlet(PortletPresenter presenter) {
		portal.add(presenter.getView().asPortlet(), 0);
	}
}
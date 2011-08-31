package org.sigmah.client.page.dashboard;

import org.sigmah.client.page.NavigationCallback;
import org.sigmah.client.page.Page;
import org.sigmah.client.page.PageId;
import org.sigmah.client.page.PageState;
import org.sigmah.client.page.dashboard.portlets.PortletView;
import org.sigmah.shared.dto.portlets.PortletDTO;

import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.custom.Portal;

public class DashboardView extends LayoutContainer implements DashboardPresenter.View, Page {
	private Portal portal;
	
	public DashboardView() {
		super();
		
		initializeComponent();
	}

	private void initializeComponent() {
		int amountColumns = 3;
		portal = new Portal(amountColumns);
		for (int i=0; i<amountColumns; i++) {
			portal.setColumnWidth(i, 1 / amountColumns);
		}
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
	public void addPortlet(PortletView<PortletDTO> portletView) {
		portal.add(portletView.asPortlet(), portletView.getValue().column());
	}
}
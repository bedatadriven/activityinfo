package org.sigmah.client.page.dashboard;

import org.sigmah.client.page.dashboard.portlets.PortletPresenter;

import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.custom.Portal;

public class DashboardView extends LayoutContainer implements DashboardPresenter.View {
	private Portal portal = new Portal(3);
	
	public DashboardView() {
		super();
		
		add(portal);
	}

	@Override
	public void addPortlet(PortletPresenter presenter) {
		portal.add(presenter.getView().asPortlet(), 0);
	}
}
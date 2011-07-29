package org.sigmah.client.page.dashboard;

import org.sigmah.client.page.dashboard.portlets.PortletViewFactory;
import org.sigmah.shared.dto.DashboardDTO;
import org.sigmah.shared.dto.portlets.PortletDTO;

import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.custom.Portal;

public class DashboardView extends LayoutContainer implements DashboardPresenter.View {
	private DashboardDTO dashboard;
	private PortletViewFactory factory = new PortletViewFactory();
	private Portal portal = new Portal(3);
	
	@Override
	public DashboardDTO getDashboard() {
		return dashboard;
	}
	
	private void init() {
		for (PortletDTO portlet : dashboard.getPortlets()) {
			portal.add(factory.fromPortlet(portlet).asPortlet(), 0);
		}
	}

}

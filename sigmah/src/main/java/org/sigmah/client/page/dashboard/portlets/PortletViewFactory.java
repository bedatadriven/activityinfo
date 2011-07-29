package org.sigmah.client.page.dashboard.portlets;

import org.sigmah.shared.dto.portlets.NoGpsLocations;
import org.sigmah.shared.dto.portlets.PortletDTO;


/*
 * Returns a PortletView from a portlet
 */
public class PortletViewFactory {
	public PortletView fromPortlet(PortletDTO portlet) {
		if (portlet instanceof NoGpsLocations) {
//			return new NoGpsSitesPresenter.NoGpsSitesPortletView((NoGpsLocations) portlet);
		}
		
		
		return null;
	}
}

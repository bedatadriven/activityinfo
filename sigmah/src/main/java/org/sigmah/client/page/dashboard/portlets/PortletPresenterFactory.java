package org.sigmah.client.page.dashboard.portlets;

import org.sigmah.client.dispatch.Dispatcher;
import org.sigmah.shared.dto.portlets.FavoritesDTO;
import org.sigmah.shared.dto.portlets.NoGpsLocationsDTO;
import org.sigmah.shared.dto.portlets.PortletDTO;

import com.google.inject.Inject;


/*
 * Returns a PortletPresenter from a portlet
 */
public class PortletPresenterFactory {
	Dispatcher service;
	
	@Inject
	public PortletPresenterFactory(Dispatcher service) {
		this.service = service;
	}

	public PortletPresenter fromPortlet(PortletDTO portlet) {
		if (portlet instanceof NoGpsLocationsDTO) {
			return new NoGpsLocationsPresenter(service, (NoGpsLocationsDTO) portlet); 
		}
		
		if (portlet instanceof FavoritesDTO) {
//			return new FavoritesPresenter((FavoritesDTO) portlet);
		}
		
		return null;
	}
}

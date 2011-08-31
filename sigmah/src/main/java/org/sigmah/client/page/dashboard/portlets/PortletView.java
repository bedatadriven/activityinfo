package org.sigmah.client.page.dashboard.portlets;

import org.sigmah.client.dispatch.AsyncMonitor;
import org.sigmah.client.mvp.CanRefresh;
import org.sigmah.shared.dto.portlets.PortletDTO;

import com.extjs.gxt.ui.client.widget.custom.Portlet;
import com.google.gwt.user.client.ui.HasValue;

public interface PortletView<P extends PortletDTO> 
	extends 
		HasValue<P>,
		CanRefresh<P> {
	
	public Portlet asPortlet();
	public void initialize();
	public AsyncMonitor loadingMonitor();
}

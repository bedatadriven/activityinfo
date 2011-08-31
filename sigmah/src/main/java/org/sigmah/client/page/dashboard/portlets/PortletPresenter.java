package org.sigmah.client.page.dashboard.portlets;

import org.sigmah.shared.dto.portlets.PortletDTO;

public interface PortletPresenter {
	public PortletView<PortletDTO> getView();
}
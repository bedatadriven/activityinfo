package org.sigmah.shared.dto;

import java.util.ArrayList;
import java.util.List;

import org.sigmah.shared.domain.User;
import org.sigmah.shared.dto.portlets.PortletDTO;

import com.extjs.gxt.ui.client.data.BaseModelData;


public class DashboardDTO extends BaseModelData {
	private List<PortletDTO> portlets = new ArrayList<PortletDTO>();
	private User user;
	private DashboardSettingsDTO settings;
	
	public List<PortletDTO> getPortlets() {
		return portlets;
	}
	
	
}

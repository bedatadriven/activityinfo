package org.sigmah.shared.dto;

import java.util.ArrayList;
import java.util.List;

import org.sigmah.shared.domain.User;
import org.sigmah.shared.dto.portlets.FavoritesDTO;
import org.sigmah.shared.dto.portlets.NoGpsLocationsDTO;
import org.sigmah.shared.dto.portlets.PortletDTO;

import com.extjs.gxt.ui.client.data.BaseModelData;

/*
 * Represents a user configured dashboard
 */
public class DashboardSettingsDTO extends BaseModelData implements DTO {
	
	public DashboardSettingsDTO() {
		super();
		
		setPortlets(new ArrayList<PortletDTO>());
		getPortlets().add(new FavoritesDTO());
		getPortlets().add(new NoGpsLocationsDTO());
	}
	
	public void setAmountColumns(int amountColumns) {
		set("amountColumns", amountColumns);
	}

	public int getAmountColumns() {
		return (Integer)get("amountColumns");
	}

	public void setPortlets(List<PortletDTO> portlets) {
		set("portlets", portlets);
	}

	@SuppressWarnings("unchecked")
	public List<PortletDTO> getPortlets() {
		return (List<PortletDTO>)get("portlets");
	}

	public void setUser(User user) {
		set("user", user);
	}

	public User getUser() {
		return (User)get("user");
	}
	
	/*
	 * Returns a new DashboardSettingsDTO instance with default settings
	 */
	public static DashboardSettingsDTO createDefault() {
		DashboardSettingsDTO defaultDashBoard = new DashboardSettingsDTO();
		
		defaultDashBoard.getPortlets().add(new NoGpsLocationsDTO());
		defaultDashBoard.setAmountColumns(3);
		
		return defaultDashBoard;
	}
}
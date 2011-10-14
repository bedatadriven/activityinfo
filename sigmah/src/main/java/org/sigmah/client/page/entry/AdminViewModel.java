package org.sigmah.client.page.entry;

import org.sigmah.shared.dto.AdminEntityDTO;

import com.extjs.gxt.ui.client.data.BaseModelData;

public class AdminViewModel extends BaseModelData {
	public AdminViewModel(AdminEntityDTO adminEntity, int amountSites) {
		setAmountSites(amountSites);
		setAdminEntity(adminEntity);
		setName(adminEntity.getName() + " (" + Integer.toString(amountSites) + ")");
	}
	public AdminViewModel() {
	}
	public String getName() {
		return (String)get("name");
	}
	public void setName(String name) {
		set("name", name);
	}
	public AdminEntityDTO getAdminEntity() {
		return (AdminEntityDTO)get("adminEntity");
	}
	public void setAdminEntity(AdminEntityDTO adminEntity) {
		set("adminEntity", adminEntity);
	}
	public int getAmountSites() {
		return (Integer)get("amountSites");
	}
	public void setAmountSites(int amountSites) {
		set("amountSites", amountSites);
	}
}

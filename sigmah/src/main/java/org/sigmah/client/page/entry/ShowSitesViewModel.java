package org.sigmah.client.page.entry;

import org.sigmah.client.i18n.I18N;
import org.sigmah.shared.dto.AdminEntityDTO;

import com.extjs.gxt.ui.client.data.BaseModelData;

public class ShowSitesViewModel extends BaseModelData {
	public ShowSitesViewModel(int amount, AdminEntityDTO adminEntity) {
		set("name", I18N.MESSAGES.clickToShowAllSitesOfAdminEntity(Integer.toString(amount), adminEntity.getName())); 
		set("adminEntityId", adminEntity.getId());
	}
	public int getAdminEntityId() {
		return (Integer)get("adminEntityId");
	}
}

package org.sigmah.client.page.entry;

import com.extjs.gxt.ui.client.data.BaseModelData;

public class ShowSitesViewModel extends BaseModelData {
	public ShowSitesViewModel(int amount, int adminEntityId) {
		set("name", "[click to expand all " + amount + " sites]");
		set("adminEntityId", adminEntityId);
	}
	public int getAdminEntityId() {
		return (Integer)get("adminEntityId");
	}
}

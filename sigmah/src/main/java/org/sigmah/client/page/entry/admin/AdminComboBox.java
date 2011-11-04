package org.sigmah.client.page.entry.admin;

import org.sigmah.shared.dto.AdminEntityDTO;
import org.sigmah.shared.dto.AdminLevelDTO;

import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.form.ComboBox;

public class AdminComboBox extends ComboBox<AdminEntityDTO> {

	public AdminComboBox(AdminLevelDTO level, ListStore<AdminEntityDTO> store) {
		setFieldLabel(level.getName());
		setStore(store);
		setTypeAhead(false);
		setForceSelection(true);
		setEditable(false);
		setValueField("id");
		setUseQueryCache(false);
		setDisplayField("name");
		setTriggerAction(TriggerAction.ALL);
	}
	
}

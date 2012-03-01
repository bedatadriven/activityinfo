package org.sigmah.client.page.entry.form;

import org.sigmah.client.page.entry.admin.AdminComboBox;
import org.sigmah.client.page.entry.admin.AdminComboBoxSet.ComboBoxFactory;
import org.sigmah.shared.dto.AdminEntityDTO;
import org.sigmah.shared.dto.AdminLevelDTO;

import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SelectionChangedEvent;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.form.ComboBox;

public class BoundAdminComboBox extends ComboBox<AdminEntityDTO> implements AdminComboBox {

	public BoundAdminComboBox(AdminLevelDTO level, ListStore<AdminEntityDTO> store) {
		setFieldLabel(level.getName());
		setStore(store);
		setTypeAhead(false);
		setForceSelection(true);
		setEditable(false);
		setValueField("id");
		setUseQueryCache(false);
		setDisplayField("name");
		setAllowBlank(false);
		setTriggerAction(TriggerAction.ALL);
	}
	
	@Override
	public void addSelectionChangeListener(
			Listener<SelectionChangedEvent> listener) {
		addListener(Events.SelectionChange, listener);
	}
	
	public static class Factory implements ComboBoxFactory {

		@Override
		public AdminComboBox create(AdminLevelDTO level,
				ListStore<AdminEntityDTO> store) {
			return new BoundAdminComboBox(level, store);
		}
	}

}

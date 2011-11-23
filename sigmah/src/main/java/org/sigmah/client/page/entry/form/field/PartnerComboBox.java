package org.sigmah.client.page.entry.form.field;

import java.util.List;

import org.sigmah.client.i18n.I18N;
import org.sigmah.shared.dto.ActivityDTO;
import org.sigmah.shared.dto.PartnerDTO;

import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.form.ComboBox;

public class PartnerComboBox extends ComboBox<PartnerDTO> {

	public PartnerComboBox(ActivityDTO activity) {
		this(activity.getDatabase().getPartners());
	}
	
	public PartnerComboBox(List<PartnerDTO> partners) {
		
		ListStore<PartnerDTO> store = new ListStore<PartnerDTO>();
		store.add(partners);
		
		setName("partner");
		setDisplayField("name");
		setEditable(false);
		setTriggerAction(ComboBox.TriggerAction.ALL);
		setStore(store);
		setFieldLabel(I18N.CONSTANTS.partner());
		setForceSelection(true);
		setAllowBlank(false);
		
		if(store.getCount() == 1) {
			setValue(store.getAt(0));
		}
	}
	
}

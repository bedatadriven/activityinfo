package org.sigmah.client.page.entry.form.field;

import java.util.Collections;
import java.util.List;

import org.sigmah.client.i18n.I18N;
import org.sigmah.shared.dto.ActivityDTO;
import org.sigmah.shared.dto.PartnerDTO;

import com.extjs.gxt.ui.client.Style.SortDir;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.form.ComboBox;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;

public class PartnerComboBox extends ComboBox<PartnerDTO> {

	public PartnerComboBox(ActivityDTO activity) {
		this(allowablePartners(activity));
	}
	
	private static List<PartnerDTO> allowablePartners(ActivityDTO activity) {
		if(activity.getDatabase().isEditAllAllowed()) {
			return activity.getDatabase().getPartners();
		} else {
			return Collections.singletonList(activity.getDatabase().getMyPartner());
		}
	}
	
	public PartnerComboBox(List<PartnerDTO> partners) {
		
		final ListStore<PartnerDTO> store = new ListStore<PartnerDTO>();	
		store.add(partners);
		store.sort("name", SortDir.ASC);
		
		setName("partner");
		setDisplayField("name");
		setEditable(false);
		setTriggerAction(ComboBox.TriggerAction.ALL);
		setStore(store);
		setFieldLabel(I18N.CONSTANTS.partner());
		setForceSelection(true);
		setAllowBlank(false);
		
		if(store.getCount() == 1) {
			Scheduler.get().scheduleDeferred(new ScheduledCommand() {
				
				@Override
				public void execute() {
					setValue(store.getAt(0));
				}
			});
		}
	}
	
}

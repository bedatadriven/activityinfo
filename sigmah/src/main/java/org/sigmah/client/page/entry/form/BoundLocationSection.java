package org.sigmah.client.page.entry.form;

import org.sigmah.client.dispatch.Dispatcher;
import org.sigmah.client.page.entry.admin.AdminComboBox;
import org.sigmah.client.page.entry.admin.AdminComboBoxSet;
import org.sigmah.client.page.entry.admin.AdminFieldSetPresenter;
import org.sigmah.shared.dto.ActivityDTO;

/**
 * Presents a form dialog for a Site for an Activity that 
 * has a LocationType that is bound to an AdminLevel
 */
public class BoundLocationSection extends FormSection {

	private AdminFieldSetPresenter adminFieldSet;

	public BoundLocationSection(Dispatcher dispatcher, ActivityDTO activity) {
	

		adminFieldSet = new AdminFieldSetPresenter(dispatcher, activity
				.getDatabase().getCountry(), activity.getAdminLevels());
			
		AdminComboBoxSet comboBoxes = new AdminComboBoxSet(adminFieldSet);

		for (AdminComboBox comboBox : comboBoxes) {
			add(comboBox);
		}
	}

}

package org.sigmah.client.page.entry.form;

import org.sigmah.client.dispatch.Dispatcher;
import org.sigmah.client.page.entry.admin.AdminComboBox;
import org.sigmah.client.page.entry.admin.AdminComboBoxSet;
import org.sigmah.client.page.entry.admin.AdminFieldSetPresenter;
import org.sigmah.shared.dto.ActivityDTO;
import org.sigmah.shared.dto.AdminLevelDTO;
import org.sigmah.shared.dto.LocationDTO;
import org.sigmah.shared.dto.SiteDTO;

/**
 * Presents a form dialog for a Site for an Activity that 
 * has a LocationType that is bound to an AdminLevel
 */
public class BoundLocationSection extends FormSectionWithFormLayout<SiteDTO> implements LocationFormSection {

	private AdminFieldSetPresenter adminFieldSet;
	private AdminComboBoxSet comboBoxes;
	
	private boolean isNew;
	
	public BoundLocationSection(Dispatcher dispatcher, ActivityDTO activity) {
	

		adminFieldSet = new AdminFieldSetPresenter(dispatcher, activity
				.getDatabase().getCountry(), activity.getAdminLevels());
			
		comboBoxes = new AdminComboBoxSet(adminFieldSet);

		for (AdminComboBox comboBox : comboBoxes) {
			add(comboBox);
		}
	}

	@Override
	public boolean validate() {
		return comboBoxes.validate();
	}

	@Override
	public void updateModel(SiteDTO m) {
		for(AdminLevelDTO level : adminFieldSet.getAdminLevels()) {
			m.setAdminEntity(level.getId(), adminFieldSet.getAdminEntity(level));
		}
	}

	@Override
	public boolean isNew() {
		return isNew;
	}

	@Override
	public LocationDTO getLocation() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void updateForm(SiteDTO m) {
		// TODO Auto-generated method stub
		
	}

}

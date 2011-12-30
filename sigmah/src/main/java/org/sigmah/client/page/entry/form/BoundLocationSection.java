package org.sigmah.client.page.entry.form;

import org.sigmah.client.dispatch.Dispatcher;
import org.sigmah.client.offline.command.handler.KeyGenerator;
import org.sigmah.client.page.entry.admin.AdminComboBox;
import org.sigmah.client.page.entry.admin.AdminComboBoxSet;
import org.sigmah.client.page.entry.admin.AdminFieldSetPresenter;
import org.sigmah.shared.command.CreateLocation;
import org.sigmah.shared.command.result.VoidResult;
import org.sigmah.shared.dto.ActivityDTO;
import org.sigmah.shared.dto.AdminEntityDTO;
import org.sigmah.shared.dto.AdminLevelDTO;
import org.sigmah.shared.dto.LocationDTO;
import org.sigmah.shared.dto.SiteDTO;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * Presents a form dialog for a Site for an Activity that 
 * has a LocationType that is bound to an AdminLevel
 */
public class BoundLocationSection extends FormSectionWithFormLayout<SiteDTO> implements LocationFormSection {

	private final Dispatcher dispatcher;
	
	private AdminFieldSetPresenter adminFieldSet;
	private AdminComboBoxSet comboBoxes;
	private BoundAdminComboBox leafComboBox;
	
	private LocationDTO location;
	
	public BoundLocationSection(Dispatcher dispatcher, ActivityDTO activity) {
	
		this.dispatcher = dispatcher; 
		
		adminFieldSet = new AdminFieldSetPresenter(dispatcher, activity
				.getDatabase().getCountry(), activity.getAdminLevels());
			
		comboBoxes = new AdminComboBoxSet(adminFieldSet, new BoundAdminComboBox.Factory());
		
		for (AdminComboBox comboBox : comboBoxes) {
			add(comboBox.asWidget());
			leafComboBox = (BoundAdminComboBox)comboBox;
		}
		
	}
	
	@Override
	public void updateForm(LocationDTO location, boolean isNew) {
		this.location = location;
		adminFieldSet.setSelection(location);
	}
	

	@Override
	public void save(final AsyncCallback<Void> callback) {
		if(isDirty()) {
			newLocation();
			dispatcher.execute(new CreateLocation(location), null, new AsyncCallback<VoidResult>() {

				@Override
				public void onFailure(Throwable caught) {
					callback.onFailure(caught);
				}

				@Override
				public void onSuccess(VoidResult result) {
					callback.onSuccess(null);
				}
			});
		} else {
			callback.onSuccess(null);
		}
	}
	
	private void newLocation() {
		location = new LocationDTO(location);
		location.setId(new KeyGenerator().generateInt());
		location.setName(leafComboBox.getValue().getName());
		for(AdminLevelDTO level : adminFieldSet.getAdminLevels()) {
			location.setAdminEntity(level.getId(), adminFieldSet.getAdminEntity(level));
		}
	}
	
	private boolean isDirty() {
		for(AdminLevelDTO level : adminFieldSet.getAdminLevels()) {
			AdminEntityDTO original = location.getAdminEntity(level.getId());
			AdminEntityDTO current = adminFieldSet.getAdminEntity(level);
			
			if(current == null && original != null) {
				return true;
			}
			if(current != null && original == null) {
				return true;
			}
			if(current.getId() != original.getId()) {
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean validate() {
		return comboBoxes.validate();
	}

	@Override
	public void updateModel(SiteDTO m) {
		m.setLocation(location);
	}

	@Override
	public LocationDTO getLocation() {
		return location;
	}

	@Override
	public void updateForm(SiteDTO m) {
		
	}
}

/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.client.page.config.form;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.allen_sauer.gwt.log.client.Log;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.CheckBox;
import com.extjs.gxt.ui.client.widget.form.ComboBox;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.LabelField;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.form.ComboBox.TriggerAction;
import com.extjs.gxt.ui.client.widget.layout.FormLayout;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Grid;

import org.sigmah.client.UserInfo;
import org.sigmah.client.i18n.I18N;
import org.sigmah.client.i18n.UIConstants;
import org.sigmah.client.dispatch.Dispatcher;
import org.sigmah.shared.command.CreateEntity;
import org.sigmah.shared.command.GetProfiles;
import org.sigmah.shared.command.result.CreateResult;
import org.sigmah.shared.command.result.ProfileListResult;
import org.sigmah.shared.dto.OrgUnitDTOLight;
import org.sigmah.shared.dto.UserDTO;
import org.sigmah.shared.dto.profile.ProfileDTO;
import org.sigmah.shared.dto.profile.ProfileDTOLight;

/**
 * Create user form.
 * 
 * @author nrebiai
 * 
 */
public class UserSigmahForm extends FormPanel {

	private final TextField<String> nameField;
	private final TextField<String> firstNameField;
	private final TextField<String> emailField;
	private final TextField<String> localeField;
	private final ComboBox<OrgUnitDTOLight> orgUnitsList;
	private final ListStore<OrgUnitDTOLight> orgUnitsStore;
	private final ComboBox<ProfileDTOLight> profilesList;
	private final LabelField selectedProfiles;
	private final List<Integer> selectedProfilesIds;
	
	private final Dispatcher dispatcher;
	private HashMap<String, Object> newUserProperties;
	
	private final static int LABEL_WIDTH = 90;
	
	public UserSigmahForm(Dispatcher dispatcher, UserInfo info, 
			final AsyncCallback<CreateResult> callback, UserDTO userToUpdate) {
		
		this.dispatcher = dispatcher;
		UIConstants constants = GWT.create(UIConstants.class);
		
		FormLayout layout = new FormLayout();
		layout.setLabelWidth(LABEL_WIDTH);
		setLayout(layout);
		
		nameField = new TextField<String>();
		nameField.setFieldLabel(constants.adminUsersName());
		nameField.setAllowBlank(false);
		if(userToUpdate != null && !userToUpdate.getName().isEmpty())
			nameField.setValue(userToUpdate.getName());
		add(nameField);
		
		firstNameField = new TextField<String>();
		firstNameField.setFieldLabel(constants.adminUsersFirstName());
		firstNameField.setAllowBlank(false);
		if(userToUpdate != null && !userToUpdate.getFirstName().isEmpty())
			firstNameField.setValue(userToUpdate.getFirstName());
		add(firstNameField);
		
		emailField = new TextField<String>();
		emailField.setFieldLabel(constants.adminUsersEmail());
		emailField.setAllowBlank(false);
		if(userToUpdate != null && !userToUpdate.getEmail().isEmpty())
			emailField.setValue(userToUpdate.getEmail());
		add(emailField);
		
		localeField = new TextField<String>();
		localeField.setFieldLabel(constants.adminUsersLocale());
		localeField.setAllowBlank(false);
		if(userToUpdate != null && !userToUpdate.getLocale().isEmpty())
			localeField.setValue(userToUpdate.getLocale());
		add(localeField);
		
		orgUnitsList = new ComboBox<OrgUnitDTOLight>();
		orgUnitsList.setFieldLabel(I18N.CONSTANTS.adminUsersOrgUnit());
		orgUnitsList.setDisplayField("fullName");
		orgUnitsList.setValueField("id");
		orgUnitsList.setEditable(true);
		orgUnitsList.setTriggerAction(TriggerAction.ALL);		
		if(userToUpdate != null && userToUpdate.getOrgUnitWithProfiles() != null
				&& !userToUpdate.getOrgUnitWithProfiles().getFullName().isEmpty())
			orgUnitsList.setEmptyText(userToUpdate.getOrgUnitWithProfiles().getFullName());
		else
			orgUnitsList.setEmptyText(I18N.CONSTANTS.adminUserCreationOrgUnitChoice());
		orgUnitsStore = new ListStore<OrgUnitDTOLight>();        
        orgUnitsList.setStore(orgUnitsStore);
		info.getOrgUnit(new AsyncCallback<OrgUnitDTOLight>() {
			@Override
            public void onFailure(Throwable e) {
				orgUnitsList.setEmptyText(I18N.CONSTANTS.adminUserCreationChoiceProblem());
            }

            @Override
            public void onSuccess(OrgUnitDTOLight result) {
            	orgUnitsStore.removeAll();
                if (result != null) {
                	fillOrgUnitsList(result);
                	orgUnitsStore.commitChanges();	
                }
            }
		});			
		add(orgUnitsList);
		
		profilesList = new ComboBox<ProfileDTOLight>();
		profilesList.setDisplayField("name");
		profilesList.setValueField("id");
		profilesList.setEditable(true);		
		profilesList.setTriggerAction(TriggerAction.ALL);
		final ListStore<ProfileDTOLight> profilesStore = new ListStore<ProfileDTOLight>();
		dispatcher.execute(new GetProfiles(), 
        		null,
        		new AsyncCallback<ProfileListResult>() {

					@Override
					public void onFailure(Throwable arg0) {
						profilesList.setEmptyText(I18N.CONSTANTS.adminUserCreationChoiceProblem());
					}

					@Override
					public void onSuccess(ProfileListResult result) {
						profilesList.setEmptyText(I18N.CONSTANTS.adminUserCreationProfileChoice());
						profilesStore.removeAll();
		                if (result != null) {
		                    profilesStore.add(result.getList());
		                    profilesStore.commitChanges();
		                }						
					}			
		});
		
		
		profilesList.setStore(profilesStore);
		
		final Grid profilesAddSelectionGrid = new Grid(1, 3);
		
		profilesAddSelectionGrid.getCellFormatter().setWidth(0, 0, (LABEL_WIDTH + 5)+"px");
		profilesAddSelectionGrid.setCellPadding(0);
		profilesAddSelectionGrid.setCellSpacing(0);
		profilesAddSelectionGrid.setText(0, 0, I18N.CONSTANTS.adminUsersProfiles()+ ": ");
		profilesAddSelectionGrid.setWidget(0, 0, new LabelField(I18N.CONSTANTS.adminUsersProfiles()+":"));
		profilesAddSelectionGrid.setWidget(0, 1, profilesList);
		profilesList.setHideLabel(false);
		
		selectedProfiles = new LabelField();
		selectedProfilesIds = new ArrayList<Integer>();
		selectedProfiles.setFieldLabel(I18N.CONSTANTS.adminUserCreationSelectedProfiles()+":");
		if(userToUpdate != null && userToUpdate.getOrgUnitWithProfiles() != null
        		&& userToUpdate.getProfilesDTO() != null){
        	List<ProfileDTO> usedProfiles = userToUpdate.getProfilesDTO();
        	for(ProfileDTO usedProfile : usedProfiles){
        		if(selectedProfiles.getText() != null)
            		selectedProfiles.setText(selectedProfiles.getText() + ", " + usedProfile.getName());
            	else
            		selectedProfiles.setText(usedProfile.getName());
        	}
        }
		final Button addButton = new Button(I18N.CONSTANTS.addItem());
        addButton.addListener(Events.OnClick, new Listener<ButtonEvent>() {
            @Override
            public void handleEvent(ButtonEvent be) {
                if(profilesList.getValue() != null){            	
                	if(selectedProfiles.getText() != null)
                		selectedProfiles.setText(selectedProfiles.getText() + ", " + profilesList.getValue().getName());
                	else
                		selectedProfiles.setText(profilesList.getValue().getName());
                	selectedProfilesIds.add(profilesList.getValue().getId());
                }                
            }
        });
        //add(addButton);
        
        profilesAddSelectionGrid.setWidget(0, 2, addButton);
        add(profilesAddSelectionGrid);
        
        add(selectedProfiles);
		
		// Create button.
        final Button createButton = new Button(I18N.CONSTANTS.save());
        createButton.addListener(Events.OnClick, new Listener<ButtonEvent>() {
            @Override
            public void handleEvent(ButtonEvent be) {
                createUser(callback);
            }
        });
        add(createButton);
  	}
	
	protected CheckBox createCheckBox(String property, String label) {
		CheckBox box = new CheckBox();
		box.setName(property);
		box.setBoxLabel(label);
		return box;
	}

	private void createUser(final AsyncCallback<CreateResult> callback) {
		 if (!this.isValid()) {
	            MessageBox.alert(I18N.CONSTANTS.createProjectFormIncomplete(),
	                    I18N.CONSTANTS.createProjectFormIncompleteDetails(), null);
	            return;
		 }
		 final String name = nameField.getValue();
		 final String firstName = firstNameField.getValue();
		 final String email = emailField.getValue();
		 final String locale = localeField.getValue();
		 final int orgUnit = orgUnitsList.getValue().getId();
		 final List<Integer> profiles = selectedProfilesIds;
		 
		 newUserProperties = new HashMap<String, Object>();
		 newUserProperties.put("name", name);
		 newUserProperties.put("firstName", firstName);   
		 newUserProperties.put("email", email);
		 newUserProperties.put("locale", locale);
		 newUserProperties.put("orgUnit", orgUnit);
		 newUserProperties.put("profiles", profiles);
		 
         dispatcher.execute(new CreateEntity("User", newUserProperties), null, new AsyncCallback<CreateResult>(){

             public void onFailure(Throwable caught) {
             	MessageBox.alert(I18N.CONSTANTS.adminUserCreationBox(), I18N.MESSAGES.adminUserCreationFailure(name), null);
             	callback.onFailure(caught);
             }

				@Override
				public void onSuccess(CreateResult result) {
					if(result != null){						
						callback.onSuccess(result);						
						MessageBox.alert(I18N.CONSTANTS.adminUserCreationBox(), I18N.MESSAGES.adminUserCreationSuccess(new Integer(result.getNewId()).toString()), null);
					}					
					else{
						Throwable t = new Throwable("AdminUsersPresenter : creation result is null");
						callback.onFailure(t);
						MessageBox.alert(I18N.CONSTANTS.adminUserCreationBox(), I18N.MESSAGES.adminUserCreationNull(name), null);
					}		
				}
         });
		 
	}
	
	public HashMap<String, Object> getUserProperties(){
		return newUserProperties;
	}
	
	/**
     * Fills combobox with the children of the given root org units.
     * 
     * @param root
     *            The root org unit.
     */
    private void fillOrgUnitsList(OrgUnitDTOLight root) {

        for (final OrgUnitDTOLight child : root.getChildrenDTO()) {
            recursiveFillOrgUnitsList(child);
        }
    }

    /**
     * Fills recursively the combobox from the given root org unit.
     * 
     * @param root
     *            The root org unit.
     */
    private void recursiveFillOrgUnitsList(OrgUnitDTOLight root) {

        if (root.getCanContainProjects()) {
            orgUnitsStore.add(root);
        }

        for (final OrgUnitDTOLight child : root.getChildrenDTO()) {
            recursiveFillOrgUnitsList(child);
        }
    }
}

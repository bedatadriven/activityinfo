package org.sigmah.client.page.entry.form.field;

import org.sigmah.client.i18n.I18N;
import org.sigmah.shared.dto.ActivityDTO;
import org.sigmah.shared.dto.ProjectDTO;

import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.form.ComboBox;

public class ProjectComboBox extends ComboBox<ProjectDTO> {

	public ProjectComboBox(ActivityDTO activity) {
		
		ListStore<ProjectDTO> store = new ListStore<ProjectDTO>();
		store.add(activity.getDatabase().getProjects());
		
		setName("project");
		setDisplayField("name");
		setEditable(false);
		setStore(store);
		setTriggerAction(ComboBox.TriggerAction.ALL);
		setFieldLabel(I18N.CONSTANTS.project());
		setForceSelection(true);
		setAllowBlank(true);
	}
}

package org.sigmah.client.page.entry.editor;

import java.util.List;

import org.sigmah.client.dispatch.AsyncMonitor;
import org.sigmah.client.i18n.I18N;
import org.sigmah.client.page.entry.editor.ProjectPresenter.ProjectPickerView;
import org.sigmah.shared.dto.ProjectDTO;
import org.sigmah.shared.dto.SiteDTO;

import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.form.ComboBox;
import com.extjs.gxt.ui.client.widget.form.FieldSet;

public class ProjectFieldSet extends FieldSet implements ProjectPickerView {
	private ComboBox<ProjectDTO> comboboxProjects;
	
	public ProjectFieldSet(SiteDTO site) {
		super();
		
		initializeComponent();
		
		createComboboxProjects();
	}

	private void createComboboxProjects() {
		comboboxProjects = new ComboBox<ProjectDTO>();
		
		comboboxProjects.setName("project");
		comboboxProjects.setDisplayField("name");
		comboboxProjects.setEditable(false);
		comboboxProjects.setTriggerAction(ComboBox.TriggerAction.ALL);
		comboboxProjects.setFieldLabel(I18N.CONSTANTS.project());
		comboboxProjects.setForceSelection(true);
		comboboxProjects.setAllowBlank(false);
		
		add(comboboxProjects);
	}

	private void initializeComponent() {
		setHeading(I18N.CONSTANTS.project());
		setCollapsible(false);
	}

	@Override
	public void setProjects(List<ProjectDTO> projects) {
		ListStore<ProjectDTO> projectStore = new ListStore<ProjectDTO>();
		projectStore.add(projects);
		comboboxProjects.setStore(projectStore);
	}

	@Override
	public void initialize() {
	}

	@Override
	public AsyncMonitor getAsyncMonitor() {
		return null;
	}

	@Override
	public void setValue(ProjectDTO value) {
		comboboxProjects.setValue(value);
	}

	@Override
	public ProjectDTO getValue() {
		return comboboxProjects.getValue();
	}
}
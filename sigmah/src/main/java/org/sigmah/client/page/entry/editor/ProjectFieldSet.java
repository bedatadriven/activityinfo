package org.sigmah.client.page.entry.editor;

import org.sigmah.client.i18n.I18N;
import org.sigmah.shared.dto.ProjectDTO;
import org.sigmah.shared.dto.SiteDTO;

import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.form.ComboBox;
import com.extjs.gxt.ui.client.widget.form.FieldSet;

public class ProjectFieldSet extends FieldSet implements ProjectPresenter.View {
	private SiteDTO site;
	private ComboBox<ProjectDTO> comboboxProjects;
	
	public ProjectFieldSet(SiteDTO site) {
		super();
		
		this.site = site;
		
		initializeComponent();
		
		createComboboxProjects();
	}

	private void createComboboxProjects() {
		comboboxProjects = new ComboBox<ProjectDTO>();
		
		comboboxProjects.setName("project");
		comboboxProjects.setDisplayField("name");
		comboboxProjects.setEditable(false);
		comboboxProjects.setTriggerAction(ComboBox.TriggerAction.ALL);
		comboboxProjects.setFieldLabel(I18N.CONSTANTS.projectName());
		comboboxProjects.setForceSelection(true);
		comboboxProjects.setAllowBlank(false);
		
		add(comboboxProjects);
	}

	private void initializeComponent() {
		setHeading(I18N.CONSTANTS.projectName());
		setCollapsible(false);
	}

	@Override
	public void init(ProjectPresenter presenter) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public ProjectDTO getProject() {
		return comboboxProjects.getValue();
	}

	@Override
	public void setProject(ProjectDTO project) {
		comboboxProjects.setValue(project);
	}

	@Override
	public void setStore(ListStore<ProjectDTO> projectStore) {
		comboboxProjects.setStore(projectStore);
	}
}
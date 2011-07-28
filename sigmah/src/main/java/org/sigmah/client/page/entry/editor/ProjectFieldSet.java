package org.sigmah.client.page.entry.editor;

import java.util.List;

import org.sigmah.client.i18n.I18N;
import org.sigmah.shared.dto.ProjectDTO;

import com.extjs.gxt.ui.client.widget.form.ComboBox;
import com.extjs.gxt.ui.client.widget.form.FieldSet;

public class ProjectFieldSet extends FieldSet {
	private ProjectDTO project;
	private ComboBox<ProjectDTO> comboboxProjects = new ComboBox<ProjectDTO>();
	private List<ProjectDTO> projects;
	
	public ProjectFieldSet(ProjectDTO project) {
		super();
		
		this.project = project;
		
		initializeComponent();
		
		createComboboxProjects();
	}

	private void createComboboxProjects() {
	}

	private void initializeComponent() {
		setHeading(I18N.CONSTANTS.projectName());
		setCollapsible(false);
	}
	
	
}

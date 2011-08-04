package org.sigmah.client.page.entry.editor;

import java.util.List;

import org.sigmah.client.mvp.View;
import org.sigmah.shared.dto.ProjectDTO;
import org.sigmah.shared.dto.SiteDTO;
import org.sigmah.shared.dto.UserDatabaseDTO;

public class ProjectPresenter {
	public interface ProjectPickerView extends View<ProjectDTO>{
        public void setProjects(List<ProjectDTO> projects);
	}
	
	private ProjectPickerView view;

	public ProjectPresenter(ProjectPickerView view) { 
		this.view = view;
	}

	public void setSite(SiteDTO site, UserDatabaseDTO database) {
		this.view.setValue(site.getProject());
		this.view.setProjects(database.getProjects());
	}

	public ProjectPickerView getView() {
		return view;
	}
}
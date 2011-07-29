package org.sigmah.client.page.entry.editor;

import org.sigmah.shared.dto.ProjectDTO;
import org.sigmah.shared.dto.SiteDTO;
import org.sigmah.shared.dto.UserDatabaseDTO;

import com.extjs.gxt.ui.client.store.ListStore;

public class ProjectPresenter {
	
	public interface View {
        public void init(ProjectPresenter presenter);
        
        public ProjectDTO getProject();

        public void setProject(ProjectDTO project);

        public void setStore(ListStore<ProjectDTO> projectStore);
	}
	
	private ProjectPresenter.View view;
	private SiteDTO site;
	private UserDatabaseDTO database;

	public ProjectPresenter(ProjectPresenter.View view) { 
		this.view = view;
		//this.view.init(this);
	}

	public void setSite(SiteDTO site, UserDatabaseDTO database) {
		this.site = site;
		this.database=database;
		
		this.view.setProject(site.getProject());
		ListStore<ProjectDTO> store = new ListStore<ProjectDTO>();
		store.add(database.getProjects());
		this.view.setStore(store);
	}

	public ProjectPresenter.View getView() {
		return view;
	}
}
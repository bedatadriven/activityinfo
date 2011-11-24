package org.sigmah.client.page.config;

import java.util.ArrayList;

import org.sigmah.client.AppEvents;
import org.sigmah.client.EventBus;
import org.sigmah.client.dispatch.Dispatcher;
import org.sigmah.client.i18n.I18N;
import org.sigmah.client.page.PageId;
import org.sigmah.client.page.PageState;
import org.sigmah.client.page.common.dialog.FormDialogCallback;
import org.sigmah.client.page.common.dialog.FormDialogTether;
import org.sigmah.client.page.common.grid.AbstractGridPresenter;
import org.sigmah.client.page.common.grid.GridView;
import org.sigmah.client.page.common.toolbar.UIActions;
import org.sigmah.client.util.state.StateProvider;
import org.sigmah.shared.command.AddProject;
import org.sigmah.shared.command.RemoveProject;
import org.sigmah.shared.command.result.CreateResult;
import org.sigmah.shared.command.result.VoidResult;
import org.sigmah.shared.dto.ProjectDTO;
import org.sigmah.shared.dto.UserDatabaseDTO;
import org.sigmah.shared.exception.DuplicatePartnerException;
import org.sigmah.shared.exception.ProjectHasSitesException;

import com.extjs.gxt.ui.client.Style;
import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.ImplementedBy;
import com.google.inject.Inject;

/*
 * Displays a grid where users can add, remove and change projects
 */
public class DbProjectEditor extends AbstractGridPresenter<ProjectDTO> {
	public static final PageId DatabaseProjects = new PageId("projects");
	
    @ImplementedBy(DbProjectGrid.class)
    public interface View extends GridView<DbProjectEditor, ProjectDTO> {
        public void init(DbProjectEditor editor, UserDatabaseDTO db, ListStore<ProjectDTO> store);
        public FormDialogTether showAddDialog(ProjectDTO partner, FormDialogCallback callback);
    }
	
    private final Dispatcher service;
    private final EventBus eventBus;
    private final View view;
    
    private UserDatabaseDTO db;
    private ListStore<ProjectDTO> store;
    
    @Inject
    public DbProjectEditor(EventBus eventBus, Dispatcher service, StateProvider stateMgr, View view) {
        super(eventBus, stateMgr, view);
        this.service = service;
        this.eventBus = eventBus;
        this.view = view;
    }
    
    public void go(UserDatabaseDTO db) {
        this.db = db;

        store = new ListStore<ProjectDTO>();
        store.setSortField("name");
        store.setSortDir(Style.SortDir.ASC);
        store.add(new ArrayList<ProjectDTO>(db.getProjects()));

        view.init(this, db, store);
        view.setActionEnabled(UIActions.delete, false);
    }

	@Override
	protected void onDeleteConfirmed(final ProjectDTO project) {
        service.execute(new RemoveProject(db.getId(), project.getId()), view.getDeletingMonitor(), new AsyncCallback<VoidResult>() {
            public void onFailure(Throwable caught) {
                if (caught instanceof ProjectHasSitesException) { 
                    MessageBox.alert(I18N.CONSTANTS.removeItem(), I18N.MESSAGES.projectHasDataWarning(project.getName()), null);
                } else {
                	MessageBox.alert(I18N.CONSTANTS.error(), I18N.CONSTANTS.errorOnServer(), null);
                }
            }

            public void onSuccess(VoidResult result) {
                store.remove(project);
            }
        });
	}

	@Override
	protected void onAdd() {
        final ProjectDTO newProject = new ProjectDTO();
        this.view.showAddDialog(newProject, new FormDialogCallback() {

            @Override
            public void onValidated(final FormDialogTether dlg) {

                service.execute(new AddProject(db.getId(), newProject), dlg, new AsyncCallback<CreateResult>() {
                    public void onFailure(Throwable caught) {
                        if (caught instanceof DuplicatePartnerException) {
                        	MessageBox.alert(I18N.CONSTANTS.error(), I18N.CONSTANTS.errorOnServer(), null);
                        } else {
                        	MessageBox.alert(I18N.CONSTANTS.error(), I18N.CONSTANTS.errorOnServer(), null);
                        }
                    }

                    public void onSuccess(CreateResult result) {
                    	newProject.setId(result.getNewId());
                        store.add(newProject);
                        db.getProjects().add(newProject);
                        eventBus.fireEvent(AppEvents.SchemaChanged);
                        dlg.hide();
                    }
                });
            }
        });
	}

//	@Override
//	public void onSelectionChanged(ProjectDTO selectedItem) {
//		view.setActionEnabled(UIActions.delete, true);
//	}

	@Override
	public PageId getPageId() {
		return DatabaseProjects;
	}

	@Override
	public Object getWidget() {
		return view;
	}

	@Override
	public boolean navigate(PageState place) {
		return false;
	}

	@Override
	public void shutdown() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected String getStateId() {
		return "ProjectsGrid";
	}

	@Override
	public void onSelectionChanged(ModelData selectedItem) {
		view.setActionEnabled(UIActions.delete, true);
	}

}

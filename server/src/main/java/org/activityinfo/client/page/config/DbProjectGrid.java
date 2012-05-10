package org.activityinfo.client.page.config;

import java.util.ArrayList;
import java.util.List;

import org.activityinfo.client.i18n.I18N;
import org.activityinfo.client.icon.IconImageBundle;
import org.activityinfo.client.page.common.dialog.FormDialogCallback;
import org.activityinfo.client.page.common.dialog.FormDialogImpl;
import org.activityinfo.client.page.common.dialog.FormDialogTether;
import org.activityinfo.client.page.common.grid.AbstractGridView;
import org.activityinfo.client.page.common.toolbar.UIActions;
import org.activityinfo.client.page.config.form.ProjectForm;
import org.activityinfo.shared.dto.ProjectDTO;
import org.activityinfo.shared.dto.UserDatabaseDTO;
import org.activityinfo.client.i18n.UIConstants;

import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.store.Store;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.google.inject.Inject;

public class DbProjectGrid extends
		AbstractGridView<ProjectDTO, DbProjectEditor> implements DbProjectEditor.View {
    private final UIConstants messages;
    private final IconImageBundle icons;
    
    private Grid<ProjectDTO> grid;
    
	@Inject
    public DbProjectGrid(UIConstants messages, IconImageBundle icons) {
        this.messages = messages;
        this.icons = icons;
    }
	
	@Override
	protected Grid<ProjectDTO> createGridAndAddToContainer(Store store) {
        grid = new Grid<ProjectDTO>((ListStore)store, createColumnModel());
        grid.setAutoExpandColumn("description");
        grid.setLoadMask(true);

        setLayout(new FitLayout());
        add(grid);

        return grid;
	}
	
    protected ColumnModel createColumnModel() {
        List<ColumnConfig> columns = new ArrayList<ColumnConfig>();

        columns.add(new ColumnConfig("name", messages.name(), 150));
        columns.add(new ColumnConfig("description", messages.description(), 300));

        return new ColumnModel(columns);
    }
    
	@Override
	protected void initToolBar() {
        toolBar.addButton(UIActions.ADD, I18N.CONSTANTS.addProject(), icons.add()); 
        toolBar.addButton(UIActions.DELETE, messages.delete(), icons.delete());
	}

	@Override
	public void init(DbProjectEditor editor, UserDatabaseDTO db,
			ListStore<ProjectDTO> store) {
        super.init(editor, store);
        this.setHeading(I18N.MESSAGES.projectsForDatabase(db.getName())); 
	}

	@Override
	public FormDialogTether showAddDialog(ProjectDTO project,
			FormDialogCallback callback) {
		
		ProjectForm form = new ProjectForm();
        form.getBinding().bind(project);

        FormDialogImpl<ProjectForm> dlg = new FormDialogImpl<ProjectForm>(form);
        dlg.setWidth(450);
        dlg.setHeight(300);
        dlg.setHeading(messages.createProject());

        dlg.show(callback);

        return dlg;
	}

}

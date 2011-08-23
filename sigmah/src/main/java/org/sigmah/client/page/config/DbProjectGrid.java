package org.sigmah.client.page.config;

import java.util.ArrayList;
import java.util.List;

import org.sigmah.client.i18n.I18N;
import org.sigmah.client.i18n.UIConstants;
import org.sigmah.client.icon.IconImageBundle;
import org.sigmah.client.page.common.dialog.FormDialogCallback;
import org.sigmah.client.page.common.dialog.FormDialogImpl;
import org.sigmah.client.page.common.dialog.FormDialogTether;
import org.sigmah.client.page.common.grid.AbstractGridView;
import org.sigmah.client.page.common.toolbar.UIActions;
import org.sigmah.client.page.config.form.ProjectForm;
import org.sigmah.shared.dto.ProjectDTO;
import org.sigmah.shared.dto.UserDatabaseDTO;

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
    private ListStore<ProjectDTO> store;
    
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
        toolBar.addButton(UIActions.add, I18N.CONSTANTS.addProject(), icons.add()); 
        toolBar.addButton(UIActions.delete, messages.delete(), icons.delete());
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

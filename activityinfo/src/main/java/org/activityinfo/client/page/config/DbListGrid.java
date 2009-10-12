package org.activityinfo.client.page.config;

import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.store.Store;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import org.activityinfo.client.Application;
import org.activityinfo.client.page.common.dialog.FormDialogCallback;
import org.activityinfo.client.page.common.dialog.FormDialogImpl;
import org.activityinfo.client.page.common.dialog.FormDialogTether;
import org.activityinfo.client.page.common.grid.AbstractGridView;
import org.activityinfo.client.page.common.toolbar.UIActions;
import org.activityinfo.client.page.config.form.DatabaseForm;
import org.activityinfo.shared.dto.UserDatabaseDTO;

import java.util.ArrayList;
import java.util.List;

public class DbListGrid extends AbstractGridView<UserDatabaseDTO, DbListPresenter> implements DbListPresenter.View {

    private Grid<UserDatabaseDTO> grid;
    private ListStore<UserDatabaseDTO> store;
    private DbListPresenter presenter;

    public DbListGrid() {

        setLayout(new FitLayout());
        setHeading(Application.CONSTANTS.databases());
        setIcon(Application.ICONS.database());
    }

	protected void initToolBar() {
        toolBar.addButton(UIActions.add, Application.CONSTANTS.newDatabase(), Application.ICONS.addDatabase());
        toolBar.addEditButton(Application.ICONS.editDatabase());
        toolBar.addDeleteButton();
	}

    @Override
	protected Grid createGridAndAddToContainer(Store store) {

		grid = new Grid<UserDatabaseDTO>((ListStore)store, createColumnModel());
		grid.setAutoExpandColumn("fullName");
        grid.setLoadMask(true);
	
		add(grid);

        return grid;
	}

	protected ColumnModel createColumnModel() {

		List<ColumnConfig> columns = new ArrayList<ColumnConfig>();
		columns.add(new ColumnConfig("name", Application.CONSTANTS.name(), 100));
		columns.add(new ColumnConfig("fullName", Application.CONSTANTS.fullName(), 150));
		columns.add(new ColumnConfig("ownerName", Application.CONSTANTS.ownerName(), 150));

		return new ColumnModel(columns);
	}


    public FormDialogTether showAddDialog(UserDatabaseDTO db, FormDialogCallback callback) {

        DatabaseForm form = new DatabaseForm();
        form.getBinding().bind(db);

        FormDialogImpl dlg = new FormDialogImpl(form);
        dlg.setWidth(400);
        dlg.setHeight(200);
        dlg.setHeading(Application.CONSTANTS.newDatabase());

        dlg.show(callback);

        return dlg;

    }
}

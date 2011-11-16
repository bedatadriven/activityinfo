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
import org.sigmah.client.page.config.form.TargetForm;
import org.sigmah.shared.dto.TargetDTO;
import org.sigmah.shared.dto.UserDatabaseDTO;

import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.store.Store;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.google.inject.Inject;

public class DbTargetGrid extends AbstractGridView<TargetDTO, DbTargetEditor>
		implements DbTargetEditor.View {

	private final UIConstants messages;
	private final IconImageBundle icons;

	private Grid<TargetDTO> grid;

	@Inject
	public DbTargetGrid(UIConstants messages, IconImageBundle icons) {
		this.messages = messages;
		this.icons = icons;
	}

	@Override
	protected Grid<TargetDTO> createGridAndAddToContainer(Store store) {
		grid = new Grid<TargetDTO>((ListStore) store, createColumnModel());
		grid.setAutoExpandColumn("name");
		grid.setLoadMask(true);

		setLayout(new FitLayout());
		add(grid);

		return grid;
	}

	protected ColumnModel createColumnModel() {
		List<ColumnConfig> columns = new ArrayList<ColumnConfig>();

		columns.add(new ColumnConfig("name", messages.name(), 150));
		columns.add(new ColumnConfig("project", messages.project(), 150));
		columns.add(new ColumnConfig("partner", messages.partner(), 150));
		columns.add(new ColumnConfig("area", messages.area(), 150));
		columns.add(new ColumnConfig("timeperiod", messages.timePeriod(), 300));

		return new ColumnModel(columns);
	}

	@Override
	protected void initToolBar() {
		toolBar.addButton(UIActions.add, I18N.CONSTANTS.add(), icons.add());
		toolBar.addButton(UIActions.delete, messages.delete(), icons.delete());
	}

	@Override
	public void init(DbTargetEditor editor, UserDatabaseDTO db,
			ListStore<TargetDTO> store) {
		super.init(editor, store);
		this.setHeading(I18N.MESSAGES.projectsForDatabase(db.getName()));
	}

	@Override
	public FormDialogTether showAddDialog(TargetDTO target,FormDialogCallback callback) {

		TargetForm form = new TargetForm();
		form.getBinding().bind(target);
		
		FormDialogImpl<TargetForm> dlg = new FormDialogImpl<TargetForm>(form);
		dlg.setWidth(450);
		dlg.setHeight(300);
		dlg.setHeading(messages.createTarget());

		dlg.show(callback);

		return dlg;
	}
}

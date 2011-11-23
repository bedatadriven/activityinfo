package org.sigmah.client.page.config;

import java.util.ArrayList;
import java.util.List;

import org.sigmah.client.dispatch.Dispatcher;
import org.sigmah.client.i18n.I18N;
import org.sigmah.client.icon.IconImageBundle;
import org.sigmah.client.page.common.dialog.FormDialogCallback;
import org.sigmah.client.page.common.dialog.FormDialogImpl;
import org.sigmah.client.page.common.dialog.FormDialogTether;
import org.sigmah.client.page.common.grid.AbstractEditorTreeGridView;
import org.sigmah.client.page.common.grid.ImprovedCellTreeGridSelectionModel;
import org.sigmah.client.page.common.toolbar.UIActions;
import org.sigmah.shared.dto.ActivityDTO;
import org.sigmah.shared.dto.AttributeDTO;
import org.sigmah.shared.dto.AttributeGroupDTO;
import org.sigmah.shared.dto.EntityDTO;
import org.sigmah.shared.dto.IndicatorDTO;
import org.sigmah.shared.dto.UserDatabaseDTO;

import com.extjs.gxt.ui.client.GXT;
import com.extjs.gxt.ui.client.Style;
import com.extjs.gxt.ui.client.binding.FieldBinding;
import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.data.ModelIconProvider;
import com.extjs.gxt.ui.client.data.TreeModel;
import com.extjs.gxt.ui.client.dnd.DND;
import com.extjs.gxt.ui.client.dnd.TreeGridDragSource;
import com.extjs.gxt.ui.client.dnd.TreeGridDropTarget;
import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.DNDEvent;
import com.extjs.gxt.ui.client.event.DNDListener;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.GridEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.MenuEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.store.Store;
import com.extjs.gxt.ui.client.store.TreeStore;
import com.extjs.gxt.ui.client.util.Margins;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.grid.CellEditor;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.EditorGrid;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.layout.BorderLayout;
import com.extjs.gxt.ui.client.widget.layout.BorderLayoutData;
import com.extjs.gxt.ui.client.widget.menu.Menu;
import com.extjs.gxt.ui.client.widget.menu.MenuItem;
import com.extjs.gxt.ui.client.widget.menu.SeparatorMenuItem;
import com.extjs.gxt.ui.client.widget.treegrid.CellTreeGridSelectionModel;
import com.extjs.gxt.ui.client.widget.treegrid.EditorTreeGrid;
import com.extjs.gxt.ui.client.widget.treegrid.TreeGrid;
import com.extjs.gxt.ui.client.widget.treegrid.TreeGridCellRenderer;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.google.inject.Inject;

/**
 * @author Muhammad Abid
 */

public class TargetIndicatorView extends
		AbstractEditorTreeGridView<ModelData, TargetIndicatorPresenter> implements
		TargetIndicatorPresenter.View {

	protected final Dispatcher service;

	protected EditorTreeGrid<ModelData> tree;

	protected UserDatabaseDTO db;

	@Inject
	public TargetIndicatorView(Dispatcher service) {
		this.service = service;
	}

	@Override
	public void init(TargetIndicatorPresenter presenter, UserDatabaseDTO db,
			TreeStore store) {

		this.db = db;
		super.init(presenter, store);
		
		setBorders(false);
		setHeaderVisible(false);
		setFrame(false);
	}

	@Override
	protected Grid<ModelData> createGridAndAddToContainer(Store store) {

		final TreeStore treeStore = (TreeStore) store;

		tree = new EditorTreeGrid<ModelData>(treeStore, createColumnModel());
		tree.setSelectionModel(new ImprovedCellTreeGridSelectionModel<ModelData>());
		tree.setClicksToEdit(EditorGrid.ClicksToEdit.TWO);
		tree.setAutoExpandColumn("name");
		tree.setHideHeaders(true);
		tree.setLoadMask(true);
		// tree.setContextMenu(createContextMenu());

		tree.setIconProvider(new ModelIconProvider<ModelData>() {
			public AbstractImagePrototype getIcon(ModelData model) {

				return null;

			}
		});
		tree.addListener(Events.CellClick, new Listener<GridEvent>() {
			public void handleEvent(GridEvent ge) {
				// TODO show form
			}
		});

		add(tree, new BorderLayoutData(Style.LayoutRegion.CENTER));

		return tree;
	}

	@Override
	protected void initToolBar() {


	}

	private ColumnModel createColumnModel() {

		List<ColumnConfig> columns = new ArrayList<ColumnConfig>();

		TextField<String> nameField = new TextField<String>();
		nameField.setAllowBlank(false);

		ColumnConfig nameColumn = new ColumnConfig("name",
				I18N.CONSTANTS.name(), 150);
		nameColumn.setEditor(new CellEditor(nameField));
		nameColumn.setRenderer(new TreeGridCellRenderer());

		columns.add(nameColumn);
		
		TextField<String> valueField = new TextField<String>();
		valueField.setAllowBlank(false);
		
		ColumnConfig valueColumn = new ColumnConfig("units",
				I18N.CONSTANTS.value(), 150);
		valueColumn.setEditor(new CellEditor(valueField));
//		valueColumn.setRenderer(new TreeGridCellRenderer());

		columns.add(valueColumn);
		

		return new ColumnModel(columns);
	}

	protected Class formClassForSelection(ModelData sel) {

		return null;

	}
}
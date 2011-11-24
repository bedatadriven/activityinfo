package org.sigmah.client.page.config;

import java.util.ArrayList;
import java.util.List;

import org.sigmah.client.dispatch.Dispatcher;
import org.sigmah.client.i18n.I18N;
import org.sigmah.client.icon.IconImageBundle;
import org.sigmah.client.page.common.grid.AbstractEditorTreeGridView;
import org.sigmah.client.page.common.grid.ImprovedCellTreeGridSelectionModel;
import org.sigmah.shared.dto.ActivityDTO;
import org.sigmah.shared.dto.IndicatorDTO;
import org.sigmah.shared.dto.TargetValueDTO;
import org.sigmah.shared.dto.UserDatabaseDTO;
import com.extjs.gxt.ui.client.Style;
import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.data.ModelIconProvider;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.GridEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.store.Store;
import com.extjs.gxt.ui.client.store.TreeStore;
import com.extjs.gxt.ui.client.widget.form.Field;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.grid.CellEditor;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.EditorGrid;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.layout.BorderLayoutData;
import com.extjs.gxt.ui.client.widget.treegrid.EditorTreeGrid;
import com.extjs.gxt.ui.client.widget.treegrid.TreeGridCellRenderer;
import com.google.gwt.core.client.GWT;
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
	TargetIndicatorPresenter presenter ;
	protected UserDatabaseDTO db;

	@Inject
	public TargetIndicatorView(Dispatcher service) {
		this.service = service;
	}

	@Override
	public void init(TargetIndicatorPresenter presenter, UserDatabaseDTO db,
			TreeStore store) {

		this.db = db;
		this.presenter = presenter;
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

		tree.setIconProvider(new ModelIconProvider<ModelData>() {
			public AbstractImagePrototype getIcon(ModelData model) {

			 if (model instanceof ActivityDTO) {
                    return IconImageBundle.ICONS.activity();
                } else if (model instanceof TargetValueDTO) {
                    return IconImageBundle.ICONS.indicator();
                } else {
                    return null;
                }

			}
		});
		tree.addListener(Events.CellClick, new Listener<GridEvent>() {
			public void handleEvent(GridEvent ge) {
				// TODO show form
			}
		});
		
		tree.addListener(Events.OnKeyDown, new Listener<GridEvent>() {

			@Override
			public void handleEvent(GridEvent be) {
				if(!(be.getModel() instanceof TargetValueDTO)){
					presenter.rejectChanges();
				}
			}
		});
		
		tree.addListener(Events.AfterEdit, new Listener<GridEvent>() {

			@Override
			public void handleEvent(GridEvent be) {
				if(be.getModel() instanceof TargetValueDTO){
					presenter.updateTargetValue();	
				}else{
					presenter.rejectChanges();
				}
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

		ColumnConfig nameColumn = new ColumnConfig("name",
				I18N.CONSTANTS.name(), 250);
		nameColumn.setRenderer(new TreeGridCellRenderer());

		columns.add(nameColumn);

		TextField<String> valueField = new TextField<String>();
		valueField.setAllowBlank(true);
		
		ColumnConfig valueColumn = new ColumnConfig("value",
				I18N.CONSTANTS.value(), 150);
		valueColumn.setEditor(new CellEditor(valueField));

		columns.add(valueColumn);

		return new ColumnModel(columns);
	}
}
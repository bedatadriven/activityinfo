package org.activityinfo.client.page.config;

import java.util.ArrayList;
import java.util.List;

import org.activityinfo.client.dispatch.Dispatcher;
import org.activityinfo.client.i18n.I18N;
import org.activityinfo.client.icon.IconImageBundle;
import org.activityinfo.client.page.common.grid.AbstractEditorTreeGridView;
import org.activityinfo.client.page.common.grid.ImprovedCellTreeGridSelectionModel;
import org.activityinfo.client.page.common.nav.Link;
import org.activityinfo.shared.dto.ActivityDTO;
import org.activityinfo.shared.dto.TargetValueDTO;
import org.activityinfo.shared.dto.UserDatabaseDTO;

import com.extjs.gxt.ui.client.Style;
import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.data.ModelIconProvider;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.GridEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.store.Store;
import com.extjs.gxt.ui.client.store.TreeStore;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.grid.CellEditor;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnData;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.EditorGrid;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.grid.GridCellRenderer;
import com.extjs.gxt.ui.client.widget.layout.BorderLayoutData;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.treegrid.EditorTreeGrid;
import com.extjs.gxt.ui.client.widget.treegrid.TreeGridCellRenderer;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.google.inject.Inject;

/**
 * @author Muhammad Abid
 */

public class TargetIndicatorView extends
		AbstractEditorTreeGridView<ModelData, TargetIndicatorPresenter> implements
		TargetIndicatorPresenter.View {

	private final Dispatcher service;

	private EditorTreeGrid<ModelData> tree;
	private TargetIndicatorPresenter presenter ;
	private UserDatabaseDTO db;

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
		setLayout(new FitLayout());
	}

	@Override
	protected Grid<ModelData> createGridAndAddToContainer(Store store) {

		final TreeStore treeStore = (TreeStore) store;

		tree = new EditorTreeGrid<ModelData>(treeStore, createColumnModel());
	    tree.setAutoExpandColumn("name");  
	    tree.setSelectionModel(new ImprovedCellTreeGridSelectionModel<ModelData>());
		tree.setClicksToEdit(EditorGrid.ClicksToEdit.ONE);
		tree.setLoadMask(true);
		tree.setStateId("TargetValueGrid" + db.getId());
		
		tree.setIconProvider(new ModelIconProvider<ModelData>() {
			public AbstractImagePrototype getIcon(ModelData model) {

			 if (model instanceof ActivityDTO) {
                    return IconImageBundle.ICONS.activity();
                } else if (model instanceof TargetValueDTO) {
                    return IconImageBundle.ICONS.indicator();
                } else if(model instanceof Link){
                	return IconImageBundle.ICONS.folder();
                }else {
                    return null;
                }

			}
		});
	
		addBeforeEditListener();
		addAfterEditListener();
		
		add(tree, new BorderLayoutData(Style.LayoutRegion.CENTER));

		return tree;
	}
	
	private void addBeforeEditListener(){
		tree.addListener(Events.BeforeEdit, new Listener<GridEvent>() {

			@Override
			public void handleEvent(GridEvent be) {
				if(!(be.getModel() instanceof TargetValueDTO)){
					be.setCancelled(true);
				}
			}
			
		});
	}
	
	private void addAfterEditListener(){

		tree.addListener(Events.AfterEdit, new Listener<GridEvent>() {

			@Override
			public void handleEvent(GridEvent be) {
				if(be.getModel() instanceof TargetValueDTO){
					
					if(be.getRecord().getChanges().size()>0){
						presenter.updateTargetValue();	
					}						
				}
			}
			
		});
	}

	@Override
	public void expandAll(){
		tree.expandAll();
	}
	
	@Override
	protected void initToolBar() {

	}

	private ColumnModel createColumnModel() {

		List<ColumnConfig> columns = new ArrayList<ColumnConfig>();

		ColumnConfig nameColumn = new ColumnConfig("name",
				I18N.CONSTANTS.indicator(), 250);
		nameColumn.setRenderer(new TreeGridCellRenderer());
		columns.add(nameColumn);

		TextField<String> valueField = new TextField<String>();
		valueField.setAllowBlank(true);
		
		ColumnConfig valueColumn = new ColumnConfig("value",
				I18N.CONSTANTS.targetValue(), 150);
		valueColumn.setEditor(new CellEditor(valueField));
		valueColumn.setRenderer(new TargetValueCellRenderer());
		columns.add(valueColumn);

		return new ColumnModel(columns);
	}
	
	private class TargetValueCellRenderer implements GridCellRenderer<ModelData>{		
		
		public TargetValueCellRenderer(){}

		@Override
		public Object render(ModelData model, String property,
				ColumnData config, int rowIndex, int colIndex, ListStore store,
				Grid grid) {
			
			if(model instanceof TargetValueDTO  && model.get("value")==null){
				config.style = "color:gray;font-style:italic;";
				return	I18N.CONSTANTS.noTarget();
				
			}else if(model.get("value")!=null){
				
				return model.get("value");
			}
			
			return "";
		}	
	}
}
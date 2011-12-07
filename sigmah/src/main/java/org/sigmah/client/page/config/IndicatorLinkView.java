package org.sigmah.client.page.config;

import java.util.ArrayList;
import java.util.List;

import org.sigmah.client.dispatch.Dispatcher;
import org.sigmah.client.i18n.I18N;
import org.sigmah.client.icon.IconImageBundle;
import org.sigmah.client.page.common.filter.IndicatorTreePanel;
import org.sigmah.client.page.common.grid.AbstractEditorTreeGridView;
import org.sigmah.client.page.common.grid.ImprovedCellTreeGridSelectionModel;
import org.sigmah.client.page.common.nav.Link;
import org.sigmah.client.page.common.widget.MappingComboBox;
import org.sigmah.shared.dto.ActivityDTO;
import org.sigmah.shared.dto.TargetValueDTO;
import org.sigmah.shared.dto.UserDatabaseDTO;
import com.extjs.gxt.ui.client.Style;
import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.data.ModelIconProvider;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.GridEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SelectionChangedEvent;
import com.extjs.gxt.ui.client.event.SelectionChangedListener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.store.Store;
import com.extjs.gxt.ui.client.store.TreeStore;
import com.extjs.gxt.ui.client.util.Margins;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.HorizontalPanel;
import com.extjs.gxt.ui.client.widget.Html;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.grid.CellEditor;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnData;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.EditorGrid;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.grid.GridCellRenderer;
import com.extjs.gxt.ui.client.widget.layout.BorderLayout;
import com.extjs.gxt.ui.client.widget.layout.BorderLayoutData;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.treegrid.EditorTreeGrid;
import com.extjs.gxt.ui.client.widget.treegrid.TreeGridCellRenderer;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.google.inject.Inject;

public class IndicatorLinkView extends
		AbstractEditorTreeGridView<ModelData, LinkIndicatorPresenter> implements
		LinkIndicatorPresenter.View {

	private final Dispatcher service;

	private EditorTreeGrid<ModelData> sourceTree;
	private IndicatorTreePanel indicatorTreePanel;
	private HorizontalPanel listContainer;

	private UserDatabaseDTO db;

	private MappingComboBox databaseCombo;
	
	@Inject
	public IndicatorLinkView(Dispatcher service) {
		this.service = service;
	}

	

	@Override
	public void init(LinkIndicatorPresenter presenter, UserDatabaseDTO db,
			TreeStore store, TreeStore destinationStore) {

		this.db = db;
		this.presenter = presenter;
		super.init(presenter, store);

		setLayout(new BorderLayout());
		setHeaderVisible(false);

		createListContainer();
		createDestinationContainer();
	}
	
	private void createListContainer() {
		listContainer = new HorizontalPanel();
		listContainer.setBorders(false);
		
		listContainer.add(new Html(I18N.CONSTANTS.showLinksBetweenThisDatabaseAnd()));
		listContainer.add(createDatabaseList());
		
		BorderLayoutData layout = new BorderLayoutData(Style.LayoutRegion.NORTH);
		layout.setSplit(false);
		layout.setCollapsible(false);
		layout.setSize(30);
		layout.setMargins(new Margins(5, 0, 0, 5));

		add(listContainer, layout);
	}

	private MappingComboBox createDatabaseList(){
		databaseCombo = new MappingComboBox();
		databaseCombo.setAllowBlank(false);
		databaseCombo.addSelectionChangedListener(new SelectionChangedListener<ModelData>() {

			@Override
			public void selectionChanged(SelectionChangedEvent<ModelData> se) {
				loadDestinationIndicators();
			}
		});
		
		return databaseCombo;	
	}
	
	private void createDestinationContainer(){
		indicatorTreePanel =  IndicatorTreePanel.forSingleDatabase(service);
		indicatorTreePanel.setLeafCheckableOnly();
		
		BorderLayoutData layout = new BorderLayoutData(Style.LayoutRegion.EAST);
		layout.setSplit(true);
		layout.setCollapsible(true);
		layout.setSize(400);
		layout.setMargins(new Margins(0, 0, 0, 5));

		add(indicatorTreePanel, layout);
	}

	
	public void loadDestinationIndicators(){
		List selectedItems = databaseCombo.getSelection();
		ModelData model = (ModelData) selectedItems .get(0);
		indicatorTreePanel.setHeading(I18N.CONSTANTS.destinationIndicator() + " - " + String.valueOf(model.get("label")));
		indicatorTreePanel.loadSingleDatabase(presenter.loadDestinationDatabase((Integer)model.get("value")));
	}
	
	@Override
	public void addDatabasesToList(List<ModelData> models){
		databaseCombo.clear();
		
		for(ModelData model:models){
			if((Integer)model.get("id") == db.getId()){
				continue;
			}
			
			databaseCombo.add(model.get("id"), (String)model.get("name"));
		}
		
		databaseCombo.setValue(databaseCombo.getStore().getAt(0));
	}
	
	@Override
	protected Grid<ModelData> createGridAndAddToContainer(Store store) {

		final ContentPanel sourceIndicatorsContainer = new ContentPanel(new FitLayout());
		sourceIndicatorsContainer.setHeading(I18N.CONSTANTS.sourceIndicators() + " - " + db.getName());
		
		final TreeStore treeStore = (TreeStore) store;

		sourceTree = new EditorTreeGrid<ModelData>(treeStore, createColumnModel());
		sourceTree.setAutoExpandColumn("name");
		sourceTree.setSelectionModel(new ImprovedCellTreeGridSelectionModel<ModelData>());
		sourceTree.setClicksToEdit(EditorGrid.ClicksToEdit.ONE);
		sourceTree.setLoadMask(true);
		sourceTree.setStateId("IndicatorLink" + db.getId());

		sourceTree.setIconProvider(new ModelIconProvider<ModelData>() {
			public AbstractImagePrototype getIcon(ModelData model) {

				if (model instanceof ActivityDTO) {
					return IconImageBundle.ICONS.activity();
				} else if (model instanceof Link) {
					return IconImageBundle.ICONS.folder();
				} else {
					return null;
				}

			}
		});

		addBeforeEditListner();
		addAfterEditListner();

		sourceIndicatorsContainer.add(sourceTree);
		add(sourceIndicatorsContainer, new BorderLayoutData(Style.LayoutRegion.CENTER));

		return sourceTree;
	}

	private ColumnModel createColumnModel() {

		List<ColumnConfig> columns = new ArrayList<ColumnConfig>();

		ColumnConfig nameColumn = new ColumnConfig("name",
				I18N.CONSTANTS.sourceIndicator(), 200);
		nameColumn.setRenderer(new TreeGridCellRenderer());
		columns.add(nameColumn);

		TextField<String> valueField = new TextField<String>();
		valueField.setAllowBlank(true);

		ColumnConfig valueColumn = new ColumnConfig("value",
				I18N.CONSTANTS.destinationIndicator(), 150);
		valueColumn.setEditor(new CellEditor(valueField));
		valueColumn.setRenderer(new GridCellRenderer<ModelData>() {

			@Override
			public Object render(ModelData model, String property,
					ColumnData config, int rowIndex, int colIndex,
					ListStore<ModelData> store, Grid<ModelData> grid) {

				if (model instanceof TargetValueDTO
						&& model.get("value") == null) {
					config.style = "color:gray;font-style:italic;";
					return I18N.CONSTANTS.notLinked();

				} else if (model.get("value") != null) {

					return model.get("value");
				}

				return "";
			}
		});
		columns.add(valueColumn);

		return new ColumnModel(columns);
	}

	private void addBeforeEditListner() {

		sourceTree.addListener(Events.BeforeEdit, new Listener<GridEvent>() {

			@Override
			public void handleEvent(GridEvent be) {

			}

		});
	}

	private void addAfterEditListner() {

		sourceTree.addListener(Events.AfterEdit, new Listener<GridEvent>() {

			@Override
			public void handleEvent(GridEvent be) {

			}

		});
	}

	@Override
	protected void initToolBar() {

	}
}

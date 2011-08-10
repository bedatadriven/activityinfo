package org.sigmah.client.page.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.sigmah.client.dispatch.AsyncMonitor;
import org.sigmah.client.i18n.I18N;
import org.sigmah.client.icon.IconImageBundle;
import org.sigmah.client.mvp.Filter;
import org.sigmah.client.page.common.toolbar.ActionListener;
import org.sigmah.client.page.common.toolbar.ActionToolBar;
import org.sigmah.client.page.common.toolbar.UIActions;
import org.sigmah.client.page.config.LockedPeriodsPresenter.AddLockedPeriodView;
import org.sigmah.client.page.config.LockedPeriodsPresenter.LockedPeriodListEditor;
import org.sigmah.shared.dto.LockedPeriodDTO;
import org.sigmah.shared.dto.UserDatabaseDTO;

import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.MessageBoxEvent;
import com.extjs.gxt.ui.client.event.SelectionChangedEvent;
import com.extjs.gxt.ui.client.event.SelectionChangedListener;
import com.extjs.gxt.ui.client.event.SelectionEvent;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.store.Record;
import com.extjs.gxt.ui.client.store.Store;
import com.extjs.gxt.ui.client.store.StoreEvent;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.form.CheckBox;
import com.extjs.gxt.ui.client.widget.form.DateField;
import com.extjs.gxt.ui.client.widget.grid.CellEditor;
import com.extjs.gxt.ui.client.widget.grid.CheckColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnData;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.EditorGrid;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.grid.GridCellRenderer;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.i18n.client.DateTimeFormat;

public class LockedPeriodGrid extends ContentPanel implements LockedPeriodListEditor {
	
	private EventBus eventBus = new SimpleEventBus();
	
	private ListStore<LockedPeriodDTO> lockedPeriodStore;
	private EditorGrid<LockedPeriodDTO> gridLockedPeriods;
	private UserDatabaseDTO userDatabase;
	private ActionToolBar toolbarActions;
	private AddLockedPeriodView addLockedPeriod;
	private LockedPeriodDTO lockedPeriod;

	public LockedPeriodGrid() {
		super();
		
		initializeComponent();
		
		createGrid();
	}

	private void createGrid() {
		List<ColumnConfig> configs = new ArrayList<ColumnConfig>();
		
		configs.add(createEnabledColumn());
	    configs.add(createParentTypeColumn());
	    configs.add(createParentNameColumn());
	    configs.add(createNameColumn());
	    configs.add(createStartDateColumn());
	    configs.add(createEndDateColumn());
	    //configs.add(createTotalTimeColumn());
	    
	    gridLockedPeriods = new EditorGrid<LockedPeriodDTO>(
	    		lockedPeriodStore, new ColumnModel(configs));
	    
	    gridLockedPeriods.getSelectionModel().addSelectionChangedListener(new SelectionChangedListener<LockedPeriodDTO>() {
			@Override
			public void selectionChanged(SelectionChangedEvent<LockedPeriodDTO> se) {
				setDeleteEnabled(se.getSelectedItem() != null);
				lockedPeriod = se.getSelectedItem();
			}
		});
	    
	    gridLockedPeriods.getSelectionModel().addListener(Events.SelectionChange, new Listener<SelectionEvent<LockedPeriodDTO>>() {
			@Override
			public void handleEvent(SelectionEvent<LockedPeriodDTO> be) {
				setDeleteEnabled(be.getModel() != null);
				if (be.getModel() != null) {
					lockedPeriod = be.getModel();
				}
			}
		});
	    
	    gridLockedPeriods.addListener(Events.OnClick, new Listener<ComponentEvent>() {
			@Override
			public void handleEvent(ComponentEvent be) {
				lockedPeriod = gridLockedPeriods.getSelectionModel().getSelectedItem();
				setDeleteEnabled(gridLockedPeriods.getSelectionModel().getSelectedItem() != null);
			}
		});
	    
	    add(gridLockedPeriods);
	}

	private ColumnConfig createParentNameColumn() {
		ColumnConfig columnName = new ColumnConfig();
		columnName.setHeader("parentName");
		columnName.setDataIndex("parentName");
		columnName.setWidth(150);
		columnName.setRowHeader(true); 
		columnName.setRenderer(new GridCellRenderer<LockedPeriodDTO>() {
			@Override
			public Object render(LockedPeriodDTO model, String property,
					ColumnData config, int rowIndex, int colIndex,
					ListStore<LockedPeriodDTO> store, Grid<LockedPeriodDTO> grid) {
				return model.getParentName();
			}
		});
		
	    return columnName;
	}

	private ColumnConfig createEnabledColumn() {
	    CheckColumnConfig columnEnabled = new CheckColumnConfig("enabled", "Enabled?", 55);  
	    columnEnabled.setEditor(new CellEditor(new CheckBox()));
	    
	    return columnEnabled;
	}

	private ColumnConfig createTotalTimeColumn() {
		ColumnConfig columnTotalTime = new ColumnConfig();
	    columnTotalTime.setId("endDate");  
	    columnTotalTime.setHeader(I18N.CONSTANTS.timePeriod());  
	    columnTotalTime.setWidth(100);  
	    columnTotalTime.setRowHeader(true);
	    columnTotalTime.setDateTimeFormat(DateTimeFormat.getFormat("yyyy-MMM-dd"));
	    
		return columnTotalTime;
	}

	private ColumnConfig createNameColumn() {
		ColumnConfig columnName = new ColumnConfig();  
		columnName.setId("name");  
		columnName.setHeader(I18N.CONSTANTS.name());  
		columnName.setWidth(150);  
		columnName.setRowHeader(true); 
		
	    return columnName;
	}

	private ColumnConfig createEndDateColumn() {
		DateField datefieldEndDate = new DateField();
	    datefieldEndDate.getPropertyEditor().setFormat(DateTimeFormat.getFormat("dd/MM/yyyy")); 
	    
	    ColumnConfig columnEndDate = new ColumnConfig();  
	    columnEndDate.setEditor(new CellEditor(datefieldEndDate));
	    columnEndDate.setId("toDate");  
	    columnEndDate.setHeader(I18N.CONSTANTS.toDate());  
	    columnEndDate.setWidth(100);  
	    columnEndDate.setRowHeader(true);  
	    columnEndDate.setDateTimeFormat(DateTimeFormat.getFormat("yyyy-MMM-dd"));
	    
		return columnEndDate;
	}

	private ColumnConfig createStartDateColumn() {
		DateField datefieldStartDate = new DateField();
	    datefieldStartDate.getPropertyEditor().setFormat(DateTimeFormat.getFormat("dd/MM/yyyy")); 
	    
	    ColumnConfig columnStartDate = new ColumnConfig();  
	    columnStartDate.setEditor(new CellEditor(datefieldStartDate));
	    columnStartDate.setId("fromDate");  
	    columnStartDate.setHeader(I18N.CONSTANTS.fromDate());  
	    columnStartDate.setWidth(100);  
	    columnStartDate.setRowHeader(true);
	    columnStartDate.setDateTimeFormat(DateTimeFormat.getFormat("yyyy-MMM-dd"));
	    
		return columnStartDate;
	}
	
	private ColumnConfig createParentTypeColumn() {
	    ColumnConfig columnParentType = new ColumnConfig();  
	    GridCellRenderer<LockedPeriodDTO> iconRenderer = new GridCellRenderer<LockedPeriodDTO>() {
			@Override
			public Object render(LockedPeriodDTO model, String property,
					ColumnData config, int rowIndex, int colIndex,
					ListStore<LockedPeriodDTO> store, Grid<LockedPeriodDTO> grid) {
				
				if (model.getActivity() != null) {
					return IconImageBundle.ICONS.activity().getHTML();
				}
				
				if (model.getUserDatabase() != null) {
					return IconImageBundle.ICONS.database().getHTML();
				}
				
				if (model.getProject() != null) {
					return IconImageBundle.ICONS.project().getHTML();
				}
				
				return null;
			}
		}; 
	    //columnStartDate.setId("fromDate");
		columnParentType.setHeader(I18N.CONSTANTS.type());  
	    columnParentType.setWidth(48);
	    columnParentType.setRowHeader(true);
	    columnParentType.setRenderer(iconRenderer);
		
		return columnParentType;
	}

	private void initializeComponent() {
		setHeading("Manage time locks on databases, projects and activities");
		setLayout(new FitLayout());

		createListStore();
		createActionToolbar();
		createAddLockedPeriodDialog();
	}

	private void createListStore() {
		lockedPeriodStore = new ListStore<LockedPeriodDTO>();
		lockedPeriodStore.addListener(Store.DataChanged, new Listener<StoreEvent>(){
			@Override
			public void handleEvent(StoreEvent be) {
				toolbarActions.setUpdateEnabled(true);
			}
		});
	}

	private void createActionToolbar() {
		toolbarActions = new ActionToolBar(new ActionListener(){
			@Override
			public void onUIAction(String actionId) {
				if (actionId.equals(UIActions.add)) {
					addLockedPeriod.startCreate();
				} else if (actionId.equals(UIActions.delete)) {
					eventBus.fireEvent(new RequestDeleteEvent());
				} else if (actionId.equals(UIActions.save)) {
					eventBus.fireEvent(new UpdateEvent());
				} else if (actionId.equals(UIActions.discardChanges)) {
					lockedPeriodStore.rejectChanges();
				}
			}});
		toolbarActions.addDeleteButton();
		toolbarActions.addCreateButton();
		toolbarActions.addSaveSplitButton();
		this.setTopComponent(toolbarActions);
	}

	private void createAddLockedPeriodDialog() {
		addLockedPeriod = new AddLockedPeriodDialog();
		addLockedPeriod.addCreateHandler(new CreateHandler() {
			@Override
			public void onCreate(CreateEvent createEvent) {
				lockedPeriod = addLockedPeriod.getValue();
				eventBus.fireEvent(new CreateEvent());
			}
		});
		addLockedPeriod.addCancelCreateHandler(new CancelCreateHandler() {
			@Override
			public void onCancelCreate(CancelCreateEvent createEvent) {
				addLockedPeriod.stopCreate();
			}
		});
	}

	@Override
	public void create(LockedPeriodDTO item) {
		lockedPeriodStore.add(item);
		addLockedPeriod.stopCreate();
	}

	@Override
	public void update(LockedPeriodDTO item) {
		lockedPeriodStore.commitChanges();
	}

	@Override
	public void delete(LockedPeriodDTO item) {
		lockedPeriodStore.remove(item);
	}

	@Override
	public void setCreateEnabled(boolean createEnabled) {
		toolbarActions.setAddEnabled(createEnabled);
	}

	@Override
	public void setUpdateEnabled(boolean updateEnabled) {
		toolbarActions.setUpdateEnabled(updateEnabled);
	}

	@Override
	public void setDeleteEnabled(boolean deleteEnabled) {
		toolbarActions.setDeleteEnabled(deleteEnabled);
	}

	@Override
	public void initialize() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public AsyncMonitor getAsyncMonitor() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setValue(LockedPeriodDTO value) {
		//gridLockedPeriods.getSelectionModel().select(value, false);
	}

	@Override
	public LockedPeriodDTO getValue() {
		return lockedPeriod;
	}

	@Override
	public HandlerRegistration addCreateHandler(CreateHandler handler) {
		return eventBus.addHandler(CreateEvent.TYPE, handler);
	}

	@Override
	public HandlerRegistration addUpdateHandler(UpdateHandler handler) {
		return eventBus.addHandler(UpdateEvent.TYPE, handler);
	}

	@Override
	public HandlerRegistration addConfirmDeleteHandler(ConfirmDeleteHandler handler) {
		return eventBus.addHandler(ConfirmDeleteEvent.TYPE, handler);
	}

	@Override
	public HandlerRegistration addRefreshHandler(
			org.sigmah.client.mvp.CrudView.RefreshHandler handler) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public HandlerRegistration addFilterHandler(
			FilterHandler filter) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void cancelUpdate(LockedPeriodDTO item) {
		gridLockedPeriods.stopEditing(true);
	}

	@Override
	public void filter(Filter<LockedPeriodDTO> filter) {
		// TODO Auto-generated method stub
	}

	@Override
	public void askConfirmDelete(LockedPeriodDTO item) {
		// TODO: i18n
		MessageBox.confirm("Delete LockedPeriod", "Wanna delete this lockedPeriod?", new Listener<MessageBoxEvent>() {
			@Override
			public void handleEvent(MessageBoxEvent be) {
				eventBus.fireEvent(new ConfirmDeleteEvent());
			}
		});
	}

	@Override
	public void setParent(UserDatabaseDTO parent) {
		this.userDatabase = parent;
		addLockedPeriod.setUserDatabase(parent);
	}

	@Override
	public void setCanCancelUpdate(boolean canCancelUpdate) {
		// TODO Auto-generated method stub
	}

	@Override
	public void setItems(List<LockedPeriodDTO> items) {
		lockedPeriodStore.removeAll();
		lockedPeriodStore.add(new ArrayList<LockedPeriodDTO>(items));
	}

	@Override
	public void removeFilter() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public HandlerRegistration addCancelUpdateHandler(
			CancelUpdateHandler handler) {
		return eventBus.addHandler(CancelUpdateEvent.TYPE, handler);
	}

	@Override
	public HandlerRegistration addRequestDeleteHandler(
			RequestDeleteHandler handler) {
		return eventBus.addHandler(RequestDeleteEvent.TYPE, handler);
	}

	@Override
	public HandlerRegistration addCancelCreateHandler(
			org.sigmah.client.mvp.CanCreate.CancelCreateHandler handler) {
		return eventBus.addHandler(CancelCreateEvent.TYPE, handler);
	}

	@Override
	public HandlerRegistration addStartCreateHandler(
			org.sigmah.client.mvp.CanCreate.StartCreateHandler handler) {
		return eventBus.addHandler(StartCreateEvent.TYPE, handler);
	}

	@Override
	public List<LockedPeriodDTO> getUnsavedItems() {
		List<LockedPeriodDTO> unsavedItems = new ArrayList<LockedPeriodDTO>();
		
		List<Record> modifiedRecords = lockedPeriodStore.getModifiedRecords();
		for (Record record : modifiedRecords) {
			unsavedItems.add((LockedPeriodDTO)record.getModel());
		}
		
		return unsavedItems;
	}

	@Override
	public boolean hasChangedItems() {
		return getUnsavedItems().size() > 0;
	}

	@Override
	public boolean hasSingleChangedItem() {
		return getUnsavedItems().size() == 1;
	}

	@Override
	public Map<String, Object> getChanges(LockedPeriodDTO item) {
		for (Record record : lockedPeriodStore.getModifiedRecords()) {
			LockedPeriodDTO lockedPeriod = (LockedPeriodDTO)record.getModel();
			if (lockedPeriod.getId() == item.getId()) {
				Map<String, Object> changes = new HashMap<String, Object>();
				for (String property : record.getPropertyNames()) {
					changes.put(property, lockedPeriod.get(property));
				}
				
				return changes;
			}
		}
		
		return null;
	}

}

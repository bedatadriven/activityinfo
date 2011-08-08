package org.sigmah.client.page.config;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.sigmah.client.dispatch.AsyncMonitor;
import org.sigmah.client.i18n.I18N;
import org.sigmah.client.mvp.Filter;
import org.sigmah.client.page.common.toolbar.ActionListener;
import org.sigmah.client.page.common.toolbar.ActionToolBar;
import org.sigmah.client.page.common.toolbar.UIActions;
import org.sigmah.client.page.config.LockedPeriodsPresenter.AddLockedPeriodView;
import org.sigmah.client.page.config.LockedPeriodsPresenter.LockedPeriodListEditor;
import org.sigmah.shared.dto.LockedPeriodDTO;
import org.sigmah.shared.dto.UserDatabaseDTO;

import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.MessageBoxEvent;
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
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.EditorGrid;
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
	    configs.add(createNameColumn()); 
	    configs.add(createStartDateColumn());
	    configs.add(createEndDateColumn());
	    configs.add(createTotalTimeColumn());
	    
	    gridLockedPeriods = new EditorGrid<LockedPeriodDTO>(
	    		lockedPeriodStore, new ColumnModel(configs));
	    
	    gridLockedPeriods.getSelectionModel().addListener(Events.SelectionChange, new Listener<SelectionEvent<LockedPeriodDTO>>() {
			@Override
			public void handleEvent(SelectionEvent<LockedPeriodDTO> be) {
				setDeleteEnabled(be.getModel() != null);
				if (be.getModel() != null) {
					lockedPeriod = be.getModel();
				}
			}
		});
	    
	    add(gridLockedPeriods);
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
	    columnTotalTime.setWidth(200);  
	    columnTotalTime.setRowHeader(true);
	    columnTotalTime.setDateTimeFormat(DateTimeFormat.getFormat("yyyy-MMM-dd"));
	    
		return columnTotalTime;
	}

	private ColumnConfig createNameColumn() {
		ColumnConfig columnName = new ColumnConfig();  
		columnName.setId("name");  
		columnName.setHeader(I18N.CONSTANTS.name());  
		columnName.setWidth(200);  
		columnName.setRowHeader(true); 
		
	    return columnName;
	}

	private ColumnConfig createEndDateColumn() {
		DateField datefieldEndDate = new DateField();
	    datefieldEndDate.getPropertyEditor().setFormat(DateTimeFormat.getFormat("dd/MM/yyyy")); 
	    
	    ColumnConfig columnEndDate = new ColumnConfig();  
	    columnEndDate.setEditor(new CellEditor(datefieldEndDate));
	    columnEndDate.setId("endDate");  
	    columnEndDate.setHeader(I18N.CONSTANTS.toDate());  
	    columnEndDate.setWidth(200);  
	    columnEndDate.setRowHeader(true);  
	    columnEndDate.setDateTimeFormat(DateTimeFormat.getFormat("yyyy-MMM-dd"));
	    
		return columnEndDate;
	}

	private ColumnConfig createStartDateColumn() {
		DateField datefieldStartDate = new DateField();
	    datefieldStartDate.getPropertyEditor().setFormat(DateTimeFormat.getFormat("dd/MM/yyyy")); 
	    
	    ColumnConfig columnStartDate = new ColumnConfig();  
	    columnStartDate.setEditor(new CellEditor(datefieldStartDate));
	    columnStartDate.setId("startDate");  
	    columnStartDate.setHeader(I18N.CONSTANTS.fromDate());  
	    columnStartDate.setWidth(200);  
	    columnStartDate.setRowHeader(true);
	    columnStartDate.setDateTimeFormat(DateTimeFormat.getFormat("yyyy-MMM-dd"));
	    
		return columnStartDate;
	}

	private void initializeComponent() {
		setHeading("Manage time locks on databases, projects and activities");

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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public HandlerRegistration addRequestDeleteHandler(
			RequestDeleteHandler handler) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public HandlerRegistration addCancelCreateHandler(
			org.sigmah.client.mvp.CanCreate.CancelCreateHandler handler) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public HandlerRegistration addStartCreateHandler(
			org.sigmah.client.mvp.CanCreate.StartCreateHandler handler) {
		// TODO Auto-generated method stub
		return null;
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
			if (record.getModel().equals(item)) {
				return record.getChanges();
			}
		}
		
		return null;
	}

}

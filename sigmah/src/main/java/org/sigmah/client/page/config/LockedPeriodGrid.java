package org.sigmah.client.page.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.sigmah.client.dispatch.AsyncMonitor;
import org.sigmah.client.dispatch.monitor.NullAsyncMonitor;
import org.sigmah.client.i18n.I18N;
import org.sigmah.client.mvp.CanCreate;
import org.sigmah.client.mvp.CanDelete;
import org.sigmah.client.mvp.CanUpdate;
import org.sigmah.client.mvp.Filter;
import org.sigmah.client.page.common.columns.EditCheckColumnConfig;
import org.sigmah.client.page.common.columns.EditDateColumn;
import org.sigmah.client.page.common.columns.ReadLockedPeriodTypeColumn;
import org.sigmah.client.page.common.columns.ReadTextColumn;
import org.sigmah.client.page.common.toolbar.ActionListener;
import org.sigmah.client.page.common.toolbar.ActionToolBar;
import org.sigmah.client.page.common.toolbar.UIActions;
import org.sigmah.client.page.config.LockedPeriodsPresenter.AddLockedPeriodView;
import org.sigmah.client.page.config.LockedPeriodsPresenter.LockedPeriodListEditor;
import org.sigmah.shared.dto.ActivityDTO;
import org.sigmah.shared.dto.LockedPeriodDTO;
import org.sigmah.shared.dto.UserDatabaseDTO;

import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.MessageBoxEvent;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.store.Record;
import com.extjs.gxt.ui.client.store.Store;
import com.extjs.gxt.ui.client.store.StoreEvent;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.EditorGrid;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.event.shared.SimpleEventBus;

public class LockedPeriodGrid extends ContentPanel implements LockedPeriodListEditor {
	
	private EventBus eventBus = new SimpleEventBus();
	private boolean mustConfirmDelete = true;
	
	// UI stuff
	private ListStore<LockedPeriodDTO> lockedPeriodStore;
	private EditorGrid<LockedPeriodDTO> gridLockedPeriods;
	private AsyncMonitor deletingMonitor = new NullAsyncMonitor();
	private AsyncMonitor creatingMonitor = new NullAsyncMonitor();
	private AsyncMonitor loadingMonitor = new NullAsyncMonitor();
	private AsyncMonitor updatingMonitor = new NullAsyncMonitor();

	// Data
	private UserDatabaseDTO userDatabase;
	private LockedPeriodDTO lockedPeriod;
	private ActivityDTO activityFilter= null;

	// Nested views
	private AddLockedPeriodView addLockedPeriod;
	private ActionToolBar toolbarActions;

	public LockedPeriodGrid() {
		super();
		
		initializeComponent();
		
		createListStore();
		createActionToolbar();
		createAddLockedPeriodDialog();
		createGrid();
	}

	private void createGrid() {
		List<ColumnConfig> configs = new ArrayList<ColumnConfig>();
		
		configs.add(new EditCheckColumnConfig("enabled", I18N.CONSTANTS.enabledColumn(), 55));
	    configs.add(new ReadLockedPeriodTypeColumn());
	    configs.add(new ReadTextColumn("parentName", I18N.CONSTANTS.parentName(), 150));
	    configs.add(new ReadTextColumn("name", I18N.CONSTANTS.name(), 100));
	    configs.add(new EditDateColumn("fromDate", I18N.CONSTANTS.fromDate(), 100));
	    configs.add(new EditDateColumn("toDate", I18N.CONSTANTS.toDate(), 100));
	    
	    gridLockedPeriods = new EditorGrid<LockedPeriodDTO>(
	    		lockedPeriodStore, new ColumnModel(configs));
	    
	    gridLockedPeriods.addListener(Events.OnClick, new Listener<ComponentEvent>() {
			@Override
			public void handleEvent(ComponentEvent be) {
				lockedPeriod = gridLockedPeriods.getSelectionModel().getSelectedItem();
				setDeleteEnabled(gridLockedPeriods.getSelectionModel().getSelectedItem() != null);
			}
		});
	    
	    add(gridLockedPeriods);
	}

	private void initializeComponent() {
		setHeading("Manage time locks on databases, projects and activities");
		setLayout(new FitLayout());
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
					eventBus.fireEvent(new StartCreateEvent());
				} else if (actionId.equals(UIActions.delete)) {
					eventBus.fireEvent(new RequestDeleteEvent());
				} else if (actionId.equals(UIActions.save)) {
					eventBus.fireEvent(new UpdateEvent());
				} else if (actionId.equals(UIActions.discardChanges)) {
					eventBus.fireEvent(new CancelUpdateEvent());
				}
			}});
		toolbarActions.addDeleteButton();
		toolbarActions.addCreateButton();
		toolbarActions.addSaveSplitButton();
		toolbarActions.setDeleteEnabled(false);
		toolbarActions.setUpdateEnabled(false);
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
				eventBus.fireEvent(new CancelCreateEvent());
			}
		});
	}

	@Override
	public void create(LockedPeriodDTO item) {
		lockedPeriodStore.add(item);
		addLockedPeriod.cancelCreate();
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
	public void cancelUpdate(LockedPeriodDTO item) {
		gridLockedPeriods.stopEditing(true);
	}

	@Override
	public void askConfirmDelete(LockedPeriodDTO item) {
		// TODO: i18n
		if (mustConfirmDelete) {
			MessageBox.confirm(I18N.CONSTANTS.deleteLockedPeriodTitle(), I18N.CONSTANTS.deleteLockedPeriodQuestion(), new Listener<MessageBoxEvent>() {
				@Override
				public void handleEvent(MessageBoxEvent be) {
					if (be.isCancelled()) {
						eventBus.fireEvent(new CancelDeleteEvent());
					} 
					eventBus.fireEvent(new ConfirmDeleteEvent());
				}
			});
		} else {
			eventBus.fireEvent(new ConfirmDeleteEvent());
		}
	}

	@Override
	public void setParent(UserDatabaseDTO parent) {
		this.userDatabase = parent;
		addLockedPeriod.setUserDatabase(parent);
	}

	@Override
	public void setItems(List<LockedPeriodDTO> items) {
		lockedPeriodStore.removeAll();
		lockedPeriodStore.add(filterLockedPeriodsByActivity(items));
	}

	private List<LockedPeriodDTO> filterLockedPeriodsByActivity(
			List<LockedPeriodDTO> items) {
		
		if (activityFilter != null) {
			// Remove LockedPeriods which have a different Activity then the activiftyFilter
			List<LockedPeriodDTO> lockedPeriodsFilteredByActivity = new ArrayList<LockedPeriodDTO>();
			for (LockedPeriodDTO lockedPeriod : items) {
				if (lockedPeriod.getActivity() != null) {
					// Activity as parent, only add when activity equals filter
					if (lockedPeriod.getActivity().getId() == activityFilter.getId()) {
						lockedPeriodsFilteredByActivity.add(lockedPeriod);
					}
				} else {
					// Database or Project, can be added
					lockedPeriodsFilteredByActivity.add(lockedPeriod);
				}
			}
			return lockedPeriodsFilteredByActivity;
		} else {
			// No filter, just return the items
			return items;
		}
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
	public HandlerRegistration addCancelCreateHandler(CanCreate.CancelCreateHandler handler) {
		return eventBus.addHandler(CancelCreateEvent.TYPE, handler);
	}

	@Override
	public HandlerRegistration addStartCreateHandler(CanCreate.StartCreateHandler handler) {
		return eventBus.addHandler(StartCreateEvent.TYPE, handler);
	}

	@Override
	public HandlerRegistration addCancelDeleteHandler(CanDelete.CancelDeleteHandler handler) {
		return eventBus.addHandler(CancelDeleteEvent.TYPE, handler);
	}

	@Override
	public HandlerRegistration addRequestUpdateHandler(CanUpdate.RequestUpdateHandler handler) {
		return eventBus.addHandler(RequestUpdateEvent.TYPE, handler);
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

	@Override
	public void cancelUpdateAll() {
		lockedPeriodStore.rejectChanges();
	}
	
	@Override
	public void startCreate() {
		addLockedPeriod.startCreate();
	}

	@Override
	public void cancelCreate() {
		addLockedPeriod.cancelCreate();
	}
	@Override
	public AsyncMonitor getLoadingMonitor() {
		return loadingMonitor;
	}

	@Override
	public AsyncMonitor getCreatingMonitor() {
		return creatingMonitor;
	}

	@Override
	public AsyncMonitor getUpdatingMonitor() {
		return updatingMonitor;
	}

	@Override
	public AsyncMonitor getDeletingMonitor() {
		return deletingMonitor;
	}
	
	@Override
	public void filter(Filter<LockedPeriodDTO> filter) {
		// TODO Auto-generated method stub
	}
	
	@Override
	public void setMustConfirmDelete(boolean mustConfirmDelete) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setRefreshEnabled(boolean canRefresh) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void removeFilter() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void startUpdate() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void cancelDelete() {
		// TODO Auto-generated method stub
		
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
	public void initialize() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setValue(LockedPeriodDTO value) {
		//gridLockedPeriods.getSelectionModel().select(value, false);
	}

	public void setActivityFilter(ActivityDTO activityFilter) {
		this.activityFilter = activityFilter;
	}

	public ActivityDTO getActivityFilter() {
		return activityFilter;
	}

	public void setReadOnly(boolean isReadOnly) {
		if (isReadOnly) {
			remove(toolbarActions);
		}
	}

	public void setTitle(String title) {
		setHeading(title);
	}

}

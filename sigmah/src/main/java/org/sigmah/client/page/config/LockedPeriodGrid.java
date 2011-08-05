package org.sigmah.client.page.config;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.hibernate.event.DeleteEvent;
import org.sigmah.client.dispatch.AsyncMonitor;
import org.sigmah.client.i18n.I18N;
import org.sigmah.client.mvp.Filter;
import org.sigmah.server.endpoint.gwtrpc.handler.DeleteHandler;
import org.sigmah.shared.dto.LockedPeriodDTO;
import org.sigmah.shared.dto.UserDatabaseDTO;

import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SelectionEvent;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
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

public class LockedPeriodGrid extends LayoutContainer implements LockedPeriodListEditor {
	private EventBus eventBus = new SimpleEventBus();
	
	private ListStore<LockedPeriodDTO> lockedPeriodStore;
	private boolean isCreateEnabled = true;
	private boolean isUpdateEnabled = true;
	private boolean isDeleteEnabled = true;
	private EditorGrid<LockedPeriodDTO> gridLockedPeriods;

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
			}
		});
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
		return columnStartDate;
	}

	private void initializeComponent() {
		lockedPeriodStore = new ListStore<LockedPeriodDTO>();
	}

	@Override
	public void create(LockedPeriodDTO item) {
		lockedPeriodStore.add(item);
	}

	@Override
	public void update(LockedPeriodDTO item) {
		// TODO: implement
	}

	@Override
	public void delete(LockedPeriodDTO item) {
		lockedPeriodStore.remove(item);
	}

	@Override
	public void setCreateEnabled(boolean createEnabled) {
		this.isCreateEnabled = createEnabled;
	}

	@Override
	public void setUpdateEnabled(boolean updateEnabled) {
		this.isUpdateEnabled = updateEnabled;
	}

	@Override
	public void setDeleteEnabled(boolean deleteEnabled) {
		this.isDeleteEnabled = deleteEnabled;
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
		// TODO Auto-generated method stub
		
	}

	@Override
	public LockedPeriodDTO getValue() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setPeriods(Set<LockedPeriodDTO> lockedPeriods) {
		lockedPeriodStore.removeAll();
		lockedPeriodStore.add(new ArrayList<LockedPeriodDTO>(lockedPeriods));
	}
	
	@Override
	public HandlerRegistration addCreateHandler(
			org.sigmah.client.mvp.CrudView.CreateHandler handler) {
		return eventBus.addHandler(CreateEvent.TYPE, handler);
	}

	@Override
	public HandlerRegistration addUpdateHandler(
			org.sigmah.client.mvp.CrudView.UpdateHandler handler) {
		return eventBus.addHandler(UpdateEvent.TYPE, handler);
	}

	@Override
	public HandlerRegistration addRequestDeleteHandler(
			org.sigmah.client.mvp.CrudView.DeleteHandler handler) {
		return eventBus.addHandler(DeleteEvent.TYPE, handler);
	}

	@Override
	public HandlerRegistration addCancelUpdateHandler(
			org.sigmah.client.mvp.CrudView.UpdateHandler handler) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public HandlerRegistration addRequestDeleteHandler(DeleteHandler handler) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public HandlerRegistration addConfirmDeleteHandler(
			org.sigmah.client.mvp.CrudView.ConfirmDeleteHandler handler) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public HandlerRegistration addRefreshHandler(
			org.sigmah.client.mvp.CrudView.RefreshHandler handler) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public HandlerRegistration addFilterHandler(
			org.sigmah.client.mvp.CrudView.FilterHandler filter) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void cancelUpdate(LockedPeriodDTO item) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void filter(Filter<LockedPeriodDTO> filter) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void askConfirmDelete(LockedPeriodDTO item) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setParent(UserDatabaseDTO parent) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setCanCancelUpdate(boolean canCancelUpdate) {
		// TODO Auto-generated method stub
		
	}



}

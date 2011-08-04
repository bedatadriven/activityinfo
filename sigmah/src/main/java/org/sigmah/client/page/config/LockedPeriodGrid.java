package org.sigmah.client.page.config;

import java.util.Set;

import org.sigmah.client.dispatch.AsyncMonitor;
import org.sigmah.shared.domain.LockedPeriod;
import org.sigmah.shared.dto.LockedPeriodDTO;

import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.event.shared.SimpleEventBus;

public class LockedPeriodGrid extends LayoutContainer implements LockedPeriodListEditor {
	private EventBus eventBus = new SimpleEventBus();
	private ListStore<LockedPeriodDTO> lockedPeriodStore = new ListStore<LockedPeriodDTO>();
	private boolean isCreateEnabled = true;
	private boolean isUpdateEnabled = true;
	private boolean isDeleteEnabled = true;
	private Grid<LockedPeriodDTO> gridLockedPeriods;

	public LockedPeriodGrid() {
		super();
		
		initializeComponent();
		
		createGrid();
	}

	private void createGrid() {
		
	}

	private void initializeComponent() {
		// TODO Auto-generated method stub
		
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
	public void setPeriods(Set<LockedPeriod> lockedPeriods) {
		// TODO Auto-generated method stub
		
	}
	
//	@Override
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
	public HandlerRegistration addDeleteHandler(
			org.sigmah.client.mvp.CrudView.DeleteHandler handler) {
		return eventBus.addHandler(DeleteEvent.TYPE, handler);
	}



}

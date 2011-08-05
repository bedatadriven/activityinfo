package org.sigmah.client.page.config;

import java.util.ArrayList;
import java.util.List;

import org.sigmah.client.dispatch.Dispatcher;
import org.sigmah.client.mvp.CanCreate.CreateEvent;
import org.sigmah.client.mvp.CanDelete.ConfirmDeleteEvent;
import org.sigmah.client.mvp.CanDelete.RequestDeleteEvent;
import org.sigmah.client.mvp.CanFilter.FilterEvent;
import org.sigmah.client.mvp.CanRefresh.RefreshEvent;
import org.sigmah.client.mvp.CanUpdate.CancelUpdateEvent;
import org.sigmah.client.mvp.CanUpdate.UpdateEvent;
import org.sigmah.client.mvp.CrudView;
import org.sigmah.client.mvp.ListPresenterBase;
import org.sigmah.shared.command.LockActivity;
import org.sigmah.shared.command.RemoveActivityLock;
import org.sigmah.shared.command.UpdateDatabaseLockedPeriod;
import org.sigmah.shared.command.result.VoidResult;
import org.sigmah.shared.dto.LockedPeriodDTO;
import org.sigmah.shared.dto.UserDatabaseDTO;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;

public class LockedPeriodsPresenter 
	extends 
		ListPresenterBase<LockedPeriodDTO, 
			List<LockedPeriodDTO>, 
			UserDatabaseDTO,  
			CrudView<LockedPeriodDTO, UserDatabaseDTO>> {

	@Inject
	public LockedPeriodsPresenter(Dispatcher service, EventBus eventBus,
			LockedPeriodListEditor view, LockedPeriodDTO model, UserDatabaseDTO database) {
		super(service, eventBus, view, model, database);
		
		getData();
	}

	private void getData() {
		view.setItems(new ArrayList<LockedPeriodDTO>(parentModel.getLockedPeriods()));
	}

	@Override
	public void onCreate(CreateEvent event) {
		final LockedPeriodDTO lockedPeriod = null;
		
		service.execute(new LockActivity(model.getId(), lockedPeriod), null, new AsyncCallback<VoidResult>() {
			@Override
			public void onFailure(Throwable caught) {
				// TODO Handle failure
			}

			@Override
			public void onSuccess(VoidResult result) {
				view.create(lockedPeriod);
				parentModel.getLockedPeriods().add(lockedPeriod);
			}
		});
	}

	@Override
	public void onUpdate(UpdateEvent event) {
		final LockedPeriodDTO lockedPeriod = view.getValue();
		
		service.execute(new UpdateDatabaseLockedPeriod(model.getId(), lockedPeriod), null, new AsyncCallback<VoidResult>() {

			@Override
			public void onFailure(Throwable caught) {
				// TODO Handle failure
			}

			@Override
			public void onSuccess(VoidResult result) {
				view.update(lockedPeriod);
				
				// Simplu use the hammer: remove the old one, add the updated one
				parentModel.getLockedPeriods().remove(lockedPeriod);
				parentModel.getLockedPeriods().add(lockedPeriod);
			}
		});
	}

	@Override
	public void onCancelUpdate(CancelUpdateEvent updateEvent) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onConfirmDelete(ConfirmDeleteEvent deleteEvent) {
		final LockedPeriodDTO lockedPeriod = view.getValue();
		int lockedPeriodId = 0;
		service.execute(new RemoveActivityLock(model.getId(), lockedPeriodId), null, new AsyncCallback<VoidResult>() {

			@Override
			public void onFailure(Throwable caught) {
				// TODO Handle failure
			}

			@Override
			public void onSuccess(VoidResult result) {
				view.delete(lockedPeriod);
			}
		});
	}

	@Override
	public void onFilter(FilterEvent filterEvent) {
	}

	@Override
	public void onRefresh(RefreshEvent refreshEvent) {
		view.setItems(new ArrayList<LockedPeriodDTO>(parentModel.getLockedPeriods()));
	}

	@Override
	public void onRequestDelete(RequestDeleteEvent deleteEvent) {
		view.askConfirmDelete(view.getValue());
	}
	
}
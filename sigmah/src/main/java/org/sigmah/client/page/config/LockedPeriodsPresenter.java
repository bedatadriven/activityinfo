package org.sigmah.client.page.config;

import org.sigmah.client.dispatch.Dispatcher;
import org.sigmah.client.mvp.CrudView.CreateEvent;
import org.sigmah.client.mvp.CrudView.DeleteEvent;
import org.sigmah.client.mvp.CrudView.UpdateEvent;
import org.sigmah.client.mvp.ListPresenterBase;
import org.sigmah.shared.command.LockActivity;
import org.sigmah.shared.command.RemoveActivityLock;
import org.sigmah.shared.command.UpdateDatabaseLockedPeriod;
import org.sigmah.shared.command.result.VoidResult;
import org.sigmah.shared.dto.LockedPeriodDTO;
import org.sigmah.shared.dto.UserDatabaseDTO;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class LockedPeriodsPresenter extends ListPresenterBase<LockedPeriodView, UserDatabaseDTO> {

	public LockedPeriodsPresenter(Dispatcher service, EventBus eventBus,
			LockedPeriodView view, UserDatabaseDTO model) {
		super(service, eventBus, view, model);
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
				view.addLockedPeriod(lockedPeriod);
				model.getLockedPeriods().add(lockedPeriod);
			}
		});
	}

	@Override
	public void onUpdate(UpdateEvent event) {
		final LockedPeriodDTO lockedPeriod = view.getCurrentLockedPeriod();
		
		service.execute(new UpdateDatabaseLockedPeriod(model.getId(), lockedPeriod), null, new AsyncCallback<VoidResult>() {

			@Override
			public void onFailure(Throwable caught) {
				// TODO Handle failure
			}

			@Override
			public void onSuccess(VoidResult result) {
				view.updateLockedPeriod(lockedPeriod);
				model.getLockedPeriods().remove(lockedPeriod);
				model.getLockedPeriods().add(lockedPeriod);
			}
		});
	}

	@Override
	public void onRemove(DeleteEvent event) {
		final LockedPeriodDTO lockedPeriod=null;
		int lockedPeriodId = 0;
		service.execute(new RemoveActivityLock(model.getId(), lockedPeriodId), null, new AsyncCallback<VoidResult>() {

			@Override
			public void onFailure(Throwable caught) {
				// TODO Handle failure
			}

			@Override
			public void onSuccess(VoidResult result) {
				view.removeLockedPeriod(lockedPeriod);
			}
		});
	}
	
}
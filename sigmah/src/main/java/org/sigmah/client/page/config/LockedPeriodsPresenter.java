package org.sigmah.client.page.config;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.sigmah.client.EventBus;
import org.sigmah.client.dispatch.Dispatcher;
import org.sigmah.client.mvp.CanCreate;
import org.sigmah.client.mvp.CanCreate.CreateEvent;
import org.sigmah.client.mvp.CanDelete.ConfirmDeleteEvent;
import org.sigmah.client.mvp.CanDelete.RequestDeleteEvent;
import org.sigmah.client.mvp.CanFilter.FilterEvent;
import org.sigmah.client.mvp.CanRefresh.RefreshEvent;
import org.sigmah.client.mvp.CanUpdate.CancelUpdateEvent;
import org.sigmah.client.mvp.CanUpdate.UpdateEvent;
import org.sigmah.client.mvp.CrudView;
import org.sigmah.client.mvp.ListPresenterBase;
import org.sigmah.client.mvp.View;
import org.sigmah.client.page.NavigationCallback;
import org.sigmah.client.page.Page;
import org.sigmah.client.page.PageId;
import org.sigmah.client.page.PageState;
import org.sigmah.client.page.config.LockedPeriodsPresenter.LockedPeriodListEditor;
import org.sigmah.shared.command.BatchCommand;
import org.sigmah.shared.command.Command;
import org.sigmah.shared.command.Delete;
import org.sigmah.shared.command.LockEntity;
import org.sigmah.shared.command.UpdateEntity;
import org.sigmah.shared.command.UpdateLockedPeriod;
import org.sigmah.shared.command.result.BatchResult;
import org.sigmah.shared.command.result.CreateResult;
import org.sigmah.shared.command.result.VoidResult;
import org.sigmah.shared.dto.LockedPeriodDTO;
import org.sigmah.shared.dto.UserDatabaseDTO;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.ImplementedBy;
import com.google.inject.Inject;

public class LockedPeriodsPresenter 
	extends 
		ListPresenterBase<LockedPeriodDTO, 
			List<LockedPeriodDTO>, 
			UserDatabaseDTO,  
			LockedPeriodListEditor>
	implements 
		Page {
	
	@ImplementedBy(LockedPeriodGrid.class)
	public interface LockedPeriodListEditor extends
		CrudView<LockedPeriodDTO, UserDatabaseDTO> {
		List<LockedPeriodDTO> getUnsavedItems();

		boolean hasChangedItems();

		boolean hasSingleChangedItem();
		Map<String, Object> getChanges(LockedPeriodDTO item);
	}	
	
	@ImplementedBy(AddLockedPeriodDialog.class)
	public interface AddLockedPeriodView 
		extends 
			View<LockedPeriodDTO>,
			CanCreate<LockedPeriodDTO> {

		void startCreate();
		void stopCreate();
	}
	
	public static PageId LockedPeriod = new PageId("lockedPeriod");
	
	@Inject
	public LockedPeriodsPresenter(Dispatcher service, EventBus eventBus,
			LockedPeriodListEditor view) {
		super(service, eventBus, view);
		
//		getData();
		
//		view.setCreateEnabled(true);
//		view.setDeleteEnabled(true);
//		view.setUpdateEnabled(true);
	}

	private void getData() {
//		view.setItems(new ArrayList<LockedPeriodDTO>(parentModel.getLockedPeriods()));
	}

	@Override
	public void onCreate(CreateEvent event) {
		final LockedPeriodDTO lockedPeriod = view.getValue();
		LockEntity lockUserDatabase = new LockEntity(lockedPeriod);
		lockUserDatabase.setUserDatabaseId(parentModel.getId());
		
		service.execute(lockUserDatabase, null, new AsyncCallback<CreateResult>() {
			@Override
			public void onFailure(Throwable caught) {
				System.out.println("oh noes");
			}

			@Override
			public void onSuccess(CreateResult result) {
				lockedPeriod.setId(result.getNewId());
				view.create(lockedPeriod);
				parentModel.getLockedPeriods().add(lockedPeriod);
			}
		});
	}

	@Override
	public void onUpdate(UpdateEvent event) {
		List<LockedPeriodDTO> changedItems = view.getUnsavedItems();
		if (view.hasChangedItems()) {
			Command updateCommand = null;
			
			if (view.hasSingleChangedItem()) {
				final LockedPeriodDTO lockedPeriod = view.getUnsavedItems().get(0);
				UpdateEntity updateSingle = new UpdateEntity(lockedPeriod.getEntityName(), lockedPeriod.getId(), view.getChanges(lockedPeriod));
				
				service.execute(updateSingle, null, new AsyncCallback<VoidResult>() {
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
				
			} else {
				BatchCommand batch = new BatchCommand();
				for (LockedPeriodDTO lockedPeriod : view.getUnsavedItems()) {
					batch.add(new UpdateEntity(
							lockedPeriod.getEntityName(), 
							lockedPeriod.getId(), 
							view.getChanges(lockedPeriod)));
				}
				
				service.execute(batch, null, new AsyncCallback<BatchResult>() {
					@Override
					public void onFailure(Throwable caught) {
						// TODO Handle failure
					}

					@Override
					public void onSuccess(BatchResult result) {
						// Simply use the hammer: remove the old one, add the updated one
						for (LockedPeriodDTO lockedPeriod : view.getUnsavedItems()) {
							for (LockedPeriodDTO oldLockedPeriod : parentModel.getLockedPeriods()) {
								if (lockedPeriod.getId() == oldLockedPeriod.getId()) {
									parentModel.getLockedPeriods().remove(oldLockedPeriod);
									parentModel.getLockedPeriods().add(lockedPeriod);
									continue;
								}
							}
						}

						view.update(null);
					}
				});
			}
		}
	}

	private UpdateLockedPeriod createUpdateCommand(LockedPeriodDTO lockedPeriod) {
		UpdateLockedPeriod update = new UpdateLockedPeriod(lockedPeriod);
		update.setUserDatabaseId(parentModel.getId());
		
		return update;
	}

	@Override
	public void onCancelUpdate(CancelUpdateEvent updateEvent) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onConfirmDelete(ConfirmDeleteEvent deleteEvent) {
		final LockedPeriodDTO lockedPeriod = view.getValue();
		service.execute(new Delete(lockedPeriod), null, new AsyncCallback<VoidResult>() {

			@Override
			public void onFailure(Throwable caught) {
				// TODO Handle failure
			}

			@Override
			public void onSuccess(VoidResult result) {
				view.delete(lockedPeriod);
				parentModel.getLockedPeriods().remove(lockedPeriod);
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

	public void go(UserDatabaseDTO db) {
		parentModel = db;
		
		ArrayList<LockedPeriodDTO> items = new ArrayList<LockedPeriodDTO>(db.getLockedPeriods()); 
		view.setItems(items);
		if (items.size() > 0) {
			view.setValue(items.get(0));
		}
		view.setParent(parentModel);
	}

	@Override
	public void shutdown() {
	}

	@Override
	public PageId getPageId() {
		return LockedPeriod;
	}

	@Override
	public Object getWidget() {
		return view;
	}

	@Override
	public void requestToNavigateAway(PageState place,
			NavigationCallback callback) {
		callback.onDecided(true);
	}

	@Override
	public String beforeWindowCloses() {
		return null;
	}

	@Override
	public boolean navigate(PageState place) {
		return false;
	}
	
}
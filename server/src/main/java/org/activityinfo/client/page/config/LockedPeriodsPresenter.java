package org.activityinfo.client.page.config;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.activityinfo.client.EventBus;
import org.activityinfo.client.dispatch.Dispatcher;
import org.activityinfo.client.i18n.I18N;
import org.activityinfo.client.mvp.AddCreateView;
import org.activityinfo.client.mvp.CrudView;
import org.activityinfo.client.mvp.ListPresenterBase;
import org.activityinfo.client.mvp.CanCreate.CreateEvent;
import org.activityinfo.client.mvp.CanDelete.ConfirmDeleteEvent;
import org.activityinfo.client.mvp.CanDelete.RequestDeleteEvent;
import org.activityinfo.client.mvp.CanFilter.FilterEvent;
import org.activityinfo.client.mvp.CanRefresh.RefreshEvent;
import org.activityinfo.client.mvp.CanUpdate.CancelUpdateEvent;
import org.activityinfo.client.mvp.CanUpdate.UpdateEvent;
import org.activityinfo.client.page.NavigationCallback;
import org.activityinfo.client.page.Page;
import org.activityinfo.client.page.PageId;
import org.activityinfo.client.page.PageState;
import org.activityinfo.client.page.config.LockedPeriodsPresenter.LockedPeriodListEditor;
import org.activityinfo.shared.command.BatchCommand;
import org.activityinfo.shared.command.Delete;
import org.activityinfo.shared.command.GetSchema;
import org.activityinfo.shared.command.LockEntity;
import org.activityinfo.shared.command.UpdateEntity;
import org.activityinfo.shared.command.result.BatchResult;
import org.activityinfo.shared.command.result.CreateResult;
import org.activityinfo.shared.command.result.VoidResult;
import org.activityinfo.shared.dto.ActivityDTO;
import org.activityinfo.shared.dto.LockedPeriodDTO;
import org.activityinfo.shared.dto.ProjectDTO;
import org.activityinfo.shared.dto.SchemaDTO;
import org.activityinfo.shared.dto.UserDatabaseDTO;

import com.extjs.gxt.ui.client.widget.MessageBox;
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
		DbPage {
	
	@ImplementedBy(LockedPeriodGrid.class)
	public interface LockedPeriodListEditor
		extends
			CrudView<LockedPeriodDTO, UserDatabaseDTO> {
		
		void setTitle(String title);
	}	
	
	@ImplementedBy(AddLockedPeriodDialog.class)
	public interface AddLockedPeriodView
		extends 
			AddCreateView<LockedPeriodDTO> {

		void setUserDatabase(UserDatabaseDTO userDatabase);
	}
	
	public static final PageId PAGE_ID = new PageId("lockedPeriod");
	
	@Inject
	public LockedPeriodsPresenter(Dispatcher service, EventBus eventBus,
			LockedPeriodListEditor view) {
		super(service, eventBus, view);
		
	}

	@Override
	public void onCreate(CreateEvent event) {
		view.getCreatingMonitor().beforeRequest();
		
		final LockedPeriodDTO lockedPeriod = view.getValue();
		LockEntity lockUserDatabase = new LockEntity(lockedPeriod);
		if (lockedPeriod.getParent() instanceof ActivityDTO) {
			lockUserDatabase.setActivityId(lockedPeriod.getParent().getId());
		}
		if (lockedPeriod.getParent() instanceof ProjectDTO) {
			lockUserDatabase.setProjectId(lockedPeriod.getParent().getId());		
		}
		if (lockedPeriod.getParent() instanceof UserDatabaseDTO) {
			lockUserDatabase.setUserDatabaseId(lockedPeriod.getParent().getId());		
		}
		
		service.execute(lockUserDatabase, new AsyncCallback<CreateResult>() {
			@Override
			public void onFailure(Throwable caught) {
				view.getCreatingMonitor().onServerError();
				MessageBox.alert(I18N.CONSTANTS.error(), I18N.CONSTANTS.errorOnServer() + "\n\n" +  caught.getMessage(), null);
			}

			@Override
			public void onSuccess(CreateResult result) {
				// Update the Id for the child instance
				lockedPeriod.setId(result.getNewId());
				
				// Tell the view there's a new kid on the block
				view.create(lockedPeriod);
				
				// Actually add the lock to it's parent
				lockedPeriod.getParent().getLockedPeriods().add(lockedPeriod);
			}
		});
	}

	@Override
	public void onUpdate(UpdateEvent event) {
		if (view.hasChangedItems()) {
			// Tell the user we're about to persist his changes
			view.getUpdatingMonitor().beforeRequest();
			
			service.execute(createBatchCommand(), new AsyncCallback<BatchResult>() {
				@Override
				public void onFailure(Throwable caught) {
					// Tell the user an error occurred
					view.getDeletingMonitor().onServerError();
					// TODO Handle failure
					MessageBox.alert(I18N.CONSTANTS.error(), I18N.CONSTANTS.errorOnServer() + "\n\n" +  caught.getMessage(), null);
				}

				@Override
				public void onSuccess(BatchResult result) {
					// Tell the user we're done updating
					view.getDeletingMonitor().onCompleted();
					
					// Update the in-memory model
					updateParent();
					
					// Update the view
					view.update(null);
				}

				/** Replace changed locks */
				private void updateParent() {
					for (LockedPeriodDTO lockedPeriod : view.getUnsavedItems()) {
						LockedPeriodDTO lockedPeriodToRemove = null;
						
						// Cache the LockedPeriods candidate for removal
						Set<LockedPeriodDTO> lockedPeriodsToUpdate = parentModel.getLockedPeriods();
						
						// Find the LockedPeriod in the model
						for (LockedPeriodDTO oldLockedPeriod : lockedPeriodsToUpdate) {
							if (lockedPeriod.getId() == oldLockedPeriod.getId()) {
								lockedPeriodToRemove = oldLockedPeriod;
								break;
							}
						}
						
						// Replace LockedPeriod when the same entity is found
						if (lockedPeriodToRemove != null) {
							// Remove from cache
							lockedPeriodsToUpdate.remove(lockedPeriodToRemove);
							
							// Replace old instance with new instance
							parentModel.getLockedPeriods().remove(lockedPeriodToRemove);
							parentModel.getLockedPeriods().add(lockedPeriod);
						}
					}
				}
			});
		}
	}

	private BatchCommand createBatchCommand() {
		BatchCommand batch = new BatchCommand();
		for (LockedPeriodDTO lockedPeriod : view.getUnsavedItems()) {
			batch.add(new UpdateEntity(
					lockedPeriod.getEntityName(), 
					lockedPeriod.getId(), 
					view.getChanges(lockedPeriod)));
		}
		return batch;
	}

	@Override
	public void onCancelUpdate(CancelUpdateEvent updateEvent) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onConfirmDelete(ConfirmDeleteEvent deleteEvent) {
		final LockedPeriodDTO lockedPeriod = view.getValue();
		service.execute(new Delete(lockedPeriod), new AsyncCallback<VoidResult>() {

			@Override
			public void onFailure(Throwable caught) {
				MessageBox.alert(I18N.CONSTANTS.error(), I18N.CONSTANTS.errorOnServer() + "\n\n" +  caught.getMessage(), null);
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
		service.execute(new GetSchema(), new AsyncCallback<SchemaDTO>() {
			@Override
			public void onFailure(Throwable caught) {
				// TODO: handle failure
			}

			@Override
			public void onSuccess(SchemaDTO result) {
				go(result.getDatabaseById(parentModel.getId()));
			}
		});
	}

	@Override
	public void onRequestDelete(RequestDeleteEvent deleteEvent) {
		view.askConfirmDelete(view.getValue());
	}

	@Override
	public void go(UserDatabaseDTO db) {
		parentModel = db;
		
		ArrayList<LockedPeriodDTO> items = new ArrayList<LockedPeriodDTO>(db.getLockedPeriods());
		for (ActivityDTO activity : db.getActivities()) {
			if (activity.getLockedPeriods() != null && activity.getLockedPeriods().size() > 0) {
				items.addAll(activity.getLockedPeriods());
			}
		}
		for (ProjectDTO project : db.getProjects()) {
			if (project.getLockedPeriods() != null && project.getLockedPeriods().size() > 0) {
				items.addAll(project.getLockedPeriods());
			}
		}
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
		return PAGE_ID;
	}

	@Override
	public Object getWidget() {
		return view;
	}

	@Override
	public void requestToNavigateAway(PageState place, NavigationCallback callback) {
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
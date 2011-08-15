package org.sigmah.client.mvp;

import java.util.List;

import org.sigmah.client.EventBus;
import org.sigmah.client.dispatch.Dispatcher;
import org.sigmah.client.mvp.CanCreate.CancelCreateEvent;
import org.sigmah.client.mvp.CanCreate.CancelCreateHandler;
import org.sigmah.client.mvp.CanCreate.CreateEvent;
import org.sigmah.client.mvp.CanCreate.CreateHandler;
import org.sigmah.client.mvp.CanCreate.StartCreateEvent;
import org.sigmah.client.mvp.CanCreate.StartCreateHandler;
import org.sigmah.client.mvp.CanDelete.CancelDeleteEvent;
import org.sigmah.client.mvp.CanDelete.CancelDeleteHandler;
import org.sigmah.client.mvp.CanDelete.ConfirmDeleteEvent;
import org.sigmah.client.mvp.CanDelete.ConfirmDeleteHandler;
import org.sigmah.client.mvp.CanDelete.RequestDeleteEvent;
import org.sigmah.client.mvp.CanDelete.RequestDeleteHandler;
import org.sigmah.client.mvp.CanFilter.FilterEvent;
import org.sigmah.client.mvp.CanFilter.FilterHandler;
import org.sigmah.client.mvp.CanRefresh.RefreshEvent;
import org.sigmah.client.mvp.CanRefresh.RefreshHandler;
import org.sigmah.client.mvp.CanUpdate.CancelUpdateEvent;
import org.sigmah.client.mvp.CanUpdate.CancelUpdateHandler;
import org.sigmah.client.mvp.CanUpdate.RequestUpdateEvent;
import org.sigmah.client.mvp.CanUpdate.RequestUpdateHandler;
import org.sigmah.client.mvp.CanUpdate.UpdateEvent;
import org.sigmah.client.mvp.CanUpdate.UpdateHandler;
import org.sigmah.shared.command.CreateEntity;
import org.sigmah.shared.command.Delete;
import org.sigmah.shared.command.GetCommand;
import org.sigmah.shared.command.UpdateCommand;
import org.sigmah.shared.dto.DTO;

import com.google.gwt.user.client.rpc.AsyncCallback;

/*
 * Default implementation for a presenter handling CRUD for simple entities 
 */
public class DefaultPresenter<M extends DTO, L extends List<M>, ParentP extends DTO, V extends CrudView<M, ParentP>>
	extends 
		PresenterBase<V, M> 
	implements 
		UpdateHandler,
		CancelUpdateHandler, 
		ConfirmDeleteHandler, 
		CreateHandler,
		FilterHandler, 
		RefreshHandler, 
		RequestDeleteHandler, 
		CancelCreateHandler, 
		StartCreateHandler, 
		RequestUpdateHandler, 
		CancelDeleteHandler 
	{

	// Obligatory
	protected Dispatcher service;
	protected EventBus eventBus;
	
	// Base types
	protected M modelPrototype;
	protected ParentP parentModel;
	protected V view;
	
	// Commands
	protected CreateEntity createCommand;
	protected UpdateCommand<M> updateCommand;
	protected GetCommand<M> getCommand;
	protected Delete deleteCommand;
	
	public DefaultPresenter(Dispatcher service, EventBus eventBus, V view) {
		super(service, eventBus, view);
		
		addListeners();
		initializeCommands();
	}
	
	private void initializeCommands() {
		//createCommand = modelPrototype.createCommand();
	}

	@Override
	protected void addListeners() {
		// Create
		view.addStartCreateHandler(this);
		view.addCancelCreateHandler(this);
		view.addCreateHandler(this);

		// Update
		view.addRequestUpdateHandler(this);
		view.addCancelUpdateHandler(this);
		view.addUpdateHandler(this);

		// Delete
		view.addRequestDeleteHandler(this);
		view.addCancelDeleteHandler(this);
		view.addConfirmDeleteHandler(this);

		view.addFilterHandler(this);
		view.addRefreshHandler(this);
	}

	@Override
	public void onRequestDelete(RequestDeleteEvent deleteEvent) {
		view.askConfirmDelete(null);
	}

	@Override
	public void onStartCreate(StartCreateEvent createEvent) {
		view.startCreate();
	}
	
	@Override
	public void onRequestUpdate(RequestUpdateEvent requestUpdateEvent) {
		view.startUpdate();
	}
	
	@Override
	public void onRefresh(RefreshEvent refreshEvent) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onFilter(FilterEvent filterEvent) {
		
	}

	@Override
	public void onCreate(CreateEvent createEvent) {
		service.execute(createCommand, null, new AsyncCallback() {
			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onSuccess(Object result) {
				
			}
		});
	}

	@Override
	public void onConfirmDelete(ConfirmDeleteEvent deleteEvent) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onUpdate(UpdateEvent updateEvent) {
		
	}


	@Override
	public void onCancelCreate(CancelCreateEvent createEvent) {
		view.cancelCreate();
	}
	
	@Override
	public void onCancelUpdate(CancelUpdateEvent updateEvent) {
		view.cancelUpdateAll();
	}

	@Override
	public void onCancelDelete(CancelDeleteEvent cancelDeleteEvent) {
		view.cancelDelete();
	}

}

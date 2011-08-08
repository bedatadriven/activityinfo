package org.sigmah.client.mvp;

import java.util.List;

import org.sigmah.client.EventBus;
import org.sigmah.client.dispatch.Dispatcher;
import org.sigmah.client.mvp.CanCreate.CreateEvent;
import org.sigmah.client.mvp.CanCreate.CreateHandler;
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
import org.sigmah.client.mvp.CanUpdate.UpdateEvent;
import org.sigmah.client.mvp.CanUpdate.UpdateHandler;
import org.sigmah.shared.dto.DTO;

public class ListPresenterBase<M extends DTO, L extends List<M>, P extends DTO, V extends CrudView<M, P>> 
	extends 
		PresenterBase<V, M>
	implements 
		UpdateHandler,
		CancelUpdateHandler,
		ConfirmDeleteHandler,
		CreateHandler,
		FilterHandler,
		RefreshHandler,
		RequestDeleteHandler
	{
	
	protected P parentModel;

	public ListPresenterBase(Dispatcher service, EventBus eventBus, V view) {
		super(service, eventBus, view);
	}

	@Override
	protected void addListeners() {
		// Create
		view.addCreateHandler(this);

		// Update
		view.addCancelUpdateHandler(this);
		view.addUpdateHandler(this);

		// Delete
		view.addRequestDeleteHandler(this);
		view.addConfirmDeleteHandler(this);

		view.addFilterHandler(this);
		view.addRefreshHandler(this);
	}

	@Override
	public void onRequestDelete(RequestDeleteEvent deleteEvent) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onRefresh(RefreshEvent refreshEvent) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onFilter(FilterEvent filterEvent) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onCreate(CreateEvent createEvent) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onConfirmDelete(ConfirmDeleteEvent deleteEvent) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onCancelUpdate(CancelUpdateEvent updateEvent) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onUpdate(UpdateEvent updateEvent) {
		// TODO Auto-generated method stub
		
	}
}

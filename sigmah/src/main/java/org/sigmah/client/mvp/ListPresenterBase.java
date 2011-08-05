package org.sigmah.client.mvp;

import java.util.List;

import org.sigmah.client.dispatch.Dispatcher;
import org.sigmah.client.mvp.CanCreate.CreateHandler;
import org.sigmah.client.mvp.CanDelete.ConfirmDeleteHandler;
import org.sigmah.client.mvp.CanDelete.RequestDeleteHandler;
import org.sigmah.client.mvp.CanFilter.FilterHandler;
import org.sigmah.client.mvp.CanRefresh.RefreshHandler;
import org.sigmah.client.mvp.CanUpdate.CancelUpdateHandler;
import org.sigmah.client.mvp.CanUpdate.UpdateHandler;
import org.sigmah.shared.dto.DTO;

import com.google.gwt.event.shared.EventBus;

public abstract class ListPresenterBase<M extends DTO, L extends List<M>, P extends DTO, V extends CrudView<M, P>> 
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

	public ListPresenterBase(Dispatcher service, EventBus eventBus, V view,
			M model, P parent) {
		super(service, eventBus, view, model);
		this.parentModel = parent;
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
}

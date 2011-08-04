package org.sigmah.client.mvp;

import org.sigmah.client.dispatch.Dispatcher;
import org.sigmah.client.mvp.CrudView.CreateHandler;
import org.sigmah.client.mvp.CrudView.DeleteHandler;
import org.sigmah.client.mvp.CrudView.UpdateHandler;
import org.sigmah.shared.dto.DTO;

import com.google.gwt.event.shared.EventBus;

public abstract class ListPresenterBase<V extends CrudView<M>, M extends DTO> 
	extends PresenterBase<V, M> 
	implements CreateHandler, UpdateHandler, DeleteHandler {

	public ListPresenterBase(Dispatcher service, EventBus eventBus, V view,
			M model) {
		super(service, eventBus, view, model);
	}

	@Override
	protected void addListeners() {
		view.addCreateHandler(this);
		view.addUpdateHandler(this);
		view.addDeleteHandler(this);
	}
}

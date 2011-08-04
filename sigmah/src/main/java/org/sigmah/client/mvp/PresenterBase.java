package org.sigmah.client.mvp;

import org.sigmah.client.dispatch.Dispatcher;
import org.sigmah.shared.dto.DTO;

import com.google.gwt.event.shared.EventBus;

/*
 * Base class to reduce code in presenter classes and to provide a template
 */
public abstract class PresenterBase<V extends View<M>, M extends DTO> implements Presenter<V, M> {
	protected final Dispatcher service;
	protected final EventBus eventBus;
	protected final V view;
	protected final M model;
	
	public PresenterBase(Dispatcher service, EventBus eventBus, V view, M model) {
		this.service = service;
		this.eventBus = eventBus;
		this.view = view;
		this.model = model;
		
		addListeners();
	}
	
	/*
	 * Adds all the relevant listeners from the view to the presenter
	 */
	protected void addListeners() {
		
	}
}	

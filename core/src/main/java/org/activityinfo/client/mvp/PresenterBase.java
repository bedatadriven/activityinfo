package org.activityinfo.client.mvp;

import org.activityinfo.client.EventBus;
import org.activityinfo.client.dispatch.Dispatcher;
import org.activityinfo.shared.dto.DTO;

/*
 * Base class to reduce code in presenter classes and to provide a template
 */
public class PresenterBase<V extends View<M>, M extends DTO> 
	implements 
		Presenter<V, M> 
	{
	
	protected final Dispatcher service;
	protected final EventBus eventBus;
	protected final V view;
	
	public PresenterBase(Dispatcher service, EventBus eventBus, V view) {
		this.service = service;
		this.eventBus = eventBus;
		this.view = view;
		
		addListeners();
	}
	
	/*
	 * Adds all the relevant listeners from the view to the presenter
	 */
	protected void addListeners() {
		
	}
}	

package org.sigmah.client.mvp;

import org.sigmah.client.dispatch.Dispatcher;
import org.sigmah.shared.dto.DTO;

import com.google.gwt.event.shared.EventBus;

public class AbstractPresenter<V extends View<M>, M extends DTO> implements Presenter<V, M> {
	protected Dispatcher service;
	protected EventBus eventBus;
	protected V view;
	protected M model;
	
	public AbstractPresenter(Dispatcher service, EventBus eventBus, V view, M model) {
		this.service = service;
		this.eventBus = eventBus;
		
		addListeners();
	}
	
	protected void addListeners() {
	}
}	

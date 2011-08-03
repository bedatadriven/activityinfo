package org.sigmah.client.page;

import org.sigmah.client.dispatch.Dispatcher;

import com.google.gwt.event.shared.EventBus;

public class AbstractPresenter {
	private Dispatcher service;
	private EventBus eventBus;
	
	public AbstractPresenter(Dispatcher service, EventBus eventBus) {
		this.service = service;
		this.eventBus = eventBus;
		
		addListeners();
	}
	
	protected void addListeners() {
		
	}
}	

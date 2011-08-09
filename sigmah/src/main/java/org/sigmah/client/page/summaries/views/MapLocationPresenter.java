package org.sigmah.client.page.summaries.views;

import org.sigmah.client.dispatch.Dispatcher;
import org.sigmah.client.page.entry.editor.MapView;

import com.google.gwt.event.shared.EventBus;

public class MapLocationPresenter {
	private EventBus eventBus;
	private Dispatcher service;
	private MapView view;
	
	public MapLocationPresenter(EventBus eventBus, Dispatcher service,
			MapView view) {
		this.eventBus = eventBus;
		this.service = service;
		this.view = view;
		
		view.initialize();
	}
	
}
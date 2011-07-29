package org.sigmah.client.page.dashboard;

import org.sigmah.client.EventBus;
import org.sigmah.client.dispatch.Dispatcher;
import org.sigmah.shared.dto.DashboardDTO;

public class DashboardPresenter {
	public interface View {
		public DashboardDTO getDashboard();
	}
	
	private View view;
	private DashboardDTO dashboard;
	private EventBus eventBus;
	private Dispatcher service;

	public DashboardPresenter(EventBus eventBus, Dispatcher service, View view) {
		this.view = view;
		this.eventBus=eventBus;
		this.service=service;
	}
}
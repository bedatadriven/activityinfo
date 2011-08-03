package org.sigmah.client.page.dashboard;

import org.sigmah.client.EventBus;
import org.sigmah.client.dispatch.Dispatcher;
import org.sigmah.client.page.NavigationCallback;
import org.sigmah.client.page.Page;
import org.sigmah.client.page.PageId;
import org.sigmah.client.page.PageState;
import org.sigmah.client.page.dashboard.portlets.PortletPresenter;
import org.sigmah.client.page.dashboard.portlets.PortletPresenterFactory;
import org.sigmah.shared.command.GetDashboard;
import org.sigmah.shared.dto.DashboardSettingsDTO;
import org.sigmah.shared.dto.portlets.PortletDTO;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;

public class DashboardPresenter implements Page {
	public interface View {
		public void addPortlet(PortletPresenter presenter);
	}

	public static final PageId Dashboard = new PageId("Dashboard");
	
	private View view;
	private EventBus eventBus;
	private Dispatcher service;
	private PortletPresenterFactory factory;
	private DashboardSettingsDTO dashboard;

	@Inject
	public DashboardPresenter(EventBus eventBus, Dispatcher service, PortletPresenterFactory factory) {
		this.eventBus = eventBus;
		this.service = service;
		this.factory = factory;
		
		factory = new PortletPresenterFactory(service);
		
		view = new DashboardView();
		
		getDashboard();
	}
	
	private void getDashboard() {
		service.execute(new GetDashboard(), null, new AsyncCallback<DashboardSettingsDTO>(){
			@Override
			public void onFailure(Throwable caught) {
				// TODO Handle failure
			}

			@Override
			public void onSuccess(DashboardSettingsDTO result) {
				dashboard = result;
				initialize();
			}
		});
	}

	private void initialize() {
		for (PortletDTO portlet : dashboard.getPortlets()) {
			PortletPresenter portletPresenter = factory.fromPortlet(portlet);
			view.addPortlet(portletPresenter);
		}
	}

	@Override
	public void shutdown() {
	}

	@Override
	public PageId getPageId() {
		return Dashboard;
	}

	@Override
	public Object getWidget() {
		return view;
	}

	@Override
	public void requestToNavigateAway(PageState place,
			NavigationCallback callback) {
        callback.onDecided(true);
	}

	@Override
	public String beforeWindowCloses() {
		return null;
	}

	@Override
	public boolean navigate(PageState place) {
		return false;
	}
}
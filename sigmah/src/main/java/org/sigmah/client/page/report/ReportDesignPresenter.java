package org.sigmah.client.page.report;

import org.sigmah.client.EventBus;
import org.sigmah.client.dispatch.Dispatcher;
import org.sigmah.client.page.NavigationCallback;
import org.sigmah.client.page.Page;
import org.sigmah.client.page.PageId;
import org.sigmah.client.page.PageState;
import org.sigmah.client.page.common.toolbar.ActionListener;
import org.sigmah.client.util.state.StateProvider;

import com.google.inject.ImplementedBy;
import com.google.inject.Inject;

public class ReportDesignPresenter implements ActionListener, Page {
	public static final PageId PAGE_ID = new PageId("reportdesign");
	
	private final EventBus eventBus;
	private final Dispatcher service;
	private final View view;

	@ImplementedBy(ReportDesignPage.class)
	public interface View{
		public void init();
	}
	
	@Inject
	public ReportDesignPresenter(EventBus eventBus, Dispatcher service,
			StateProvider stateMgr, View view) {
		super();
		this.eventBus = eventBus;
		this.service = service;
		this.view = view;
	}

	@Override
	public void onUIAction(String actionId) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void shutdown() {
		
	}

	@Override
	public PageId getPageId() {
		return PAGE_ID;
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

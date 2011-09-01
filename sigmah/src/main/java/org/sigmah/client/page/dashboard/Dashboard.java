package org.sigmah.client.page.dashboard;

import org.sigmah.client.dispatch.Dispatcher;
import org.sigmah.client.page.NavigationCallback;
import org.sigmah.client.page.Page;
import org.sigmah.client.page.PageId;
import org.sigmah.client.page.PageState;

import com.extjs.gxt.ui.client.Style.Orientation;
import com.extjs.gxt.ui.client.Style.Scroll;
import com.extjs.gxt.ui.client.util.Margins;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.VerticalPanel;
import com.extjs.gxt.ui.client.widget.layout.RowData;
import com.extjs.gxt.ui.client.widget.layout.RowLayout;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;

public class Dashboard extends ContentPanel implements Page {
	public static final PageId DASHBOARD = new PageId("dashboard");
	private int margin=10;
	private Dispatcher service;
	private VerticalPanel mainArea = new VerticalPanel(); 
	private VerticalPanel rightColumn = new VerticalPanel(); 
	private final WelcomeWidget welcomeWidget = new WelcomeWidget();
	
	@Inject
	public Dashboard(Dispatcher service) {
		super();
		setSize("auto", "auto");

		this.service = service;

		initializeComponent();

		createBlogPosts();
		createUpdateStream();

		createSitesWithoutLocations();
		createFavorites();

		welcomeHeader();
		setHeaderVisible(false);
		setScrollMode(Scroll.AUTOY);
		setTopComponent(welcomeWidget);
	}
	
	private void createFavorites() {
		Favorites favorites = new Favorites(service, "Favorites");
		addRight(favorites);
	}

	private void createUpdateStream() {
		UpdateStream updateStream = new UpdateStream(service);
		addToMainArea(updateStream);
	}

	private void addToMainArea(Widget widget) {
		mainArea.setSpacing(10);
		mainArea.add(widget);
	}
	
	private void addRight(Widget widget ) {
		rightColumn.add(widget);
	}
	
	private void welcomeHeader() {
	}

	private void createBlogPosts() {
		NewNews newNews = new NewNews(service);
		addToMainArea(newNews);
	}

	private void createSitesWithoutLocations() {
		SitesWithoutLocations sitesWithoutLocations = new SitesWithoutLocations(service);
		addRight(sitesWithoutLocations);
	}

	private void initializeComponent() {
		setLayout(new RowLayout(Orientation.HORIZONTAL));
		mainArea.setStyleAttribute("margin", "1em");
		rightColumn.setStyleAttribute("margin", "1em");
		add(mainArea, new RowData(.67,-1,new Margins(margin)));
		mainArea.setHeight("");
		add(rightColumn, new RowData(.33,-1,new Margins(margin)));
	}

	@Override
	public void shutdown() {
		// TODO Auto-generated method stub

	}

	@Override
	public PageId getPageId() {
		return Dashboard.DASHBOARD;
	}

	@Override
	public Object getWidget() {
		return this;
	}

	@Override
	public void requestToNavigateAway(PageState place, NavigationCallback callback) {
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

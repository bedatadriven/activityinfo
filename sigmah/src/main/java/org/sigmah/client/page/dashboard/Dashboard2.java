package org.sigmah.client.page.dashboard;

import java.util.List;

import org.sigmah.client.dispatch.Dispatcher;
import org.sigmah.client.icon.IconImageBundle;
import org.sigmah.client.page.NavigationCallback;
import org.sigmah.client.page.Page;
import org.sigmah.client.page.PageId;
import org.sigmah.client.page.PageState;
import org.sigmah.shared.command.GetLocationsWithoutGpsCoordinates;
import org.sigmah.shared.command.result.LocationsWithoutGpsResult;
import org.sigmah.shared.dto.LocationDTO;

import com.extjs.gxt.ui.client.Style.Orientation;
import com.extjs.gxt.ui.client.util.Margins;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.HorizontalPanel;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.form.LabelField;
import com.extjs.gxt.ui.client.widget.layout.RowData;
import com.extjs.gxt.ui.client.widget.layout.RowLayout;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;

public class Dashboard2 extends ContentPanel implements Page{

	private Dispatcher service;
	private int margin = 10;

	@Inject
	public Dashboard2(Dispatcher service) {
		super();
		
		this.service=service;
		
		initializeComponent();
		
		createBlogPosts();
		createSitesWithoutLocations();
		
		layout(true);
		
		LayoutContainer welcomethingie = new LayoutContainer();
		RowLayout rowLayout = new RowLayout();
		rowLayout.setOrientation(Orientation.HORIZONTAL);
		
		welcomethingie.setLayout(rowLayout);
		welcomethingie.setHeight(88);
		LabelField label = new LabelField();
		label.setStyleAttribute("font-size", "20px");
		label.setText("Yey. Welcome to ActivityInfo.");
		RowData rd = new RowData(-1,-1,new Margins(20,20,20,20));
		welcomethingie.add(IconImageBundle.ICONS.logo48().createImage(), rd);
		welcomethingie.add(label, rd);
		setTopComponent(welcomethingie);
		
		setHeaderVisible(false);
	}
	
	private RowData getMainRowData() {
		RowData rd = new RowData();
		rd.setWidth(0.66);
		rd.setMargins(new Margins(margin,margin,margin,margin));
		return rd;
	}
	
	private RowData getSideRowData() {
		RowData rd = new RowData();
		rd.setWidth(0.33);
		rd.setMargins(new Margins(margin,margin,margin,margin));
		return rd;
	}

	private void createBlogPosts() {
		NewNews newNews = new NewNews(service);
		add(newNews, getMainRowData());
	}

	private void createSitesWithoutLocations() {
		SitesWithoutLocations sitesWithoutLocations = new SitesWithoutLocations(service);
		add(sitesWithoutLocations, getSideRowData());
	}

	private void initializeComponent() {
		RowLayout rowLayout = new RowLayout();
		rowLayout.setOrientation(Orientation.HORIZONTAL);
		setLayout(rowLayout);
	}
	
	private class NewNews extends AiPortlet {
		public NewNews(Dispatcher service) {
			super(service);
			
			StringBuilder text = new StringBuilder();
			for (int i=0; i<40; i++) {
				text.append("Hey! Cool new Stuff?!?! \n");
			}
			add(new LabelField("Title of blogpost"));
			add(new LabelField(text.toString()));
		}
	}
	
	private class AiPortlet extends LayoutContainer {
		protected Dispatcher service;

		public AiPortlet(Dispatcher service) {
			super();
			
			this.service=service;
		}
	}

	private class SitesWithoutLocations extends AiPortlet {
		
		public SitesWithoutLocations(Dispatcher service) {
			super(service);
			
			RowLayout layout = new RowLayout();
			layout.setOrientation(Orientation.VERTICAL);
			setLayout(layout);
			setAutoHeight(true);
			
			add(new LabelField("Sites without coordinates"));
			
			getSitesWithoutCoordinates();
		}

		private void getSitesWithoutCoordinates() {
			service.execute(
				new GetLocationsWithoutGpsCoordinates().setMaxLocations(25),
				null,
				new AsyncCallback<LocationsWithoutGpsResult>() {
					@Override
					public void onFailure(Throwable caught) {
						System.out.println();
					}

					@Override
					public void onSuccess(LocationsWithoutGpsResult result) {
						setSites(result.getData());
					}

					private void setSites(List<LocationDTO> data) {
						for (LocationDTO location: data) {
							HorizontalPanel panel = new HorizontalPanel();
							panel.add(IconImageBundle.ICONS.site().createImage());
							panel.add(new LabelField(location.getName()));
							add(panel, new RowData(-1,-1,new Margins(5,5,5,5)));
						}
						layout(true);
						Dashboard2.this.layout(true);
					}
				}
			);
		}
		
	}

	@Override
	public void shutdown() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public PageId getPageId() {
		return DashboardPresenter.Dashboard;
	}

	@Override
	public Object getWidget() {
		return this;
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

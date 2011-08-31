package org.sigmah.client.page.dashboard.portlets;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.sigmah.client.dispatch.Dispatcher;
import org.sigmah.client.i18n.I18N;
import org.sigmah.client.mvp.CanRefresh.RefreshEvent;
import org.sigmah.client.mvp.CanRefresh.RefreshHandler;
import org.sigmah.client.page.common.columns.ReadTextColumn;
import org.sigmah.shared.command.GetLocationsWithoutGpsCoordinates;
import org.sigmah.shared.command.result.LocationsWithoutGpsResult;
import org.sigmah.shared.dto.LocationDTO;
import org.sigmah.shared.dto.portlets.NoGpsLocationsDTO;

import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class NoGpsLocationsPresenter 
	implements 
		PortletPresenter, 
		RefreshHandler {
	
	public interface View extends PortletView<NoGpsLocationsDTO> {
		void setSites(List<LocationDTO> sites);
		void setTotalSitesCount(int sites);
	}

	public class NoGpsLocationsPortletView extends BasePortlet<NoGpsLocationsDTO> implements View {
		private ListStore<LocationDTO> store;

		public NoGpsLocationsPortletView(NoGpsLocationsDTO portlet) {
			super(portlet);

			initializeComponent();
			
			createGrid();
		}

		private void createGrid() {
			store = new ListStore<LocationDTO>();
			
			ColumnConfig[] configs = new ColumnConfig[] {
					new ReadTextColumn("name", I18N.CONSTANTS.name(), 100),
					new ReadTextColumn("axe", I18N.CONSTANTS.axe(), 100)
			};

			Grid<LocationDTO> sites = new Grid<LocationDTO>(store,
					new ColumnModel(Arrays.asList(configs)));

			add(sites);		
		}

		private void initializeComponent() {
			setHeading(I18N.MESSAGES.locationsWithoutGps("0"));
		}

		@Override
		public void setSites(List<LocationDTO> locations) {
			store.removeAll();
			store.add(locations);
			setHeight(2300);
			layout(true);
		}

		@Override
		public void setTotalSitesCount(int sites) {
			setHeading(I18N.MESSAGES.locationsWithoutGps(Integer.toString(sites)));
		}

		@Override
		protected boolean supportsClose() {
			return true;
		}

		@Override
		protected boolean supportsConfig() {
			return true;
		}

		@Override
		protected boolean supportsRefresh() {
			return true;
		}

		@Override
		protected boolean supportsCollapsing() {
			return false;
		}

		@Override
		public void initialize() {
		}
	}

	private Dispatcher service;
	private List<LocationDTO> sitesWithoutGps = new ArrayList<LocationDTO>();
	private View view;
	private NoGpsLocationsDTO model;

	public NoGpsLocationsPresenter(Dispatcher service, NoGpsLocationsDTO model) {
		this.service = service;
		this.model = model;

		initializeView();
		
		getSitesWithoutGps();
	}

	private void initializeView() {
		view = new NoGpsLocationsPortletView(model);
		view.addRefreshHandler(this);
	}

	private void getSitesWithoutGps() {
		service.execute(new GetLocationsWithoutGpsCoordinates()
						.setMaxLocations(25), 
						view.loadingMonitor(),
			new AsyncCallback<LocationsWithoutGpsResult>() {
				@Override
				public void onFailure(Throwable caught) {
					System.out.println();
				}
				@Override
				public void onSuccess(LocationsWithoutGpsResult result) {
					view.setSites(result.getData());
					view.setTotalSitesCount(result.getTotalLocationsCount());
				}
			}
		);
	}

	@Override
	public PortletView getView() {
		return view;
	}

	@Override
	public void onRefresh(RefreshEvent refreshEvent) {
		getSitesWithoutGps();
	}
}
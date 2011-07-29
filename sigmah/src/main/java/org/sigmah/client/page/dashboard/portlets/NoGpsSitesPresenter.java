package org.sigmah.client.page.dashboard.portlets;

import java.util.ArrayList;
import java.util.List;

import org.sigmah.client.dispatch.Dispatcher;
import org.sigmah.shared.command.GetLocationsWithoutGpsCoordinates;
import org.sigmah.shared.command.result.LocationsWithoutGpsResult;
import org.sigmah.shared.dto.LocationDTO;
import org.sigmah.shared.dto.SiteDTO;
import org.sigmah.shared.dto.portlets.NoGpsLocations;

import com.extjs.gxt.ui.client.event.IconButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.button.ToolButton;
import com.extjs.gxt.ui.client.widget.custom.Portlet;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class NoGpsSitesPresenter {
	public interface View extends PortletView {
		void setSites(List<LocationDTO> sites);
	}
	public class NoGpsSitesPortletView implements View {
		private Portlet uiPortlet;
		private NoGpsLocations portlet;
		private ListStore<LocationDTO> store;

		public NoGpsSitesPortletView(NoGpsLocations portlet) {
			this.portlet=portlet;
			
			createPortlet();
			
			initializeComponent();
		}

		private void createPortlet() {
			uiPortlet = new Portlet();
			uiPortlet.setHeading("Sites without GPS coordinate");
			configPanel(uiPortlet);
		}
		
		  private void configPanel(final ContentPanel panel) {  
			    panel.setCollapsible(true);  
			    panel.setAnimCollapse(false);  
			    panel.getHeader().addTool(new ToolButton("x-tool-gear"));  
			    panel.getHeader().addTool(  
			        new ToolButton("x-tool-close", new SelectionListener<IconButtonEvent>() {  
			          @Override  
			          public void componentSelected(IconButtonEvent ce) {  
			            panel.removeFromParent();
			          }  
			        })
			       );  
			  }  

		private void initializeComponent() {
			store = new ListStore<LocationDTO>();
			List<ColumnConfig> configs = new ArrayList<ColumnConfig>();
			
			ColumnConfig columnPartner = new ColumnConfig();
			columnPartner.setId("partnerName");
			columnPartner.setHeader("Partner");
			columnPartner.setWidth(200);
			configs.add(columnPartner);
			
			ColumnConfig columnProject = new ColumnConfig();
			columnProject.setId("projectName");
			columnProject.setHeader("Project");
			columnProject.setWidth(200);
			configs.add(columnProject);
			
			Grid<LocationDTO> sites = new Grid<LocationDTO>(store, new ColumnModel(configs)); 
			
			uiPortlet.add(sites);
		}

		@Override
		public Portlet asPortlet() {
			return uiPortlet;
		}

		@Override
		public void setSites(List<LocationDTO> locations) {
			store.removeAll();
			store.add(locations);
		}

	}
	private Dispatcher service;
	private List<SiteDTO> sitesWithoutGps = new ArrayList<SiteDTO>();
	
	public NoGpsSitesPresenter(Dispatcher service) {
		this.service = service;
		
		getSitesWithoutGps();
	}

	private void getSitesWithoutGps() {
		service.execute(new GetLocationsWithoutGpsCoordinates(), null, new AsyncCallback<LocationsWithoutGpsResult>() {
			@Override
			public void onFailure(Throwable caught) {
				
			}
			@Override
			public void onSuccess(LocationsWithoutGpsResult result) {
				// TODO Auto-generated method stub
				
			}
		});
	}
}
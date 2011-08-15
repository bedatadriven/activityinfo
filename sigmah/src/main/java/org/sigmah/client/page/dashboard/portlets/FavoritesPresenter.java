package org.sigmah.client.page.dashboard.portlets;

import java.util.ArrayList;
import java.util.List;

import org.sigmah.client.dispatch.Dispatcher;
import org.sigmah.client.page.common.columns.ReadTextColumn;
import org.sigmah.client.page.dashboard.portlets.FavoritesPresenter.View.RemovedFavoriteHandler;
import org.sigmah.shared.command.GetFavoritePages;
import org.sigmah.shared.command.RemoveFavorite;
import org.sigmah.shared.command.result.UserFavorites;
import org.sigmah.shared.command.result.VoidResult;
import org.sigmah.shared.dto.PageDTO;
import org.sigmah.shared.dto.portlets.FavoritesDTO;

import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.custom.Portlet;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnData;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.grid.GridCellRenderer;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class FavoritesPresenter implements PortletPresenter {
	public interface View extends PortletView {
		public void setData(List<PageDTO> favorites);
		public HandlerRegistration addRemovedHandler(RemovedFavoriteHandler handler);
		public void removeFavorite(PageDTO favorite);
		
		public class RemovedFavoriteEvent extends
				GwtEvent<RemovedFavoriteHandler> {
			private static final Type<RemovedFavoriteHandler> TYPE = new Type<RemovedFavoriteHandler>();

			static Type<RemovedFavoriteHandler> getType() {
				return TYPE;
			}

			private PageDTO page;

			public RemovedFavoriteEvent(PageDTO page) {
				this.page = page;
			}

			public PageDTO getPage() {
				return page;
			}

			public void setPage(PageDTO page) {
				this.page = page;
			}

			@Override
			public final Type<RemovedFavoriteHandler> getAssociatedType() {
				return TYPE;
			}

			@Override
			protected void dispatch(RemovedFavoriteHandler handler) {
				handler.onRemovedFavorite(page);
			}
		}

		public interface RemovedFavoriteHandler extends EventHandler {
			void onRemovedFavorite(PageDTO page);
		}
	}
	
	public class FavoritesView implements View {
		private Portlet portlet;
		private Grid<PageDTO> grid;
		private ListStore<PageDTO> store;
		private EventBus eventBus = new SimpleEventBus();
		
		public FavoritesView() {
		}

		@Override
		public Portlet asPortlet() {
			return portlet;
		}

		@Override
		public void initialize() {
			store = new ListStore<PageDTO>();
			List<ColumnConfig> configs = new ArrayList<ColumnConfig>();

			configs.add(new ReadTextColumn("partnerName", "Saved page", 200));

			ColumnConfig columnRemovePage = new ColumnConfig();
			columnRemovePage.setId("remove");
			columnRemovePage.setHeader("X");
			columnRemovePage.setWidth(200);
			
			GridCellRenderer<PageDTO> buttonRenderer = new GridCellRenderer<PageDTO>() {  
					@Override
					public Object render(final PageDTO model, String property,
							ColumnData config, int rowIndex, int colIndex,
							ListStore<PageDTO> store, Grid<PageDTO> grid) {
				        Button removeButton = new Button((String) model.get(property), new SelectionListener<ButtonEvent>() {  
					          @Override  
					          public void componentSelected(ButtonEvent ce) {
					        	  eventBus.fireEvent(new RemovedFavoriteEvent(model));
					          }
					    });
				        removeButton.setWidth(32);  
				        removeButton.setToolTip("Remove favorite");
				  
				        return removeButton;
					}
				};  
			
			columnRemovePage.setRenderer(buttonRenderer);
			configs.add(columnRemovePage);

			Grid<PageDTO> sites = new Grid<PageDTO>(store,
					new ColumnModel(configs));

			portlet.add(grid);
			
			grid = new Grid<PageDTO>(store, new ColumnModel(configs));
		}

		@Override
		public void setData(List<PageDTO> favorites) {
			store.removeAll();
			store.add(favorites);
		}

		@Override
		public HandlerRegistration addRemovedHandler(RemovedFavoriteHandler handler) {
			return eventBus.addHandler(RemovedFavoriteEvent.TYPE, handler);
		}

		@Override
		public void removeFavorite(PageDTO favorite) {
			store.remove(favorite);
		}
		
	}
	
	private View view;
	private FavoritesDTO favoritesPortlet;
	private Dispatcher service;
	private UserFavorites userFavorites;
	
	public FavoritesPresenter(FavoritesDTO favoritesPortlet) {
		this.favoritesPortlet = favoritesPortlet;
		
		initializeView();
		
		getFavorites();
	}

	private void getFavorites() {
		service.execute(new GetFavoritePages(), null, new AsyncCallback<UserFavorites>() {
			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onSuccess(UserFavorites result) {
				userFavorites = result;
				view.setData(userFavorites.getFavorites());
			}
		}); 
	}

	private void initializeView() {		
		view = new FavoritesView();
		view.addRemovedHandler(new RemovedFavoriteHandler() {
			@Override
			public void onRemovedFavorite(final PageDTO page) {
				service.execute(new RemoveFavorite(), null, new AsyncCallback<VoidResult>() {
					@Override
					public void onFailure(Throwable caught) {
						// TODO Handle failure
					}

					@Override
					public void onSuccess(VoidResult result) {
						view.removeFavorite(page);
					}
				});
			}
		});
		view.initialize();
	}

	@Override
	public PortletView getView() {
		return view;
	}
}

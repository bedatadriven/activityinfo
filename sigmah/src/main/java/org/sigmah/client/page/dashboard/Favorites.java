package org.sigmah.client.page.dashboard;

import java.util.HashMap;
import java.util.Map;

import org.sigmah.client.dispatch.Dispatcher;
import org.sigmah.client.page.dashboard.Favorite.FavoriteChangedEventHandler;
import org.sigmah.shared.command.GetDashboard;
import org.sigmah.shared.command.result.DashboardSettingsResult;
import org.sigmah.shared.dto.DashboardSettingsDTO;
import org.sigmah.shared.dto.FavoriteDTO;

import com.extjs.gxt.ui.client.Style.Orientation;
import com.extjs.gxt.ui.client.widget.layout.RowLayout;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class Favorites extends AiPortlet {
	private Map<FavoriteDTO, Favorite> favoritewidgetsByFavorite = new HashMap<FavoriteDTO, Favorite>();
	private DashboardSettingsDTO dashboard;
	
	public Favorites(Dispatcher service, String title) {
		super(service, title);
		
		setLayout(new RowLayout(Orientation.VERTICAL));

		getDashboard();
	}

	private void getDashboard() {
		service.execute(new GetDashboard(), loadingMonitor, new AsyncCallback<DashboardSettingsResult>() {
			@Override
			public void onFailure(Throwable caught) {
				// TODO Handle failure
				loadingMonitor.onServerError();
			}
			@Override
			public void onSuccess(DashboardSettingsResult result) {
				Favorites.this.dashboard = result.getDashboard();
				
				for (FavoriteDTO favorite : result.getDashboard().getFavorites()) {
					Favorite favoriteWidget = new Favorite(favorite);
					favoriteWidget.addFavoriteChangedEventHandler(new FavoriteChangedEventHandler() {
						@Override
						public void onFavoriteChanged(boolean isFavorite,FavoriteDTO favorite) {
							remove(favoritewidgetsByFavorite.get(favorite));
							dashboard.getFavorites().remove(favorite);
						}
					});
					favoritewidgetsByFavorite.put(favorite, favoriteWidget);
					add(favoriteWidget);
				}
				layout(true);
			}
		});
	}
	
}

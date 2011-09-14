package org.sigmah.client.page.dashboard;

import java.util.HashMap;
import java.util.Map;

import org.sigmah.client.dispatch.Dispatcher;
import org.sigmah.client.page.dashboard.Favorite.FavoriteChangedEventHandler;
import org.sigmah.shared.command.GetDashboard;
import org.sigmah.shared.command.result.DashboardSettingsResult;
import org.sigmah.shared.dto.DashboardSettingsDTO;
import org.sigmah.shared.dto.FavoriteDTO;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.VerticalPanel;

public class Favorites extends VerticalPanel {
	private Map<FavoriteDTO, Favorite> favoritewidgetsByFavorite = new HashMap<FavoriteDTO, Favorite>();
	private DashboardSettingsDTO dashboard;
	private Dispatcher service;
	//private AsyncMonitor loadingMonitor = new MaskingAsyncMonitor(this, I18N.CONSTANTS.loading());
	
	public Favorites(Dispatcher service, String title) {
		this.service=service;

		getDashboard();
	}

	private void getDashboard() {
		service.execute(new GetDashboard(), null, new AsyncCallback<DashboardSettingsResult>() {
			@Override
			public void onFailure(Throwable caught) {
				// TODO Handle failure
				//loadingMonitor.onServerError();
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
				//layout(true);
			}
		});
	}
	
}

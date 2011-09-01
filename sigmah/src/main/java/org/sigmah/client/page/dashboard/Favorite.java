package org.sigmah.client.page.dashboard;

import org.sigmah.client.icon.IconImageBundle;
import org.sigmah.client.page.common.FavoriteMarker;
import org.sigmah.shared.dto.FavoriteDTO;

import com.extjs.gxt.ui.client.widget.HorizontalPanel;
import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.google.gwt.user.client.ui.Hyperlink;

public class Favorite extends HorizontalPanel {
	private Hyperlink linkName;
	private FavoriteMarker favoritemarkerFavoriteButton;
	private FavoriteDTO favorite;
	
	public Favorite(final FavoriteDTO favorite) {
		super();
		
		this.favorite = favorite;
		
		setSpacing(5);
		favoritemarkerFavoriteButton = new FavoriteMarker();
		favoritemarkerFavoriteButton.addFavoriteChangedEventHandler(new FavoriteMarker.FavoriteChangedEventHandler() {
			@Override
			public void onFavoriteChanged(boolean isFavorite) {
				if (!isFavorite) {
					fireEvent(new FavoriteChangedEvent(isFavorite, favorite));
				}
			}
		});
		add(favoritemarkerFavoriteButton);
		add(getIconByFavoriteType(favorite.getFavoriteType()).createImage());
		linkName = new Hyperlink(favorite.getName(), "http://nu.nl");
		add(linkName);
	}
	
	public HandlerRegistration addFavoriteChangedEventHandler(FavoriteChangedEventHandler handler) {
		return addHandler(handler, FavoriteChangedEvent.TYPE);
	}
	
	public interface FavoriteChangedEventHandler extends EventHandler {
		public void onFavoriteChanged(boolean isFavorite, FavoriteDTO favorite);
	}
	
	public static class FavoriteChangedEvent extends GwtEvent<FavoriteChangedEventHandler> {
		public static final Type<FavoriteChangedEventHandler> TYPE = new Type<FavoriteChangedEventHandler>(); 
		private boolean isFavorite;
		private FavoriteDTO favorite;
		
		public FavoriteChangedEvent(boolean isFavorite, FavoriteDTO favorite) {
			super();
			this.isFavorite = isFavorite;
			this.favorite = favorite;
		}

		public boolean isFavorite() {
			return isFavorite;
		}

		public void setFavorite(boolean isFavorite) {
			this.isFavorite = isFavorite;
		}

		public FavoriteDTO getFavorite() {
			return favorite;
		}

		public void setFavorite(FavoriteDTO favorite) {
			this.favorite = favorite;
		}

		@Override
		public com.google.gwt.event.shared.GwtEvent.Type<FavoriteChangedEventHandler> getAssociatedType() {
			return null;
		}

		@Override
		protected void dispatch(FavoriteChangedEventHandler handler) {
			handler.onFavoriteChanged(isFavorite, favorite);
		}
	}

	private AbstractImagePrototype getIconByFavoriteType(org.sigmah.shared.dto.FavoriteType favoriteType) {
		switch (favoriteType) {
		case Activity:
			return IconImageBundle.ICONS.activity();
		case AttributeGroup:
			return IconImageBundle.ICONS.attributeGroup();
		case Attribute:
			return IconImageBundle.ICONS.attribute();
		case Chart:
			return IconImageBundle.ICONS.barChart();
		case  Database:
			return IconImageBundle.ICONS.database();
		case Indicator:
			return IconImageBundle.ICONS.indicator();
		case Map:
			return IconImageBundle.ICONS.map();
		case Partner:
			return IconImageBundle.ICONS.partner();
		case PivotTable:
			return IconImageBundle.ICONS.table();
		case Report:
			return IconImageBundle.ICONS.report();
		case Project:
			return IconImageBundle.ICONS.project();
		case Site:
			return IconImageBundle.ICONS.site();
		}
		return null;
	}
}	
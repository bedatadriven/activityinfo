package org.sigmah.client.page.common;

import org.sigmah.client.icon.IconImageBundle;

import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Image;

/*
 * Displays a star icon swapping active when hovered over or changed state by clicking 
 */
public class FavoriteMarker extends AbsolutePanel implements MouseOverHandler, MouseOutHandler {
	private Image imageActive = IconImageBundle.ICONS.favorite().createImage();
	private Image imageInActive = IconImageBundle.ICONS.emptyFavorite().createImage();
	private boolean isFavorite = false;
	private static final int margin=0;
	
	public FavoriteMarker() {
		super();
		
		setSize(Integer.toString(16 + (margin * 2)), 
				Integer.toString(16 + (margin * 2)));

		addDomHandler(this, MouseOverEvent.getType());
		addDomHandler(this, MouseOutEvent.getType());
		
		add(imageActive, margin,margin);
		add(imageInActive, margin,margin);
		setFavorite(isFavorite);
	}

	public void setFavorite(boolean isFavorite) {
		boolean fireEvent = this.isFavorite != isFavorite; 
		this.isFavorite=isFavorite;
		if (fireEvent) {
			fireEvent(new FavoriteChangedEvent(isFavorite));
		}
	}

	@Override
	public void onMouseOver(MouseOverEvent event) {
		updateImage(!isFavorite);
	}

	private void updateImage(boolean isFavorite) {
		imageInActive.setVisible(!isFavorite);
		imageActive.setVisible(isFavorite);
	}
	
	public HandlerRegistration addFavoriteChangedEventHandler(FavoriteChangedEventHandler handler) {
		return addHandler(handler, FavoriteChangedEvent.TYPE);
	}
	
	public interface FavoriteChangedEventHandler extends EventHandler {
		public void onFavoriteChanged(boolean isFavorite);
	}
	
	public static class FavoriteChangedEvent extends GwtEvent<FavoriteChangedEventHandler> {
		public static final Type<FavoriteChangedEventHandler> TYPE = new Type<FavoriteChangedEventHandler>(); 
		private boolean isFavorite;
		
		public FavoriteChangedEvent(boolean isFavorite) {
			super();
			this.isFavorite = isFavorite;
		}

		public boolean isFavorite() {
			return isFavorite;
		}

		public void setFavorite(boolean isFavorite) {
			this.isFavorite = isFavorite;
		}

		@Override
		public com.google.gwt.event.shared.GwtEvent.Type<FavoriteChangedEventHandler> getAssociatedType() {
			return null;
		}

		@Override
		protected void dispatch(FavoriteChangedEventHandler handler) {
			handler.onFavoriteChanged(isFavorite);
		}
	}

	@Override
	public void onMouseOut(MouseOutEvent event) {
		updateImage(isFavorite);
	}
}

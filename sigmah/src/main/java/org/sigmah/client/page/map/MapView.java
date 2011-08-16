package org.sigmah.client.page.map;

import org.sigmah.shared.command.result.SitePointList;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.IsWidget;

public interface MapView extends IsWidget{
	public interface SiteSelectedHandler extends EventHandler {
		void onSiteSelected(SiteSelectedEvent siteSelectedEvent);
	}

	public void setSites(SitePointList sites);
	
	public void selectSite(int SiteId);
	
	// The user signals the presenter it wants to add a new item
	public HandlerRegistration addSiteSelectedHandler(SiteSelectedHandler handler);
	
	// The Presenter has a seperate view for creating/updating domain object
	public class SiteSelectedEvent extends GwtEvent<SiteSelectedHandler> {
		public static Type TYPE = new Type<SiteSelectedHandler>(); 
		private int siteId;
		
		public SiteSelectedEvent(int siteId) {
			super();
			this.siteId = siteId;
		}

		public int getSiteId() {
			return siteId;
		}

		public void setSiteId(int siteId) {
			this.siteId = siteId;
		}

		@Override
		public Type<SiteSelectedHandler> getAssociatedType() {
			return TYPE;
		}

		@Override
		protected void dispatch(SiteSelectedHandler handler) {
			handler.onSiteSelected(this);
		}
	}
}

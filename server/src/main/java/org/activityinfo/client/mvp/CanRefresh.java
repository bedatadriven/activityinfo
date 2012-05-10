package org.activityinfo.client.mvp;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;

public interface CanRefresh<M> {
	interface RefreshHandler extends EventHandler {
		void onRefresh(RefreshEvent refreshEvent);
	}

	// The user wants to have the latest information from the server.
	// The refresh on the view is managed by calling the setValue and other setData
	// methods.
	HandlerRegistration addRefreshHandler(RefreshHandler handler);
	
	// Is the refresh button enabled?
	void setRefreshEnabled(boolean canRefresh);
	
	public class RefreshEvent extends GwtEvent<RefreshHandler> {
		public static final Type TYPE = new Type<RefreshHandler>(); 
		
		@Override
		public Type<RefreshHandler> getAssociatedType() {
			return TYPE;
		}

		@Override
		protected void dispatch(RefreshHandler handler) {
			handler.onRefresh(this);
		}
	}
}

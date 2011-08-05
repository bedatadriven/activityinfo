package org.sigmah.client.mvp;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;

public interface CanUpdate<M> {
	public interface CancelUpdateHandler extends EventHandler {
		void onCancelUpdate(CancelUpdateEvent updateEvent);
	}
	public interface UpdateHandler extends EventHandler {
		void onUpdate(UpdateEvent updateEvent);
	}
	
	// An item is updated by the presenter, this method updates the item at the view
	public void update(M item);
	
	public void cancelUpdate(M item);
	
	public void setUpdateEnabled(boolean updateEnabled);
	public void setCanCancelUpdate(boolean canCancelUpdate);

	// The user wants to save dirty changes 
	public HandlerRegistration addUpdateHandler(UpdateHandler handler);
	
	// The user wants to cancel the changes made to an item
	public HandlerRegistration addCancelUpdateHandler(CancelUpdateHandler handler);
	
	// The Presenter has a seperate view for creating/updating domain object
	public class UpdateEvent extends GwtEvent<UpdateHandler> {
		public static Type TYPE = new Type<UpdateHandler>(); 
		
		@Override
		public Type<UpdateHandler> getAssociatedType() {
			return TYPE;
		}

		@Override
		protected void dispatch(UpdateHandler handler) {
			handler.onUpdate(this);
		}
	}

	
	// The Presenter has a seperate view for creating/updating domain object
	public class CancelUpdateEvent extends GwtEvent<CancelUpdateHandler> {
		public static Type TYPE = new Type<CancelUpdateHandler>(); 
		
		@Override
		public Type<CancelUpdateHandler> getAssociatedType() {
			return TYPE;
		}

		@Override
		protected void dispatch(CancelUpdateHandler handler) {
			handler.onCancelUpdate(this);
		}
	}
}

package org.sigmah.client.mvp;

import org.sigmah.client.dispatch.AsyncMonitor;

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
	public interface RequestUpdateHandler extends EventHandler{
		void onRequestUpdate(RequestUpdateEvent requestUpdateEvent);
	}
	
	// An item is updated by the presenter, this method updates the item at the view
	public void update(M item);
	
	// Throw away changes of given item 
	public void cancelUpdate(M item);
	
	// Throw away all changes for all changed objects
	public void cancelUpdateAll();
	
	public void startUpdate();
	
	// Is the update button enabled?
	public void setUpdateEnabled(boolean updateEnabled);
	
	// Let the user know what's going on during updating
	public AsyncMonitor getUpdatingMonitor();
	
	// The user wants to save dirty changes 
	public HandlerRegistration addUpdateHandler(UpdateHandler handler);
	
	// The user wants to cancel the changes made to an item
	public HandlerRegistration addCancelUpdateHandler(CancelUpdateHandler handler);
	
	// The user wants to cancel the changes made to an item
	public HandlerRegistration addRequestUpdateHandler(RequestUpdateHandler handler);
	
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
	
	// The Presenter has a seperate view for creating/updating domain object
	public class RequestUpdateEvent extends GwtEvent<RequestUpdateHandler> {
		public static Type TYPE = new Type<RequestUpdateHandler>(); 
		
		@Override
		public Type<RequestUpdateHandler> getAssociatedType() {
			return TYPE;
		}

		@Override
		protected void dispatch(RequestUpdateHandler handler) {
			handler.onRequestUpdate(this);
		}
	}}

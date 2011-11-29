package org.sigmah.client.mvp;

import org.sigmah.client.dispatch.AsyncMonitor;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;

public interface CanUpdate<M> {
	interface CancelUpdateHandler extends EventHandler {
		void onCancelUpdate(CancelUpdateEvent updateEvent);
	}
	interface UpdateHandler extends EventHandler {
		void onUpdate(UpdateEvent updateEvent);
	}
	interface RequestUpdateHandler extends EventHandler{
		void onRequestUpdate(RequestUpdateEvent requestUpdateEvent);
	}
	
	// An item is updated by the presenter, this method updates the item at the view
	void update(M item);
	
	// Throw away changes of given item 
	void cancelUpdate(M item);
	
	// Throw away all changes for all changed objects
	void cancelUpdateAll();
	
	void startUpdate();
	
	// Is the update button enabled?
	void setUpdateEnabled(boolean updateEnabled);
	
	// Let the user know what's going on during updating
	AsyncMonitor getUpdatingMonitor();
	
	// The user wants to save dirty changes 
	HandlerRegistration addUpdateHandler(UpdateHandler handler);
	
	// The user wants to cancel the changes made to an item
	HandlerRegistration addCancelUpdateHandler(CancelUpdateHandler handler);
	
	// The user wants to cancel the changes made to an item
	HandlerRegistration addRequestUpdateHandler(RequestUpdateHandler handler);
	
	// The Presenter has a seperate view for creating/updating domain object
	class UpdateEvent extends GwtEvent<UpdateHandler> {
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
	class CancelUpdateEvent extends GwtEvent<CancelUpdateHandler> {
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
	class RequestUpdateEvent extends GwtEvent<RequestUpdateHandler> {
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

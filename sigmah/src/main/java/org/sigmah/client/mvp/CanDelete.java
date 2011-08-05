package org.sigmah.client.mvp;

import org.sigmah.shared.dto.DTO;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;

public interface CanDelete<M extends DTO> {
	public interface ConfirmDeleteHandler extends EventHandler {
		void onConfirmDelete(ConfirmDeleteEvent deleteEvent);
	}
	public interface RequestDeleteHandler extends EventHandler {
		void onRequestDelete(RequestDeleteEvent deleteEvent);
	}

	// The user intends to remove an item
	public HandlerRegistration addRequestDeleteHandler(RequestDeleteHandler handler);
	
	// The user confirmed his request to remove an item
	public HandlerRegistration addConfirmDeleteHandler(ConfirmDeleteHandler handler);

	public void delete(M item);
	
	// The presenter wants to know from the user if he really intends to remove the item
	public void askConfirmDelete(M item);
	
	public void setDeleteEnabled(boolean deleteEnabled);
	
	// Since View<T> extends TakesValue<T>, the value does not need to be encapsulated
	public class RequestDeleteEvent extends GwtEvent<RequestDeleteHandler> {
		public static Type TYPE = new Type<RequestDeleteHandler>(); 
		
		@Override
		public Type<RequestDeleteHandler> getAssociatedType() {
			return TYPE;
		}

		@Override
		protected void dispatch(RequestDeleteHandler handler) {
			handler.onRequestDelete(this);
		}
	}
	
	// Since View<T> extends TakesValue<T>, the value does not need to be encapsulated
	public class ConfirmDeleteEvent extends GwtEvent<ConfirmDeleteHandler> {
		public static Type TYPE = new Type<ConfirmDeleteHandler>(); 
		
		@Override
		public Type<ConfirmDeleteHandler> getAssociatedType() {
			return TYPE;
		}

		@Override
		protected void dispatch(ConfirmDeleteHandler handler) {
			handler.onConfirmDelete(this);
		}
	}
}

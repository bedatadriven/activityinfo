package org.sigmah.client.mvp;

import org.sigmah.server.endpoint.gwtrpc.handler.DeleteHandler;
import org.sigmah.shared.dto.DTO;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;

public interface CanCreate<M extends DTO> {
	
	public interface CreateHandler extends EventHandler {
		void onCreate(CreateEvent createEvent);
	}

	// The user signals the presenter it wants to add a new item
	public HandlerRegistration addCreateHandler(CreateHandler handler);
	
	// An item is created by the presenter, this method adds the item to the view 
	public void create(M item);
	
	public void setCreateEnabled(boolean createEnabled);

	// The Presenter has a seperate view for creating/updating domain object
	public class CreateEvent extends GwtEvent<CreateHandler> {
		public static Type TYPE = new Type<DeleteHandler>(); 
		
		@Override
		public Type<CreateHandler> getAssociatedType() {
			return TYPE;
		}

		@Override
		protected void dispatch(CreateHandler handler) {
			handler.onCreate(this);
		}
	}
}

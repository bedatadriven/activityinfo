package org.sigmah.client.mvp;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;

/*
 * A generalized view interface for views showing a list of items and allowing the user to
 * perform create, update and delete actions
 * 
 * The view is passive and only notifies the Presenter when a user wants to perform
 * a C/U/D action.
 * 
 * The model will usually be an entity wrapping a list of items.
 */
public interface CrudView<T> extends View<T> {
	public HandlerRegistration addCreateHandler(CreateHandler handler);
	public HandlerRegistration addUpdateHandler(UpdateHandler handler);
	public HandlerRegistration addDeleteHandler(DeleteHandler handler);
	
	public void create(T item);
	public void update(T item);
	public void delete(T item);
	
	/*
	 * whether or not the user currently may C/U/D an item
	 */
	public void setCreateEnabled(boolean createEnabled);
	public void setUpdateEnabled(boolean updateEnabled);
	public void setDeleteEnabled(boolean deleteEnabled);
	
	public interface CreateHandler extends EventHandler {
		void onCreate(CreateEvent event);
	}
	public interface UpdateHandler extends EventHandler {
		void onUpdate(UpdateEvent event);
	}
	public interface DeleteHandler extends EventHandler {
		void onRemove(DeleteEvent event);
	}
	
	// The Presenter has a seperate view for creating/updating domain object
	public class CreateEvent extends GwtEvent<CreateHandler> {
		private static Type TYPE = new Type<DeleteHandler>(); 
		
		@Override
		public Type<CreateHandler> getAssociatedType() {
			return TYPE;
		}

		@Override
		protected void dispatch(CreateHandler handler) {
			handler.onCreate(this);
		}
	}
	
	// The Presenter has a seperate view for creating/updating domain object
	public class UpdateEvent extends GwtEvent<UpdateHandler> {
		private static Type TYPE = new Type<UpdateHandler>(); 
		
		@Override
		public Type<UpdateHandler> getAssociatedType() {
			return TYPE;
		}

		@Override
		protected void dispatch(UpdateHandler handler) {
			handler.onUpdate(this);
		}
	}

	// Since View<T> extends TakesValue<T>, the value does not need to be encapsulated
	public class DeleteEvent extends GwtEvent<DeleteHandler> {
		private static Type TYPE = new Type<DeleteHandler>(); 
		
		@Override
		public Type<DeleteHandler> getAssociatedType() {
			return TYPE;
		}

		@Override
		protected void dispatch(DeleteHandler handler) {
			handler.onRemove(this);
		}
	}
}

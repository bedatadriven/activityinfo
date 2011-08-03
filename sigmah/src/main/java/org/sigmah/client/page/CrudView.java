package org.sigmah.client.page;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;

/*
 * A generalized view interface for views showing a list of items and allowing the user to
 * perform create, update and delete actions
 * 
 * The view is passive and only notifies the  
 */
public interface CrudView<T> extends View<T> {
	public HandlerRegistration addRemoveHandler(DeleteHandler handler);
	public HandlerRegistration addCreateHandler(CreateHandler handler);
	public HandlerRegistration addChangeHandler(UpdateHandler handler);
	
	public void delete(T item);
	public void create(T item);
	public void update(T item);
	
	public void setDeleteEnabled();
	public void setUpdateEnabled();
	public void setCreateEnabled();
	
	public interface DeleteHandler extends EventHandler {
		void onRemove(DeleteEvent event);
	}
	public interface CreateHandler extends EventHandler {
		void onCreate(CreateEvent event);
	}
	public interface UpdateHandler extends EventHandler {
		void onUpdate(UpdateEvent event);
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
}

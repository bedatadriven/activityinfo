package org.sigmah.client.mvp;

import org.sigmah.server.endpoint.gwtrpc.handler.DeleteHandler;
import org.sigmah.shared.dto.DTO;

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
 * Each event and handler are defined explicitly to allow for more verbose implementation
 * on the presenter and the view. Technically, Update/CancelUpdate and RequestDelete/
 * ConfirmDelete can be merged.
 * 
 * The model will usually be an entity wrapping a list of items.
 * 
 * M: the model, a DTO object 
 * P: parent, holding a collection of DTO's
 */
public interface CrudView<M extends DTO, P extends DTO> extends View<M> {
//public interface CrudView<M extends DTO, P extends HasCollection<M>> extends View<M> {
	
	/*
	 * Events to notify presenter
	 */
	
	// The user signals the presenter it wants to add a new item
	public HandlerRegistration addCreateHandler(CreateHandler handler);
	
	// The user wants to save dirty changes 
	public HandlerRegistration addUpdateHandler(UpdateHandler handler);
	
	// The user wants to cancel the changes made to an item
	public HandlerRegistration addCancelUpdateHandler(UpdateHandler handler);
	
	// The user intends to remove an item
	public HandlerRegistration addRequestDeleteHandler(DeleteHandler handler);
	
	// The user confirmed his request to remove an item
	public HandlerRegistration addConfirmDeleteHandler(ConfirmDeleteHandler handler);
	
	// The user wants to have the latest information from the server.
	// The refresh on the view is managed by calling the setValue and other setData
	// methods.
	public HandlerRegistration addRefreshHandler(RefreshHandler handler);
	
	// The user intends to filter the list of items
	public HandlerRegistration addFilterHandler(FilterHandler filter);
	
	/*
	 * Signals from the presenter to this implemented view
	 */
	
	// An item is created by the presenter, this method adds the item to the view 
	public void create(M item);
	
	// An item is updated by the presenter, this method updates the item at the view
	public void update(M item);
	
	public void cancelUpdate(M item);
	public void delete(M item);
	public void filter(Filter<M> filter);
	
	// The presenter wants to know from the user if he really intends to remove the item
	public void askConfirmDelete(M item);
	
	// Set the parent containing the list of items
	public void setParent(P parent);
	
	/*
	 * whether or not the user currently may C/U/D an item
	 */
	public void setCreateEnabled(boolean createEnabled);
	public void setUpdateEnabled(boolean updateEnabled);
	public void setCanCancelUpdate(boolean canCancelUpdate);
	public void setDeleteEnabled(boolean deleteEnabled);
	
	public interface CreateHandler extends EventHandler {
		void onCreate(CreateEvent createEvent);
	}
	public interface UpdateHandler extends EventHandler {
		void onUpdate(UpdateEvent updateEvent);
	}
	public interface CancelUpdateHandler extends EventHandler {
		void onCancelUpdate(CancelUpdateEvent updateEvent);
	}
	public interface RequestDeleteHandler extends EventHandler {
		void onRequestDelete(RequestDeleteEvent deleteEvent);
	}
	public interface ConfirmDeleteHandler extends EventHandler {
		void onConfirmDelete(ConfirmDeleteEvent deleteEvent);
	}
	public interface RefreshHandler extends EventHandler {
		void onRefresh(RefreshEvent refreshEvent);
	}
	public interface FilterHandler extends EventHandler {
		void onFilter(FilterEvent filterEvent);
	}
	
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
	
	public class RefreshEvent extends GwtEvent<RefreshHandler> {
		public static Type TYPE = new Type<RefreshHandler>(); 
		
		@Override
		public Type<RefreshHandler> getAssociatedType() {
			return TYPE;
		}

		@Override
		protected void dispatch(RefreshHandler handler) {
			handler.onRefresh(this);
		}
	}
	
	public class FilterEvent extends GwtEvent<FilterHandler> {
		public static Type TYPE = new Type<FilterHandler>(); 
		
		@Override
		public Type<FilterHandler> getAssociatedType() {
			return TYPE;
		}

		@Override
		protected void dispatch(FilterHandler handler) {
			handler.onFilter(this);
		}
	}
}

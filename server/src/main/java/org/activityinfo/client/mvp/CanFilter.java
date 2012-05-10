package org.activityinfo.client.mvp;

import org.activityinfo.shared.dto.DTO;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;

public interface CanFilter<M extends DTO> {
	public interface FilterHandler extends EventHandler {
		void onFilter(FilterEvent filterEvent);
	}
	
	// The user intends to filter the list of items
	public HandlerRegistration addFilterHandler(FilterHandler filter);
	
	public void filter(Filter<M> filter);
	
	public void removeFilter();
	
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

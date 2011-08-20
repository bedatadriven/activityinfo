package org.sigmah.client.page.common.filter;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;

public interface FilterToolBar {
	
	public void setRemoveFilterEnabled(boolean enabled);
	public void setApplyFilterEnabled(boolean enabled);
	
	public interface ApplyFilterHandler extends EventHandler {
		void onApplyFilter(ApplyFilterEvent deleteEvent);
	}

	public interface RemoveFilterHandler extends EventHandler {
		void onRemoveFilter(RemoveFilterEvent deleteEvent);
	}

	public HandlerRegistration addApplyFilterHandler(ApplyFilterHandler handler);
	public HandlerRegistration addRemoveFilterHandler(RemoveFilterHandler handler);

	public class ApplyFilterEvent extends GwtEvent<ApplyFilterHandler> {
		public static Type TYPE = new Type<ApplyFilterHandler>(); 

		@Override
		public Type<ApplyFilterHandler> getAssociatedType() {
			return TYPE;
		}

		@Override
		protected void dispatch(ApplyFilterHandler handler) {
			handler.onApplyFilter(this);
		}
	}
	
	public class RemoveFilterEvent extends GwtEvent<RemoveFilterHandler> {
		public static Type TYPE = new Type<RemoveFilterHandler>(); 

		@Override
		public Type<RemoveFilterHandler> getAssociatedType() {
			return TYPE;
		}

		@Override
		protected void dispatch(RemoveFilterHandler handler) {
			handler.onRemoveFilter(this);
		}
	}
}

package org.sigmah.client.filter;

import org.sigmah.shared.command.Filter;

import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;

public class NullFilterPanel implements FilterPanel {

	@Override
	public Filter getValue() {
		return new Filter();
	}

	@Override
	public void setValue(Filter value) {		
	}

	@Override
	public void setValue(Filter value, boolean fireEvents) {		
	}

	@Override
	public HandlerRegistration addValueChangeHandler(
			ValueChangeHandler<Filter> handler) {
		return new HandlerRegistration() {
			
			@Override
			public void removeHandler() {
				
			}
		};
	}

	@Override
	public void fireEvent(GwtEvent<?> event) {
		
	}

	@Override
	public void applyBaseFilter(Filter filter) {		
	}

}

package org.sigmah.client.filter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.sigmah.shared.command.Filter;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.event.shared.HandlerRegistration;

public class FilterPanelSet implements FilterPanel {
	
	private List<FilterPanel> panels;
	private HandlerManager manager;
	private List<HandlerRegistration> myRegistrations;
	
	public FilterPanelSet(FilterPanel... panels) {
		super();
		this.panels = Arrays.asList(panels);
	}

	private HandlerManager ensureHandlers() {
		if(manager == null) {
			manager = new HandlerManager(this);
			myRegistrations = new ArrayList<HandlerRegistration>();
			for(FilterPanel panel : panels) {
				HandlerRegistration registration = panel.addValueChangeHandler(new ValueChangeHandler<Filter>() {
					@Override
					public void onValueChange(ValueChangeEvent<Filter> event) {
						Filter value = composeFilter(new Filter(), null);
						ValueChangeEvent.fire(FilterPanelSet.this, value);
					}
				});
				myRegistrations.add(registration);
			}
		}
		return manager;
	}
	
	public Filter getValue() {
		return composeFilter(new Filter(), null);
	}

	@Override
	public void applyBaseFilter(Filter baseFilter) {
		for(FilterPanel panel : panels) {
			panel.applyBaseFilter(composeFilter(baseFilter, panel));
		}
	}

	private Filter composeFilter(Filter baseFilter, FilterPanel exclude) {
		Filter filter = baseFilter;
		for(FilterPanel panel : panels) {
			if(panel != exclude) {
				filter = new Filter(filter, panel.getValue());
			}
		}
		return filter;
	}

	@Override
	public void setValue(Filter value) {
		for(FilterPanel panel : panels) {
			panel.setValue(value);
		}
	}

	@Override
	public void setValue(Filter value, boolean fireEvents) {
		for(FilterPanel panel : panels) {
			panel.setValue(value, fireEvents);
		}
	}

	@Override
	public HandlerRegistration addValueChangeHandler(ValueChangeHandler<Filter> handler) {
		final HandlerRegistration reg = ensureHandlers().addHandler(ValueChangeEvent.getType(), handler);
		return new HandlerRegistration() {
			@Override
			public void removeHandler() {
				reg.removeHandler();
				if(manager.getHandlerCount(ValueChangeEvent.getType()) == 0) {
					for(HandlerRegistration myReg : myRegistrations) {
						myReg.removeHandler();
					}
				}
				manager = null;
			}
		};
	}

	@Override
	public void fireEvent(GwtEvent<?> event) {
		if(manager != null) {
			manager.fireEvent(event);
		}
	}

}

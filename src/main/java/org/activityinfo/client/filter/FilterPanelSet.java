package org.activityinfo.client.filter;

/*
 * #%L
 * ActivityInfo Server
 * %%
 * Copyright (C) 2009 - 2013 UNICEF
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the 
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public 
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.activityinfo.shared.command.Filter;

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
			panel.applyBaseFilter(value);
			panel.setValue(value);
		}
	}

	@Override
	public void setValue(Filter value, boolean fireEvents) {
		for(FilterPanel panel : panels) {
			panel.applyBaseFilter(value);
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

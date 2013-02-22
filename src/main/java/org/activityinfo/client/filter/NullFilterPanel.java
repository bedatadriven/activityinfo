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

import org.activityinfo.shared.command.Filter;

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

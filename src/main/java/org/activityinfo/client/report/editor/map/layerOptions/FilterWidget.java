package org.activityinfo.client.report.editor.map.layerOptions;

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

import org.activityinfo.client.filter.FilterPanel;
import org.activityinfo.shared.command.Filter;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.Widget;

public abstract class FilterWidget extends Widget implements FilterPanel {

	private static FilterWidgetUiBinder uiBinder = GWT
			.create(FilterWidgetUiBinder.class);

	interface FilterWidgetUiBinder extends UiBinder<Element, FilterWidget> {
	}

	@UiField SpanElement dimensionSpan;
	@UiField SpanElement stateSpan;
//	@UiField Element removeIcon;
	
	private Filter value = new Filter();
	private Filter baseFilter = new Filter();

	public FilterWidget() {
		setElement(uiBinder.createAndBindUi(this));
		sinkEvents(Event.ONCLICK);
	}

	@Override
	public final void onBrowserEvent(Event event) {
		if(event.getTypeInt() == Event.ONCLICK) {
			choose(event);
		}
	}
	
	protected final void setState(String html) {
		stateSpan.setInnerHTML(html);
	}
	
	protected abstract void choose(Event event);

	@Override
	public final Filter getValue() {
		return value;
	}

	@Override
	public final void setValue(Filter value) {
		setValue(value, true);
	}

	@Override
	public final void setValue(Filter value, boolean fireEvents) {
		if(value == null) {
			value = new Filter();
		}
		this.value = value;
		if(fireEvents) {
			ValueChangeEvent.fire(this, this.value);
		}
		updateView();
	}

	protected void updateView() {
	
	}
	
	@Override
	public final HandlerRegistration addValueChangeHandler(ValueChangeHandler<Filter> handler) {
		return addHandler(handler, ValueChangeEvent.getType());
	}

	@Override
	public void applyBaseFilter(Filter filter) {
		this.baseFilter = filter;
	}

	protected void setBaseFilter(Filter baseFilter) {
		this.baseFilter = baseFilter;
	}

	protected Filter getBaseFilter() {
		return baseFilter;
	}
}

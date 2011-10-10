package org.sigmah.client.page.map.layerOptions;

import org.sigmah.client.page.common.filter.FilterPanel;
import org.sigmah.shared.dao.Filter;

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
	
	@UiField Element removeIcon;
	
	protected Filter value = new Filter();
	protected Filter baseFilter = new Filter();

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
		if(fireEvents) {
			ValueChangeEvent.fireIfNotEqual(this, this.value, value);
		}
		this.value = value;
		updateView();
	}

	protected void updateView() {
	
	}
	
	@Override
	public final HandlerRegistration addValueChangeHandler(
			ValueChangeHandler<Filter> handler) {
		return addHandler(handler, ValueChangeEvent.getType());
	}

	@Override
	public void applyBaseFilter(Filter filter) {
		this.baseFilter = filter;
	}
}

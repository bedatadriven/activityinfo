package org.sigmah.client.page.common.filter;

import org.sigmah.client.dispatch.Dispatcher;
import org.sigmah.shared.command.Filter;
import org.sigmah.shared.report.model.DimensionType;

import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.TreePanelEvent;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;

public class IndicatorFilterPanel extends IndicatorTreePanel implements FilterPanel {
		
	public IndicatorFilterPanel(Dispatcher service, boolean multipleSelection) {
		super(service, multipleSelection);
		
		addCheckChangedListener(new Listener<TreePanelEvent>() {
			
			@Override
			public void handleEvent(TreePanelEvent be) {
				ValueChangeEvent.fire(IndicatorFilterPanel.this, getValue());
			}
		});
	}

	@Override
	public Filter getValue() {
		Filter filter = new Filter();
		filter.addRestriction(DimensionType.Indicator, getSelectedIds());
		
		return filter;
	}

	@Override
	public void setValue(Filter value) {
		setSelection(value.getRestrictions(DimensionType.Indicator));
	}

	@Override
	public void setValue(Filter value, boolean fireEvents) {
		setSelection(value.getRestrictions(DimensionType.Indicator));
		if(fireEvents) {
			ValueChangeEvent.fire(this, value);
		}
	}

	@Override
	public HandlerRegistration addValueChangeHandler(
			ValueChangeHandler<Filter> handler) {
		return addHandler(handler, ValueChangeEvent.getType());
	}

	@Override
	public void applyBaseFilter(Filter filter) {
		// we don't filter indicators
	}

}

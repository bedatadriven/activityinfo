package org.activityinfo.client.report.editor.map.layerOptions;

import org.activityinfo.client.dispatch.Dispatcher;
import org.activityinfo.client.filter.FilterPanelSet;
import org.activityinfo.client.i18n.I18N;
import org.activityinfo.client.icon.IconImageBundle;
import org.activityinfo.shared.command.Filter;
import org.activityinfo.shared.report.model.DimensionType;

import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.HasValue;
 
public class LayerFilterPanel extends ContentPanel implements HasValue<Filter> {
		
	private FilterPanelSet filterPanelSet;
	private Filter filter;
	private DateFilterWidget dateWidget;
	private PartnerFilterWidget partnerFilterWidget;
	
	public LayerFilterPanel(Dispatcher dispatcher) {
		FilterResources.INSTANCE.style().ensureInjected();
		
		initializeComponent();
		
		
		dateWidget = new DateFilterWidget();
		partnerFilterWidget = new PartnerFilterWidget(dispatcher);
		
		add(dateWidget);
		add(partnerFilterWidget);
		
		dateWidget.addValueChangeHandler(new ValueChangeHandler<Filter>() {
			@Override
			public void onValueChange(ValueChangeEvent<Filter> event) {
				createNewFilterAndFireEvent();
			}
		});
		
		partnerFilterWidget.addValueChangeHandler(new ValueChangeHandler<Filter>() {
			@Override
			public void onValueChange(ValueChangeEvent<Filter> event) {
				createNewFilterAndFireEvent();
			}
		});
		
		filterPanelSet = new FilterPanelSet(dateWidget, partnerFilterWidget);
	}

	private void createNewFilterAndFireEvent() {
		Filter filter = new Filter();
		Filter partnerFilter = partnerFilterWidget.getValue();
		if (partnerFilter.hasRestrictions()) {
			filter.addRestriction(DimensionType.Partner, partnerFilter.getRestrictions(DimensionType.Partner));
		}
		filter.setDateRange(dateWidget.getValue().getDateRange());
		setValue(filter);
	}

	private void initializeComponent() {
		setHeading(I18N.CONSTANTS.filter());
		setIcon(IconImageBundle.ICONS.filter());
	}
	
	public FilterPanelSet getFilterPanelSet() {
		return filterPanelSet;
	}

	@Override
	public HandlerRegistration addValueChangeHandler(
			ValueChangeHandler<Filter> handler) {
		return addHandler(handler, ValueChangeEvent.getType());	
	}

	@Override
	public Filter getValue() {
		return filter;
	}

	@Override
	public void setValue(Filter value) {
		setValue(value, true);
	}

	@Override
	public void setValue(Filter value, boolean fireEvents) {
		if(value == null) {
			value = new Filter();
		} else {
			this.filter = value;
			dateWidget.setValue(value, false);
			partnerFilterWidget.setValue(value, false);
		}
		if(fireEvents) {
			ValueChangeEvent.fire(this, value);
		}
	}

}

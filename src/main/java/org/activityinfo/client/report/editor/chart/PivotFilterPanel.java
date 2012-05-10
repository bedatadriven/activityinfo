package org.activityinfo.client.report.editor.chart;

import org.activityinfo.client.EventBus;
import org.activityinfo.client.dispatch.Dispatcher;
import org.activityinfo.client.filter.AdminFilterPanel;
import org.activityinfo.client.filter.DateRangePanel;
import org.activityinfo.client.filter.FilterPanelSet;
import org.activityinfo.client.filter.IndicatorFilterPanel;
import org.activityinfo.client.filter.PartnerFilterPanel;
import org.activityinfo.client.i18n.I18N;
import org.activityinfo.client.page.report.HasReportElement;
import org.activityinfo.client.page.report.ReportChangeHandler;
import org.activityinfo.client.page.report.ReportEventHelper;
import org.activityinfo.shared.command.Filter;
import org.activityinfo.shared.report.model.PivotReportElement;

import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.layout.AccordionLayout;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;

public class PivotFilterPanel extends ContentPanel implements HasReportElement<PivotReportElement> {
	
	
	private final FilterPanelSet panelSet;
	private final ReportEventHelper events;
	
	private PivotReportElement model;
	
	public PivotFilterPanel(EventBus eventBus, Dispatcher dispatcher) {
		this.events = new ReportEventHelper(eventBus, this);

		setLayout(new AccordionLayout());
		setHeading(I18N.CONSTANTS.filter());

		IndicatorFilterPanel indicatorPanel = new IndicatorFilterPanel(dispatcher, true);
		indicatorPanel.setHeaderVisible(true);
		add(indicatorPanel);

		AdminFilterPanel adminFilterPanel = new AdminFilterPanel(dispatcher);
		add(adminFilterPanel);

		DateRangePanel dateFilterPanel = new DateRangePanel();
		add(dateFilterPanel);

		PartnerFilterPanel partnerFilterPanel = new PartnerFilterPanel(dispatcher);
		add(partnerFilterPanel);
		
		panelSet = new FilterPanelSet(indicatorPanel, adminFilterPanel, dateFilterPanel, partnerFilterPanel);
		panelSet.addValueChangeHandler(new ValueChangeHandler<Filter>() {
			
			@Override
			public void onValueChange(ValueChangeEvent<Filter> event) {
				model.setFilter(event.getValue());
				events.fireChange();
			}
		});
		
		events.listen(new ReportChangeHandler() {
			
			@Override
			public void onChanged() {
				panelSet.setValue(model.getFilter());
			}
		});
	}	
	
	
	@Override
	public void bind(PivotReportElement model) {
		this.model = model;
		panelSet.setValue(model.getFilter(), false);
	}

	@Override
	public PivotReportElement getModel() {
		return model;
	}


	public void applyBaseFilter(Filter filter) {
		panelSet.applyBaseFilter(filter);
	}


	@Override
	public void disconnect() {
		events.disconnect();
	}

}

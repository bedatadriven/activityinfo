package org.activityinfo.client.page.entry;

import org.activityinfo.client.dispatch.Dispatcher;
import org.activityinfo.client.filter.AdminFilterPanel;
import org.activityinfo.client.filter.DateRangePanel;
import org.activityinfo.client.filter.FilterPanelSet;
import org.activityinfo.client.filter.PartnerFilterPanel;
import org.activityinfo.client.i18n.I18N;

import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.layout.AccordionLayout;

public class FilterPane extends ContentPanel {

	private final FilterPanelSet filterPanelSet;
	
	public FilterPane(Dispatcher dispatcher) {
		setHeading(I18N.CONSTANTS.filter());
		setLayout(new AccordionLayout());
	
		ActivityFilterPanel activityFilterPanel = new ActivityFilterPanel(dispatcher);
		AdminFilterPanel adminFilterPanel = new AdminFilterPanel(dispatcher);
		DateRangePanel datePanel = new DateRangePanel();
		PartnerFilterPanel partnerPanel = new PartnerFilterPanel(dispatcher);
		
		add(activityFilterPanel);
		add(adminFilterPanel);
		add(datePanel);
		add(partnerPanel);
		
		filterPanelSet = new FilterPanelSet(activityFilterPanel, adminFilterPanel, datePanel, partnerPanel);
		
	}
	
	public FilterPanelSet getSet() {
		return filterPanelSet;
	}
	
}

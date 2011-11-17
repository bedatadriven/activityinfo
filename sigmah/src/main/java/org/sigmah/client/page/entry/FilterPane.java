package org.sigmah.client.page.entry;

import org.sigmah.client.dispatch.Dispatcher;
import org.sigmah.client.i18n.I18N;
import org.sigmah.client.page.common.filter.AdminFilterPanel;
import org.sigmah.client.page.common.filter.DateRangePanel;
import org.sigmah.client.page.common.filter.FilterPanelSet;
import org.sigmah.client.page.common.filter.PartnerFilterPanel;

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
